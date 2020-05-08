import java.io.Serializable;
import java.util.Scanner;

public class DistributedJob implements Serializable{

    private static final long serialVersionUID = 2L;

    private Matrix mat1;
    private Matrix mat2;
    private int maxSplit;
    private transient Scanner scanner = new Scanner(System.in);

    public DistributedJob(){
        System.out.println("Enter first matrix tsv file path:");
        String f1 = scanner.next();
        System.out.println("Enter second matrix tsv file path:");
        String f2 = scanner.next();
        this.mat1 = new Matrix(f1);
        this.mat2 = new Matrix(f2);
        if(mat1.getRowCount() != mat2.getColCount()){
            System.out.println("Invalid dimensions for multiplication!");
            return;
        }
        this.maxSplit = mat1.getColCount() * mat2.getRowCount();
        //System.out.println("Matrix:\n" + mat1.toString());
    }

    public int getSplitCount(){ return this.maxSplit; }

    public Job getSplitData(int splitIndex){
        int rowIndex = splitIndex / mat1.getRowCount();
        int colIndex = splitIndex - rowIndex * mat1.getRowCount();
        //System.out.println("\tDistributed Job class");
        //System.out.println("\tsplit index: " + splitIndex);
        //System.out.println("\tderived row: " + rowIndex);
        //System.out.println("\tderived col: " + colIndex);
        
        double[] row = mat1.getRow(rowIndex);
        double[] col = mat1.getColumn(colIndex);
        // debug
        // System.out.println("\n\n new job");
        // System.out.println("\nRow to be multiplied:");
        // for(int i = 0; i < row.length; i++)
        //     System.out.printf("%f ", row[i]);
        // System.out.println("\nColumn to be multiplied:");
        // for(int i = 0; i < col.length; i++)
        //     System.out.printf("%f ", col[i]);

        Job job = new Job(new double[][]{row, col}){
        
            @Override
            public Object execute() {
                // TODO Auto-generated method stub
                double[] row = ((double[][]) getData()) [0];
                double[] col = ((double[][]) getData()) [1];
                double result = 0;
                for(int i = 0; i < row.length; i++)
                    result = result + row[i] * col[i];
                return result;
            }
        };
        return job;
    }

    public Matrix combineResults(double[] results){
        int rowCount = (results.length-1) / mat1.getRowCount();
        int colCount = (results.length-1) - rowCount * mat1.getRowCount();
        // test if results ok
        // for(int i = 0; i < results.length; i++){
        //     System.out.println("Resul[" + i + "] = " + results[i]);
        // }
        System.out.println("Combine step");
        Matrix matrix = new Matrix(rowCount+1, colCount+1);
        for(int splitIndex = 0; splitIndex < results.length; splitIndex++){
            int rowIndex = splitIndex / mat1.getRowCount();
            int colIndex = splitIndex - rowIndex * mat1.getRowCount();
            //System.out.printf("Placing results[%d] = %f into matrix[%d][%d]\n", 
            //    splitIndex, results[splitIndex], rowIndex, colIndex);
            Double val = (Double) results[splitIndex];
            matrix.setValue(val, rowIndex, colIndex);
        }
        return matrix;
    }
}