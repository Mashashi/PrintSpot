package de;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen; 
import javax.websocket.Session; 
import javax.websocket.server.ServerEndpoint; 
import javax.ejb.*;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@MessageDriven(
		activationConfig = { 
				@ActivationConfigProperty(propertyName = "destination", propertyValue = "PhysicalQueueOpened"), 
				@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
		}, 
		mappedName = "PhysicalQueue")
@ServerEndpoint("/notifyRegistration") 
public class WebSocketTest { 
	private static ArrayList<Session> session = new ArrayList<Session>();
	@OnMessage 
	public void onMessage(String message, Session session) throws IOException, InterruptedException { 
		System.out.println("Message: " + message); 
		session.getBasicRemote().sendText("Hello Mr. " + message);
	} 
	@OnOpen 
	public void onOpen(Session session) { 
		System.out.println("Client connected");
		synchronized(WebSocketTest.session){
			WebSocketTest.session.add(session);
		}
	} 
	@OnClose 
	public void onClose(Session session) { 
		System.out.println("Connection closed"); 
		synchronized(WebSocketTest.session){
			WebSocketTest.session.remove(session);
		}
	} 
	
	public void onMessage(Message message) {
		try {
			synchronized(WebSocketTest.session){
				Iterator<Session> ite = session.iterator();
				while(ite.hasNext()){
					Session n = ite.next();
					if(n.isOpen()){
						String s = ((TextMessage)message).getText();
						String prefix = "registration:";
						if(s.startsWith(prefix)){
							n.getBasicRemote().sendText(s.substring(prefix.length()));
						}
						System.out.println("Sent ws notification");
					}else{
						ite.remove();
					}
				}
			}
		} catch (IOException|JMSException e) {
			System.out.println("Ws notification failed");
		}
    }
}