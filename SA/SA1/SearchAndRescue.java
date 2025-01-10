/**
*@author: Mithun Rameshkumar, Dartmouth CS10, Winter 2025
*Creating and showing a Search and Rescue Class
*Date: January 8, 2025
 */

public class SearchAndRescue {
    //Creates 2d Array
    double[][] grid;

    //Variables for number of rows and columns
    int numberOfRows, numberOfColumns;

    /**
     * Constructor for method
     * @param row - number of rows
     * @param column - number of columns
     */
    public SearchAndRescue(int row, int column){
        this.numberOfRows = row;
        this.numberOfColumns = column;

        //sets size of grid
        grid = new double[row][column];

        //initializes every grid square to 1/total number of grid squares
        for (int i = 0; i < row; i++){
            for (int j = 0; j < column; j++){
                grid[i][j] = (double) 1/(row*column);
            }
        }
    }

    /**
     * Sets grid probability
     * @param row - which row to change
     * @param col - which column to change
     * @param probability - what probability to do
     */
    public void setGridProbability(int row, int col, double probability){
        if (row < 0 || row >= numberOfRows || col< 0 || col >= numberOfColumns){
            System.out.println("Error: Invalid grid location");
            return;
        }

        if (probability < 0 || probability > 1){
            System.out.println("Error: invalid probability");
            return;
        }

        grid[row][col] = probability;
    }

    /**
     * Prints the location with the highest probability
     */
    public void printBestLocation(){
        int bRow = 0;
        int bColumn = 0;

        for (int i = 0; i < numberOfRows; i++){
            for (int j = 0; j < numberOfColumns; j++){
                if (grid[i][j] > grid[bRow][bColumn]){
                    bRow = i;
                    bColumn = j;
                }
            }
        }

        System.out.printf("Location (%d,%d) has the highest probability.%n", bRow, bColumn);
    }

    public static void main(String[] args) {
        //Class instantiation of 5x5 grid
        SearchAndRescue example = new SearchAndRescue(6,5);

        //Prints Starting Probalities of grid
        System.out.println("Starting probabilities:");
        for (double[] row: example.grid){
            for (double probability: row){
                System.out.print(probability + " ");
            }
            System.out.println();
        }
        System.out.println();

        //Setting probabilities
        example.setGridProbability(0,0,0.1);
        example.setGridProbability(3,1,0.7);
        example.setGridProbability(2,4,0.6);

        //Showing an error
        example.setGridProbability(6,4,0.5);
        System.out.println();

        //Showing best location method
        example.printBestLocation();
    }



}
