import java.net.InetAddress;
import java.rmi.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Master implements WorkerHandler{

    private WorkerList workerList;

    public static void main(String[] args) throws Exception{
        Master m = new Master();
        m.runMain();
    }

    public Master(){
        workerList = new WorkerList();
    }

    public void runMain() throws Exception {
        System.setProperty("java.security.policy","file:./security.policy");
        System.setSecurityManager(new SecurityManager());

        /** Sign up code */
        Thread acceptorThread = new Thread(new WorkerAcceptor(this));
        acceptorThread.start();
        String address = Translator.getAddressFromIP(InetAddress.getLocalHost());
        System.out.println("Address: " + address);


        try{
            // /** Job Specifications **/
            int[] tempArr1 = {4, 2, 3, 7};
            int[] tempArr2 = {1, 8, 6, 5};
            List<Integer> arr1 = new ArrayList<Integer>();
            List<Integer> arr2 = new ArrayList<Integer>();
            for(int i = 0; i < tempArr1.length; i++)
                arr1.add(tempArr1[i]);
            for(int i = 0; i < tempArr2.length; i++)
                arr2.add(tempArr2[i]);
                
            // JobAcceptor sorter1 = (JobAcceptor) Naming.lookup("//localhost:1099/Hello");
            // JobAcceptor sorter2 = (JobAcceptor) Naming.lookup("//localhost:1099/Hello2");
            Scanner sc = new Scanner(System.in);
            System.out.println("Temporary roadblock!");
            sc.nextLine();
            JobAcceptor sorter1 = workerList.get(0);
            //JobAcceptor sorter2 = workerList.get(1);
            Job job1 = new SortingJob(arr1);
            //Job job2 = new SortingJob(arr2);

            List<Integer> result1 = (List<Integer>) sorter1.executeJob(job1);
            //List<Integer> result2 = (List<Integer>) sorter2.executeJob(job2);

            System.out.println("Result acquired from worker");
            System.out.println(result1);
            //System.out.println(result2);
        }catch(Exception e){
            System.out.println("Something wrong with Master");
            e.printStackTrace();
        }
    }

    @Override
    public void onArrive(WorkerRecord workerRecord) {
        System.out.println("New worker " + workerRecord.getName() + " just signed up!");
        workerList.add(workerRecord);
    }
}
