import java.rmi.*;

public class Worker2 {

    public static void main(String[] args) throws Exception {
        System.setProperty("java.security.policy","file:./security.policy");
        System.setSecurityManager(new SecurityManager());
        try {
            Naming.rebind("//localhost:1099/Hello2", new JobAcceptorImpl());
            System.out.println("Worker2 is ready");
        } catch (Exception e) {
            System.out.println("Something went wrong with worker2");
            e.printStackTrace();
        }
    }
}