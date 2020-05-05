import java.util.List;

public class SortingJob extends Job {

    public SortingJob(Object data){
        super(data);
    }

    @Override
    public Object execute() {
        // Bubble sort
        List<Integer> arr = (List<Integer>) getData();
        for(int i = 0; i < arr.size()-1; i++){
            for(int j = 0; j < arr.size() - i - 1; j++){
                if(arr.get(j) > arr.get(j+1)){
                    int temp = arr.get(j);
                    arr.set(j, arr.get(j+1));
                    arr.set(j+1, temp);
                }
            }
        }
    return arr;
    }
}