import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;

public class WorkerList {

    private List<WorkerRecord> workers;

    public WorkerList(){
        workers = new ArrayList<WorkerRecord>();
    }

    public synchronized void resetList(){
        for(WorkerRecord workerRecord: workers){
            // do nothing
            try{
                JobAcceptor jobAcceptor = (JobAcceptor) Naming.lookup(workerRecord.getAddress());
                if(!jobAcceptor.ping()){
                    workerRecord.setIsEngaged(true);
                }
            }catch(Exception e){
                System.out.println("Failed to contact " + workerRecord.getName());
                workerRecord.setIsEngaged(false);
                workerRecord.setIsUnreachable(true);
            }
        }
        for(int i = workers.size()-1; i >= 0 ; i--){
            WorkerRecord workerRecord = workers.get(i);
            if(workerRecord.getIsUnreachable()){
                System.out.println("Removing " + workerRecord.getName());
                workers.remove(i);
            }
        }
    }

    public synchronized void add(WorkerRecord worker){
        worker.setIsEngaged(false);
        this.workers.add(worker);
    }

    public synchronized void remove(int index){
        workers.remove(index);
    }

    public synchronized int getWorkerCount(){
        return this.workers.size();
    }

    public synchronized JobAcceptor get(int index){
        if(index < 0 || index >= workers.size()){
            System.out.println("Invalid index " + index + " (range " + 0 + "- " + (workers.size()-1));
            return null;
        }
        try{
            return  (JobAcceptor) Naming.lookup(workers.get(index).getAddress());
        }catch(Exception e){
            System.out.println("Could not retrieve worker " + workers.get(index).getName());
            remove(index);
            return null;
        }
    }
}