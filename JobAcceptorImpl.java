import java.rmi.*;
import java.rmi.server.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class JobAcceptorImpl extends UnicastRemoteObject implements JobAcceptor{

    private static final long serialVersionUID = 1L;

    public JobAcceptorImpl() throws RemoteException{
        super();
    }

    public boolean ping() throws RemoteException{
        return true;
    }

    public Object executeJob(Job job) throws RemoteException{
        System.out.println("New job arrived, executing");
        Object returnVal = null;
        ExecutorService jobThread = Executors.newSingleThreadExecutor();
        Callable<Object> callable = new Callable<Object>(){
            @Override
            public Object call(){
                return job.execute();
            }
        };
        try{
            Future<Object> future = jobThread.submit(callable);
            returnVal = future.get();
        } catch(Exception e){
            System.out.println("Thread exception");
            e.printStackTrace();
        } finally{
            jobThread.shutdown();
        }
        System.out.println("Finished execution");
        return returnVal;
    }

}