package presenter.event;

public interface ScorePresent{
	
	void respondEventNotFinished();
	
	void updateView(double score);
	
	void respondIsNotSignedUp();
}
