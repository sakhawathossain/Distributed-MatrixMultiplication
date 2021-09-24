import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;

public class WorkerList {

    private List<WorkerRecord> workers;
    private int loopstart = 0;

    public WorkerList(){
        workers = new ArrayList<WorkerRecord>();
    }

    public synchronized void resetList(){
        for(WorkerRecord workerRecord: workers){
            // do nothing
            try{
                JobAcceptor jobAcceptor = (JobAcceptor) Naming.lookup(workerRecord.getAddress());
                if(!jobAcceptor.ping()){
                    workerRecord.setStatus(Status.ENGAGED);;
                }
            }catch(Exception e){
                System.out.println("Failed to contact " + workerRecord.getName());
                workerRecord.setStatus(Status.UNREACHABLE);
            }
        }
        for(int i = workers.size()-1; i >= 0 ; i--){
            WorkerRecord workerRecord = workers.get(i);
            if(workerRecord.getStatus() == Status.UNREACHABLE){
                System.out.println("Removing " + workerRecord.getName());
                workers.remove(i);
            }
        }
    }

    public synchronized WorkerRecord findIdle(){

        for(int i = loopstart; i < loopstart + workers.size(); i++){
            int index = i % workers.size();
            if(workers.get(index).getStatus() ==  Status.IDLE){
                loopstart++;
                return workers.get(index);
            }
        }
        return null;
    }


    public synchronized void add(WorkerRecord worker){
        worker.setStatus(Status.IDLE);
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

    public Object deployJob(DistributedJob distJob){
        System.out.println("Starting deployment");
        System.out.println("Number of jobs: " + distJob.getSplitCount());
        JobStatus[] jobStatus = new JobStatus[distJob.getSplitCount()];
        WorkerRecord[] jobHolder = new WorkerRecord[distJob.getSplitCount()];
        double[] results = new double[distJob.getSplitCount()];
        for(int i = 0; i < jobStatus.length ; i++){
            jobStatus[i] = JobStatus.WAITING; 
        }
        boolean allDone = false;
        while(!allDone){
            allDone = true;
            for(int i = 0; i < jobStatus.length ; i++){
                // detect unfinished jobs
                if(jobStatus[i] == JobStatus.WAITING){
                    WorkerRecord workerRecord = findIdle();
                    if(workerRecord != null){
                        try{
                            JobAcceptor jobAcceptor = (JobAcceptor) Naming.lookup(workerRecord.getAddress());
                            results[i] = (Double) jobAcceptor.executeJob(distJob.getSplitData(i));
                            jobStatus[i] = JobStatus.RUNNING;
                            jobHolder[i] = workerRecord;
                        }catch(Exception re){ 
                            resetList();
                            System.out.println("Exception at new job assignment");   
                            re.printStackTrace();
                            return null;                    
                        }
                    }
                    allDone = false;
                }

                // check if running jobs have been orphaned
                else if(jobStatus[i] == JobStatus.RUNNING){
                    WorkerRecord workerRecord = jobHolder[i];
                    try{
                        JobAcceptor jobAcceptor = (JobAcceptor) Naming.lookup(workerRecord.getAddress());
                        if(!jobAcceptor.ping()){
                            resetList();
                            jobHolder[i] = null;
                            jobStatus[i] = JobStatus.ORPHANED;
                            allDone = false;
                        }
                    }catch(Exception e){
                        resetList();
                        System.out.println("Exception at running job check"); 
                        jobHolder[i] = null;
                        jobStatus[i] = JobStatus.ORPHANED;
                        allDone = false;
                    }
                }

                else{
                    jobHolder[i].setStatus(Status.IDLE);
                }
            }
        }
        System.out.println("finished everything"); 
        Matrix result = distJob.combineResults(results);
        return result;
    }

    public void getWorkerStats(){
        for(WorkerRecord wr: workers){
            System.out.println("Worker name : "+ wr.getName() + ", address: " + wr.getAddress());
        }
    }
}

enum JobStatus{
    WAITING,
    RUNNING,
    ORPHANED,
    FINISHED
}