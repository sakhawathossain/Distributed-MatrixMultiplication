import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Serializable;
import java.util.Scanner;

public class Matrix implements Serializable{

    
    /**
     * Code based on
     * https://gist.github.com/hallazzang/4e6abbb05ff2d3e168a87cf10691c4fb
     * https://stackoverflow.com/questions/26460248/
     **/

    private static final long serialVersionUID = 1L;
    private double[][] data = null;
    private int rows = 0, cols = 0;

    public Matrix(int rows, int cols){
        this.data = new double[rows][cols];
        this.rows = rows;
        this.cols = cols;
    }

    public Matrix(double[][] data) {
        this.data = data.clone();
        rows = this.data.length;
        cols = this.data[0].length;
    }

    public Matrix(String path) {
        BufferedReader CSVFile;
        LineNumberReader lnr;
        try {
            CSVFile = new BufferedReader(new FileReader(path));
            // Figure out how many columns in matrix
            lnr = new LineNumberReader(new FileReader(path));
            int linenumber = 0;
            while (lnr.readLine() != null) {
                linenumber++;
            }
            this.rows = linenumber;
            // Figure out how many rows
            String dataRow = CSVFile.readLine(); // Read first line.
            String[] testArray = (dataRow + "").split(",");
            this.cols = testArray.length;
            // System.out.println("Initial data row: " + dataRow);
            // System.out.println("Number of columns: " + this.cols);
            // initialize matrix
            this.data = new double[this.rows][this.cols];
            for (int i = 0; i < this.rows; i++) {
                String[] dataArray = dataRow.split(",");
                for (int j = 0; j < this.cols; j++) {
                    double val = Double.parseDouble(dataArray[j]);
                    this.data[i][j] =  val;
                }
                dataRow = CSVFile.readLine(); // Read next line of data.
            }
            lnr.close();
            CSVFile.close();
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("Error: no such file exists");
        } catch (ArrayIndexOutOfBoundsException ae) {
            System.out.println("Error: matrix dimensions not consistent");
        } catch (IOException e) {
            System.out.println("Error: could not read file (I/O exception)");
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public double[] getRow(int index){
        // System.out.println("Asking for column " + index);
        // System.out.printf("Rows %d, cols %d\n", this.rows, this.cols);
        // System.out.println("\t\t new job");
        // System.out.println("\tRow to be multiplied:");
        // double[] row = this.data[index];
        // for(int i = 0; i < row.length; i++)
        //     System.out.printf("%f ", row[i]);
        return this.data[index];
    }

    public double[] getColumn(int index){
        // System.out.println("Asking for column " + index);
        // System.out.printf("Rows %d, cols %d\n", this.rows, this.cols);
        double[] column = new double[this.rows];
        for(int i = 0; i < this.rows; i++){
            // System.out.printf("data[%d][%d]\n", i, index);
            column[i] = data[i][index];
        }
        //double[] col = column;
        // System.out.println("\nColumn to be multiplied:");
        // for(int i = 0; i < col.length; i++)
            //System.out.printf("%f ", col[i]);
        return column;
    }

    public int[] getDims(){
        return new int[]{this.rows, this.cols};
    }

    public int getRowCount(){ return this.rows;}

    public int getColCount(){ return this.cols;}

    public String toString(){
        String out = "";
        System.out.printf("Rows: %d, cols: %d\n", this.rows, this.cols);
        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.cols; j++){
                out = out + this.data[i][j];
                if(j < this.cols - 1)
                    out += ",";
            }
            out = out + "\n";
        }
        return out;
    }

    public void setValue(double d, int rowIndex, int colIndex){
        this.data[rowIndex][colIndex] = d;
    }

    public static void writeAsCSV(Matrix matrix, String filename){
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(filename);
            fileWriter.write(matrix.toString());
            fileWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}