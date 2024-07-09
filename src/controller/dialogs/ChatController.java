package controller.dialogs;

import controller.session.Controller;
import controller.session.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import presenter.chat.ChatPresent;
import usecase.commu.chat.manager.ChatManager;
import usecase.dto.MessageDTO;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class ChatController extends Controller{
	
	private final String CHAT_CSS = "/javafx/dialog/chatCSS.css";
	
	@FXML private WebView chatView;
	
	@FXML private Label    respond;
	@FXML private TextArea chatInput;
	@FXML private Button   send;
	
	private ChatManager       chat;
	private ChatPresenter     presenter;
	private DateTimeFormatter formatter;
	
	@Override
	public void initSession(Session session){
		super.initSession(session);
		formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd H:mm:ss");
	}
	
	public void initChat(ChatManager chat){
		this.chat = chat;
		this.presenter = new ChatPresenter();
		fetchView();
	}
	
	private void fetchView(){
		chat.updateView(session.getLoggedIn(), presenter);
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		super.initialize(url, resourceBundle);
		// initialize css
		String css = getClass().getResource(CHAT_CSS).toString();
		chatView.getEngine().setUserStyleSheetLocation(css);
		// hook the listeners
		send.setOnAction(actionEvent -> sendMessage());
		chatInput.setOnKeyPressed(this::sendMessageKeyPress);
	}
	
	private void sendMessageKeyPress(KeyEvent keyEvent){
		if(keyEvent.isShiftDown() && keyEvent.getCode().equals(KeyCode.ENTER)){
			sendMessage();
		}
	}
	
	private void sendMessage(){
		String message = chatInput.getText();
		// call usecase to process such message
		if(chat.sendMessage(session.getLoggedIn(), message, presenter)){
			fetchView();
			chatInput.clear();
		}
	}
	
	private class ChatPresenter implements ChatPresent{
		
		@Override
		public void updateView(List<MessageDTO> message, UUID owner){
			String html = generateHTML(message, owner);
			chatView.getEngine().loadContent(html, "text/html");
		}
		
		@Override
		public void respondNoAccess(String needingAccess){
			respond.setText("You need Access " + needingAccess + "!");
			respond.setTextFill(Color.RED);
		}
		
		@Override
		public void respondMessageEmpty(){
			respond.setText("Empty Message not allowed!");
			respond.setTextFill(Color.RED);
		}
		
		private String generateHTML(List<MessageDTO> messages, UUID owner){
			StringBuilder builder = new StringBuilder();
			// append some javascript for auto scrolling
			builder.append("<html>")
			       .append("<head>")
			       .append("<script language='javascript' type='text/javascript'>")
			       .append("function toBottom(){")
			       .append("window.scrollTo(0, document.body.scrollHeight);")
			       .append("}")
			       .append("</script>")
			       .append("</head>")
			       .append("<body onload='toBottom()'>")
			       .append("<div class='header'></div>");
			// append the messages
			for(MessageDTO message : messages){
				String currentMessage = message.getContent();
				String sender = message.getSender();
				String time = message.getTime().format(formatter);
				if(message.getSenderID().equals(owner)){
					// we use right_layout
					builder.append("<div class='right_layout'>");
				}else{
					// we use left_layout
					builder.append("<div class='left_layout'>");
				}
				builder.append("<div class='username'>").append(sender).append("</div>")
				       .append("<div class='content'>")
				       .append("<div class='time'>").append(time).append("</div>")
				       .append(currentMessage)
				       .append("</div>");
				builder.append("</div>");
			}
			// append the ending html and body tag
			builder.append("</body>");
			builder.append("</html>");
			return builder.toString();
		}
	}
}
