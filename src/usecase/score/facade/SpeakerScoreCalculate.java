package usecase.score.facade;

import entity.event.EventVisitor;
import entity.event.MultiSpeakerEvent;
import entity.event.NonSpeakerEvent;
import entity.event.Talk;
import usecase.event.EventManage;
import usecase.score.ScoreManage;

import java.io.Serializable;
import java.util.UUID;

public class SpeakerScoreCalculate implements EventVisitor, Serializable{
	
	private final ScoreManage sm;
	private final EventManage em;
	
	private double amount;
	private int    size;
	private UUID   speaker;
	
	public SpeakerScoreCalculate(ScoreManage sm, EventManage em){
		this.sm = sm;
		this.em = em;
	}
	
	public double getSpeakerScore(UUID speaker){
		this.amount = 0d;
		this.size = 0;
		this.speaker = speaker;
		for(UUID id : em.getAll()){
			em.get(id).accept(this);
		}
		if(size != 0){
			return amount / size;
		}else{
			return 0;
		}
	}
	
	@Override
	public void visitTalk(Talk talk){
		if(talk.getSpeaker() != null && talk.getSpeaker().equals(speaker)){
			addScore(talk.getID());
		}
	}
	
	@Override
	public void visitNonSpeakerEvent(NonSpeakerEvent nse){
		// do nothing
	}
	
	@Override
	public void visitMultiSpeakerEvent(MultiSpeakerEvent mse){
		if(mse.getSpeakers().contains(speaker)){
			addScore(mse.getID());
		}
	}
	
	private void addScore(UUID event){
		double score = sm.getEventScore(event);
		if(score != 0){
			amount += score;
			size++;
		}
	}
}
