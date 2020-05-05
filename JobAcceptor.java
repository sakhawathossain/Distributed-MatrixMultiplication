import java.rmi.*;

public interface JobAcceptor extends Remote{

    public boolean ping() throws RemoteException;

    public Object executeJob(Job job) throws RemoteException;
}