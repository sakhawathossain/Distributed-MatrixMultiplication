import java.util.List;

public abstract class DistributedJob {

    private Object data;

    public DistributedJob(Object data){
        this.data = data;
    }

    public abstract Object getSplitData(int splitIndex);

    public abstract Object combineResults(List<Object> results);

}