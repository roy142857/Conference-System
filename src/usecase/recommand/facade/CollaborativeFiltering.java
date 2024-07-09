package usecase.recommand.facade;

import usecase.event.EventManage;
import usecase.people.PeopleManage;
import usecase.score.ScoreManage;

import java.io.Serializable;
import java.util.*;

public class CollaborativeFiltering implements Serializable{
    private final EventManage em;
    private final PeopleManage pm;
    private final ScoreManage sm;
    private final SimilarityCalculate sc;
    public  final double BENCHMARK = 3;

    public CollaborativeFiltering(EventManage em, PeopleManage pm, ScoreManage sm) {
        this.em = em;
        this.pm = pm;
        this.sm = sm;
        this.sc = new SimilarityCalculate(em, pm, sm);
    }

    /**
     * The method is used to normalize scores by subtracting the average of each list
     *
     * @param scores represents the scores of each event for every user
     * @return the normalized scores for each user
     */
    public Map<UUID, ArrayList<Double>> normalizeScore(Map<UUID, ArrayList<Double>> scores){
        Map<UUID, ArrayList<Double>> new_scores = new HashMap<>();

        ArrayList<Double> averages = new ArrayList<>();

        for(UUID person : scores.keySet()){
            double sum = 0;
            int count = 0;
            for(double score : scores.get(person)){
                if(score != 0){
                    count += 1;
                    sum += score;
                }
            }
            averages.add(sum / count);
        }

        int index = 0;
        for(UUID person : scores.keySet()){
            ArrayList<Double> new_score = new ArrayList<>();
            for(double score : scores.get(person)){
                if(score != 0){
                    new_score.add(score / averages.get(index));
                }
                else{
                    new_score.add(0d);
                }
            }
            new_scores.put(person, new_score);
            index ++;
        }

        return new_scores;
    }

    /**
     * The method is used to get the similarity for the person between other users
     *
     * @param scores represents the scores of each event for every user
     * @param person represents the person used to calculate the similarity with others.
     * @return the similarity with other users
     */
    public Map<UUID, Double> getSimilarity(Map<UUID, ArrayList<Double>> scores, UUID person){
        ArrayList<Double> scoreA = scores.get(person);
        Map<UUID, Double> result = new HashMap<>();
        scores.remove(person);

        for(UUID p : scores.keySet()){
            ArrayList<Double> scoreB = scores.get(p);
            result.put(p, sc.calSimilarity(scoreA, scoreB));
        }

        return result;
    }

    /**
     * The method is used to get the users that we would use their similarity to predict the scores.
     *
     * @param index represents the index of the event required to predict the rate
     * @param scores represents the scores of each event for every user
     * @param similarities represents the similarity with other users
     * @return the list of users
     */
    public List<UUID> getRates(int index, Map<UUID, ArrayList<Double>> scores, Map<UUID, Double> similarities){
        List<UUID> result = new ArrayList<>();
        Map<UUID, Double> copy = new HashMap<>();

        for(UUID p : similarities.keySet()){
            copy.put(p, similarities.get(p));
        }

        List<Map.Entry<UUID, Double>> list = new ArrayList<>(copy.entrySet());
        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

        for(Map.Entry<UUID, Double> m : list){
            UUID r = m.getKey();
            if(scores.get(r).get(index) != 0){
                result.add(m.getKey());
            }
        }

        if(result.size() > 2){
            return result.subList(0, 2);
        }

        return result;
    }

    /**
     * The method is used to predict rates for the events that the person did not participate before.
     *
     * @param scores the original score of each event for every user
     * @param person the person required to predict the rates
     * @param similarities the similarities between the person and other users
     * @return the predict rates of not attended events
     */
    public Map<UUID, Double> predictRates(Map<UUID, ArrayList<Double>> scores, UUID person,
                                          Map<UUID, Double> similarities){
        List<UUID> events = em.getAll();
        ArrayList<Double> p_score = scores.get(person);
        Map<UUID, Double> results = new HashMap<>();

        int index = p_score.indexOf(0.d);

        while(index != -1){
            List<UUID> rates = getRates(index, scores, similarities);
            double predict = 0;
            if(rates.size() == 2){
                UUID p1 = rates.get(0);
                UUID p2 = rates.get(1);
                double r1 = similarities.get(p1);
                double r2 = similarities.get(p2);
                predict = scores.get(p1).get(index) * r1 + scores.get(p2).get(index) * r2;
            }
            else if(rates.size() == 1){
                UUID p = rates.get(0);
                double r = similarities.get(p);
                predict = scores.get(p).get(index) * r;
            }
            results.put(events.get(index), predict);
            p_score.set(index, -1.0);
            index = p_score.indexOf(0.d);
        }
        return results;
    }

    /**
     * The method is used to get the list of UUID for events which would be recommended for the person.
     *
     * @param person represents the person required recommendation of events
     * @return the list of UUID of events
     */
    public LinkedHashMap<UUID, Double> getEvent_prompt(UUID person){
        LinkedHashMap<UUID, Double> event_prompt = new LinkedHashMap<>();

        List<UUID> events = em.getAll();
        List<UUID> users = pm.getAllAttendee();

        Map<UUID, ArrayList<Double>> raw_scores = new HashMap<>();

        for(UUID p : users){
            ArrayList<Double> scores = new ArrayList<>();

            for(UUID e : events){
                double score = sm.getEventScore(e);
                if(score != 0){
                    scores.add(score);
                }else{
                    scores.add(0d);
                }
            }

            raw_scores.put(p, scores);
        }

        Map<UUID, ArrayList<Double>> nor_score = normalizeScore(raw_scores);
        Map<UUID, Double> similarities = getSimilarity(nor_score, person);

        Map<UUID, Double> predicted = predictRates(raw_scores, person, similarities);

        List<Map.Entry<UUID, Double>> list = new ArrayList<>(predicted.entrySet());
        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

        for(Map.Entry<UUID, Double> m : list){
            if(m.getValue() > BENCHMARK && !em.isSignedUp(m.getKey(), person)){
                event_prompt.put(m.getKey(), m.getValue());
            }else if(m.getValue() <= BENCHMARK){
                return event_prompt;
            }
        }

        return event_prompt;
    }
}
