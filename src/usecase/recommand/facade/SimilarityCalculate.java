package usecase.recommand.facade;

import usecase.event.EventManage;
import usecase.friends.manager.FriendsManage;
import usecase.people.PeopleManage;
import usecase.score.ScoreManage;

import java.io.Serializable;
import java.util.*;

public class SimilarityCalculate implements Serializable{
	
	public final  double        BENCHMARK = 0.6;
	private final EventManage   em;
	private final PeopleManage  pm;
	private final ScoreManage   sm;
	private       FriendsManage fm;
	
	public SimilarityCalculate(EventManage em, PeopleManage pm, ScoreManage sm){
		this.em = em;
		this.pm = pm;
		this.sm = sm;
	}
	
	public SimilarityCalculate(EventManage em, PeopleManage pm, ScoreManage sm, FriendsManage fm){
		this(em, pm, sm);
		this.fm = fm;
	}
	
	/**
	 * The method is used to do the similarity calculation
	 *
	 * @param scoreA represents the rates for every event in event list for one user
	 * @param scoreB represents the rates for every event in event list for another user
	 * @return the similarity between two list of scores
	 */
	public double calSimilarity(ArrayList<Double> scoreA, ArrayList<Double> scoreB){
		double dp = 0.0;
		double normA = 0.0;
		double normB = 0.0;
		for(int i = 0; i < scoreA.size(); i++){
			dp += scoreA.get(i) * scoreB.get(i);
			normA += Math.pow(scoreA.get(i), 2);
			normB += Math.pow(scoreB.get(i), 2);
		}
		
		if(normA == 0 || normB == 0){
			return 0;
		}
		
		return dp / (Math.sqrt(normA) * Math.sqrt(normB));
	}
	
	/**
	 * The method is used to get the similarity between two users
	 *
	 * @param userA one user required to get the similarity
	 * @param userB another user required to get the similarity
	 * @return the similarity between two users
	 */
	public double getSimilarity(UUID userA, UUID userB){
		ArrayList<Double> scoreA = new ArrayList<>();
		ArrayList<Double> scoreB = new ArrayList<>();
		List<UUID> events = em.getAll();
		
		Map<UUID, Double> infoA = sm.getPreference(userA);
		Map<UUID, Double> infoB = sm.getPreference(userB);
		
		for(UUID e : events){
			scoreA.add(infoA.getOrDefault(e, 0d));
			scoreB.add(infoB.getOrDefault(e, 0d));
		}
		
		return calSimilarity(scoreA, scoreB);
	}
	
	/**
	 * The method is used to get a list of users' UUID recommending to the person
	 *
	 * @param person represents the UUID of a person required recommendation of friends.
	 * @return the list of users' UUID recommended to the person
	 */
	public LinkedHashMap<UUID, Double> getPeople_prompt(UUID person){
		LinkedHashMap<UUID, Double> people_prompt = new LinkedHashMap<>();
		List<UUID> other_users = pm.getAllAttendee();
		other_users.remove(person);
		
		Map<UUID, Double> similarities = new HashMap<>();
		
		for(UUID u : other_users){
			if(! fm.isFriend(person, u)){ //check if the u is already person's friend
				similarities.put(u, getSimilarity(person, u));
			}
		}
		
		List<Map.Entry<UUID, Double>> list = new ArrayList<>(similarities.entrySet());
		list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
		
		for(Map.Entry<UUID, Double> m : list){
			if(m.getValue() > BENCHMARK){
				people_prompt.put(m.getKey(), m.getValue());
			}else{
				return people_prompt;
			}
		}
		
		return people_prompt;
	}
	
}
