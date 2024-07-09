package usecase.statistic;

import presenter.center.dashboard.OrganizerDashboardPresent;
import usecase.people.PeopleManage;
import usecase.score.ScoreManage;
import usecase.statistic.facade.SpeakerScoreConvert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Statistics implements IStatistic{
	
	private final int MAX_REPORT = 5;
	
	private SpeakerScoreConvert speakerScoreConvert;
	
	private ScoreManage  sm;
	private PeopleManage pm;
	
	public void initialize(ScoreManage sm, PeopleManage pm){
		speakerScoreConvert = new SpeakerScoreConvert(sm, pm);
		this.sm = sm;
		this.pm = pm;
	}
	
	@Override
	public void updateTopSpeakerView(OrganizerDashboardPresent presenter){
		List<UUID> result;
		Map<UUID, Double> map = new HashMap<>();
		List<UUID> speakerList = pm.getAllSpeaker();
//		List<UUID> clone_speakers_list = speakerList.stream().collect(Collectors.toList());
		for(UUID id : speakerList){
			double score = sm.getSpeakerScore(id);
			if(score != 0)
				map.put(id, score);
		}
		result = map.entrySet().stream()
		            .sorted(Map.Entry.comparingByValue()).limit(MAX_REPORT)
		            .map(Map.Entry::getKey).collect(Collectors.toList());
		presenter.updateTopSpeakerView(speakerScoreConvert.convert(result));
	}
}
