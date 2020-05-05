import java.io.ObjectOutputStream;
import java.net.*;
import java.rmi.*;
import java.util.Scanner;

public class Worker {

    private static final String serverAddress = "//localhost:1099/Hello";

    public static void main(String[] args) throws Exception {
        System.setProperty("java.security.policy","file:./security.policy");
        System.setSecurityManager(new SecurityManager());

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
        // os.close();
        // s.close();
        try {
            Naming.rebind(serverAddress, new JobAcceptorImpl());
            System.out.println("Worker is ready");
        } catch (Exception e) {
            System.out.println("Something went wrong with the worker");
            e.printStackTrace();
        }
    }
}