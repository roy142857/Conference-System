package controller.session;

import gateway.SessionIO;
import javafx.application.Application;
import javafx.stage.Stage;
import usecase.people.PersonType;

import java.io.IOException;

public class ConferenceSystem extends Application{
	
	public static void main(String[] args){
		launch(args);
	}
	
	private final String SESSION_PATH = "./Session/Session.ser";
	
	private Session      session;
	private StageHandler stageHandler;
	
	@Override
	public void start(Stage primaryStage){
		Session session = new Session();
		this.session = session;
		loadDemo(session);
		primaryStage.setOnCloseRequest(windowEvent -> onClose());
		StageHandler stageHandler = new StageHandler(session);
		stageHandler.attachMenu("CSC207 Conference Project", primaryStage);
		stageHandler.show();
	}
	
	private void loadDemo(Session session){
		session.getPM().debugCreate(PersonType.ORGANIZER, "org1", new char[]{'1'});
		session.getPM().debugCreate(PersonType.ORGANIZER, "org2", new char[]{'2'});
		session.getPM().debugCreate(PersonType.SPEAKER, "spk1", new char[]{'1'});
		session.getPM().debugCreate(PersonType.SPEAKER, "spk2", new char[]{'2'});
		session.getPM().debugCreate(PersonType.SPEAKER, "spk3", new char[]{'3'});
		session.getPM().debugCreate(PersonType.ATTENDEE, "att1", new char[]{'1'});
		session.getPM().debugCreate(PersonType.ATTENDEE, "att2", new char[]{'2'});
		session.getPM().debugCreate(PersonType.ATTENDEE, "att3", new char[]{'3'});
		session.getPM().debugCreate(PersonType.ATTENDEE, "att4", new char[]{'4'});
	}
	
	private void loadSession(){
	
	}
	
	private void onClose(){
		System.out.println("called!");
		SessionIO io = new SessionIO();
		try{
			io.saveSession(SESSION_PATH, session);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
