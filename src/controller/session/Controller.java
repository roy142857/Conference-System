package controller.session;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable{
	
	protected Session      session;
	protected StageHandler stageHandler;
	
	public void initSession(Session session){
		this.session = session;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){}
	
	public void initStageHandler(StageHandler stageHandler){
		this.stageHandler = stageHandler;
	}
}
