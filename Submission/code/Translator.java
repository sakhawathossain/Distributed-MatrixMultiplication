import java.net.InetAddress;

public class Translator {
    public static String getAddressFromIP(InetAddress inetAddress){
        String ip = inetAddress.getHostAddress();
        String name = inetAddress.getHostName();
        String[] blocks = ip.split(".");
        return ip;
    }

    public static String getIPFromAddress(String address){
        return address;
    }
}