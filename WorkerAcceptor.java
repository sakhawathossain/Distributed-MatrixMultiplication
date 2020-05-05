import java.io.*;
import java.net.*;

public class WorkerAcceptor implements Runnable{

    private ServerSocket server;
    private Socket s;
    private ObjectInputStream is;

    private WorkerHandler workerHandler;

    public WorkerAcceptor(WorkerHandler workerHandler){
        this.workerHandler = workerHandler;
    }

    public String getAddress(){
        return null;
    }

    @Override
    public void run() {
        try{
            // Server will be started on 1700 port number
            server=new ServerSocket(1700);
            // Server listening for connection            
            while(true){
                s=server.accept();
                is = new ObjectInputStream(s.getInputStream());
                WorkerRecord workerRecord = (WorkerRecord) is.readObject();
                System.out.println(workerRecord.toString());
                workerHandler.onArrive(workerRecord);
            }
        }catch(Exception e){
            System.out.println("WorkerAcceptor socket error");
            e.printStackTrace();
            
        }
    }


}