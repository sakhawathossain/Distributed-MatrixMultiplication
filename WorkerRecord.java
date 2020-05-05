import java.io.Serializable;

public class WorkerRecord implements Serializable {
    private String name;
    private String address;
    private boolean isEngaged;
    private boolean isUnreachable;

    private static final long serialVersionUID = 1L;

    public WorkerRecord(String name, String address){
        this.name = name;
        this.address = address;
        this.isEngaged = false;
        this.isUnreachable = false;
    }

    public String getName(){
        return name;
    }

    public String getAddress(){
        return address;
    }

    public boolean getIsEngaged(){
        return isEngaged;
    }

    public void setIsEngaged(boolean engaged){
        this.isEngaged = engaged;
    }

    public boolean getIsUnreachable(){
        return isUnreachable;
    }

    public void setIsUnreachable(boolean isUnreachable){
        this.isUnreachable = isUnreachable;
    }
    
    public String toString(){
        return "Name: " + name + "\n" + "Address: " + address;
    }
}