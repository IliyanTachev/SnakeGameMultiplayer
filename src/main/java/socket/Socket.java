package socket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import classes.ApplicationRunner;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

@ServerEndpoint("/home")
public class Socket {
    private ApplicationRunner applicationRunner = new ApplicationRunner();

    @OnOpen
    public String handOpen(Session session){
        return "[Server] Connection established.";
    }

    @OnClose
    public void handClose(){
        System.out.println("[Server] Connection closed.");
    }

    @OnMessage
    public void handMessage(Session session, String messageReceived){
        try {
                // Parse JSON
               JSONParser jsonParser = new JSONParser();
               JSONObject jsonData = (JSONObject) jsonParser.parse(messageReceived);
               if(jsonData.get("cmd") != null){
                   try {
                       session.getBasicRemote().sendText(applicationRunner.parseMethod(jsonData.get("cmd").toString(), (JSONObject) jsonData.get("params")));
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
        } catch (ParseException e) {
            try {
                session.getBasicRemote().sendText("json-invalid");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @OnError
    public void handError(Throwable t){
        System.out.println("Error on server.");
        t.printStackTrace();
    }
}