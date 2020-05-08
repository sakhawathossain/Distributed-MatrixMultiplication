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
        return;
    }

    private void loadJob(){
        try{
            // /** Job Specifications **/
            DistributedJob distJob = new DistributedJob();
            if(this.workerList.getWorkerCount() < 1){
                System.out.println("No workers available, job stored for later execution");
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
        while(true){
            System.out.println("\n-------MENU--------");
            System.out.println("Command list:");
            System.out.println("exit\t:\tclose program");
            System.out.println("load\t:\tload new matrix multiplication task");
            System.out.println("list\t:\tview list of workers");
            command = scanner.nextLine().toLowerCase();
            if(command.equals("exit")){
                System.out.println("Goodbye!");
                return;
            }else if(command.equals("load")){
                if(this.pendingJob != null){
                    System.out.println("A job is pending already, can not load new");
                }else{
                    loadJob();
                }
            }else if(command.equals("list")){
                this.workerList.getWorkerStats();
            }else{
                System.out.println("Invalid command");
            }
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
