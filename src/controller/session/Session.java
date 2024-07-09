package controller.session;

import usecase.commu.MessagingManage;
import usecase.commu.MessagingManager;
import usecase.download.DownloadManage;
import usecase.download.DownloadManager;
import usecase.event.EventManage;
import usecase.event.EventManager;
import usecase.friends.manager.FriendsManage;
import usecase.friends.manager.FriendsManager;
import usecase.people.PeopleManage;
import usecase.people.PeopleManager;
import usecase.recommand.RecommendManage;
import usecase.recommand.RecommendManager;
import usecase.score.ScoreManage;
import usecase.score.ScoreManager;
import usecase.statistic.IStatistic;
import usecase.statistic.Statistics;
import usecase.task.TaskManage;
import usecase.task.TaskManager;

import java.io.Serializable;
import java.util.UUID;

/**
 * Session contains all Managers, and also being Serializable.
 */
public class Session implements Serializable{
	
	// loggedIn is transient because we don't need to store it
	// when the session get restored, it needs to login again.
	transient private UUID loggedIn;
	
	// managers
	private final EventManage     em;
	private final PeopleManage    pm;
	private final FriendsManage   fm;
	private final MessagingManage mm;
	private final TaskManage      tm;
	private final RecommendManage rm;
	private final ScoreManage     sm;
	private final DownloadManage  dm;
	private final IStatistic      sta;
	
	public Session(){
		fm = new FriendsManager();
		em = new EventManager();
		pm = new PeopleManager();
		mm = new MessagingManager();
		tm = new TaskManager();
		rm = new RecommendManager();
		sm = new ScoreManager();
		dm = new DownloadManager();
		sta = new Statistics();
		
		em.initialize(pm, tm, mm, fm, sm);
		pm.initialize(em, fm);
		mm.initialize(pm, fm, em);
		tm.initialize(em, pm);
		rm.initialize(em, pm, sm, fm);
		sm.initialize(em);
		dm.initialize(em, pm);
		sta.initialize(sm, pm);
	}
	
	/**
	 * Get the loggedIn person's ID.
	 */
	public UUID getLoggedIn(){
		return loggedIn;
	}
	
	/**
	 * Set the loggedIn.
	 */
	public void setLoggedIn(UUID loggedIn){
		this.loggedIn = loggedIn;
	}
	
	/**
	 * Check the LoggedIn Can Attend.
	 */
	public boolean canAttend(){
		return pm.canAttend(getLoggedIn());
	}
	
	/**
	 * Check the LoggedIn can Speak.
	 */
	public boolean canSpeak(){
		return pm.canSpeak(getLoggedIn());
	}
	
	/**
	 * Check the LoggedIn can Organize.
	 */
	public boolean canOrganize(){
		return pm.canOrganize(getLoggedIn());
	}
	
	/**
	 * Return the EventManager
	 */
	public EventManage getEM(){
		return em;
	}
	
	/**
	 * Return the ChatManage
	 */
	public MessagingManage getMM(){
		return mm;
	}
	
	/**
	 * Return the PeopleManage
	 */
	public PeopleManage getPM(){
		return pm;
	}
	
	/**
	 * Return the FriendsManage
	 */
	public FriendsManage getFM(){
		return fm;
	}
	
	/**
	 * Return the TaskManage
	 */
	public TaskManage getTM(){
		return tm;
	}
	
	/**
	 * Return the RecommendsManage
	 */
	public RecommendManage getRM(){
		return rm;
	}
	
	/**
	 * Return the ScoreEventManage
	 */
	public ScoreManage getSM(){
		return sm;
	}
	
	/**
	 * Return the DownloadManage
	 */
	public DownloadManage getDM(){
		return dm;
	}
	
	/**
	 * Return the IStatistic
	 */
	public IStatistic getSTA(){
		return sta;
	}
}