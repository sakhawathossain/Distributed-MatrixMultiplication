import java.io.*;

public class MessageReceiver extends Thread{

    private InputStream inputStream;
    private BufferedReader receiveRead;
    private MessageHandler messageHandler;

    public MessageReceiver(MessageHandler messageHandler, InputStream inputStream){
        this.inputStream = inputStream;
        this.receiveRead = new BufferedReader(new InputStreamReader(inputStream));
        this.messageHandler = messageHandler;
    }

    public void run(){
        String receiveMessage;
        while(true){
            try{
                if((receiveMessage = receiveRead.readLine()) != null)  {
                    messageHandler.onReceived(receiveMessage);        
                }    
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}