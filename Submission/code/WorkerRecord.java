import java.io.Serializable;

public class WorkerRecord implements Serializable {
    private String name;
    private String address;
    private Status status;
    

    private static final long serialVersionUID = 1L;

    public WorkerRecord(String name, String address){
        this.name = name;
        this.address = address;
        this.status = Status.IDLE;
    }

    public String getName(){
        return name;
    }

    public String getAddress(){
        return address;
    }

    public Status getStatus(){
        return this.status;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setStatus(Status status){
        this.status = status;
    }
    
    public String toString(){
        return "Name: " + name + "\n" + "Address: " + address;
    }
}

enum Status{
    IDLE,
    ENGAGED,
    UNREACHABLE,
    COMPLETED
}