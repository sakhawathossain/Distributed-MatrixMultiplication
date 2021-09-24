import java.io.ObjectOutputStream;
import java.net.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Worker2 {

    private static final String serverAddress = "//localhost:1099/Hello2";
    private static JobAcceptor jobAcceptor;

    public static void main(String[] args) throws Exception {
        System.setProperty("java.security.policy","file:./security.policy");
        System.setSecurityManager(new SecurityManager());

        System.out.println("Initializing Worker Program");

        /** Sign up code */
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter node name");
        String name = scanner.nextLine();
        System.out.println("Please enter Master Address:");
        String address = scanner.next();
        
        Socket s = new Socket(address, 1700);
        ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
        WorkerRecord workerForm = new WorkerRecord(name, serverAddress);
        os.writeObject(workerForm);
        try {
            jobAcceptor = new JobAcceptorImpl();
            Naming.rebind(serverAddress, jobAcceptor);
            System.out.println("Worker is ready");
        } catch (Exception e) {
            System.out.println("Something went wrong with the worker");
            e.printStackTrace();
            System.exit(0);
        }
        if(scanner.next() != null){
            scanner.close();
            System.exit(0);
        }
    }
}