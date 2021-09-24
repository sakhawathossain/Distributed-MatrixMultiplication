import java.io.Serializable;

public abstract class Job implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Object data;

    public Job(Object data){
        this.data = data;
    }

    public Object getData(){
        return data;
    }

    public abstract Object execute();
}