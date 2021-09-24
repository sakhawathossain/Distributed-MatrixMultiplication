import java.net.InetAddress;
import java.util.Scanner;

public class Master implements WorkerHandler{

    private WorkerList workerList;
    private DistributedJob pendingJob;
    private Scanner scanner;

    public static void main(String[] args) throws Exception{
        System.setProperty("java.security.policy","file:./security.policy");
        System.setSecurityManager(new SecurityManager());
        Master m = new Master();
        m.runMain();
        System.exit(0);
    }

    public Master(){
        System.out.println("Initializing Master Program");
        workerList = new WorkerList();
        scanner = new Scanner(System.in);
        System.out.println("Written by Sakhawat Hossain Saimon for CS5523 / Fall 2020 / UTSA");
    }

    public void runMain() throws Exception {
        /** Sign up code */
        WorkerAcceptor workerAcceptor = new WorkerAcceptor(this);
        Thread acceptorThread = new Thread(workerAcceptor);
        acceptorThread.start();
        String address = Translator.getAddressFromIP(InetAddress.getLocalHost());
        System.out.println("Address: " + address);
        menu();
        workerAcceptor.stop();
        scanner.close();
        return;
    }

    private void loadJob(){
        try{
            // /** Job Specifications **/
            DistributedJob distJob = new DistributedJob();
            if(distJob.isInvalid()){
                System.out.println("Invalid dimensions for multiplication!");
                System.out.println("Halting");
                System.exit(0);
            }
            if(this.workerList.getWorkerCount() < 1){
                System.out.println("No workers available, job can not be executed");
                pendingJob = distJob;
                distJob = null;
                return;
            }
            pendingJob = null;
            Matrix result = (Matrix) this.workerList.deployJob(distJob);
            System.out.println("Result acquired from worker");
            Matrix.writeAsCSV(result, "testoutput.txt");
        }catch(Exception e){
            System.out.println("Something wrong with Master");
            e.printStackTrace();
        }
    }

    private void loadPendingJob(){
        DistributedJob distJob = pendingJob;
        pendingJob = null;
        try{
            Matrix result = (Matrix) this.workerList.deployJob(distJob);
            System.out.println("Result acquired from worker");
            Matrix.writeAsCSV(result, "testoutput.txt");
        }catch(Exception e){
            System.out.println("Something wrong with Master");
            e.printStackTrace();
        }
    }

    private void menu(){
        String command = "";
            
            try{
                loadJob();
            }catch(Exception e){
                System.out.println("Goodbye!");
                return;
            }
    }

    @Override
    public void onArrive(WorkerRecord workerRecord) {
        System.out.println("New worker " + workerRecord.getName() + " just signed up!");
        workerList.add(workerRecord);
        if(pendingJob != null){
            loadPendingJob();
        }
    }
}
