import java.util.ArrayList;
import java.util.LinkedList;

public class Connect4 {
    private ArrayList<LinkedList<Integer>> board;
    // ‘1’ for player 1, ‘2’ for player 2
    // let index 0 of each LinkedList represent the bottom of the stack of pieces
    private int columns; // the number of columns in the board, also equal to board.size()
    private int rows; // the number of rows in the board, and the max size of any LinkedList in the board
    private final int numToWin = 4;

    // initializes the variables and connect4 board with the proper number of linked lists
    public Connect4(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        this.board = new ArrayList<>();
        // add linked lists to the board to represent the columns
        for (int i = 0; i < columns; i++) {
            board.add(new LinkedList<>());
        }
    }

    /* should insert the piece into the board, if it is possible
    return true if the move is made
    return false if the move is not possible
    assume that player is either 1 or 2, but do NOT assume column is valid
    */
    public boolean move(int column, int player) {
        if (column > this.columns){
            return false;
        }

        if (board.get(column).size() > rows){
            return false;
        }

        board.get(column).add(player);
        return true;

    }

    /* should check if a player has won the game
    if player 1 wins, return 1
    if player 2 wins, return 2
    if no player wins, return 0
    assume that only one player will win at a time
    (this is a very difficult method - do not forget to check for horizontal, vertical,
    and diagonal victories and watch out for index exceptions!)
    */
    public int checkForWinner() {
        // fill this in

        if (verticalWinner() != 0) return verticalWinner();
        if (horizontalWinner() != 0) return horizontalWinner();

        return 0;


    }

    public int verticalWinner(){
        for (int column = 0; column < columns; column ++){
            LinkedList<Integer> vertical = board.get(column);
            for (int startingRow = 0; startingRow <= vertical.size() - numToWin; startingRow++ ) {
                int size = 0;
                int playerToCheck = vertical.get(startingRow);
                int row = startingRow;
                while (vertical.get(row) == playerToCheck) {
                    size += 1;
                    row += 1;

                    if (size >= 4) {
                        return playerToCheck;
                    }
                }
            }


        }
        return 0;
    }

    public int horizontalWinner(){
        for (int row = 0; row < rows; row ++){
            for (int startingColumn = 0; startingColumn <= columns - numToWin; startingColumn++){
                int size = 0;
                int playerToCheck = board.get(startingColumn).get(row);
                int column = startingColumn;
                while (board.get(column).get(row) == playerToCheck){
                    size += 1;
                    column += 1;

                    if (size == 4){
                        return playerToCheck;
                    }
                }

            }
        }
        return 0;

    }

//    public int diagonalWinner(){
//        //bottom right
//
//
//    }


    /* should print out the board in a grid formation
    with blank spaces filled in by 0s and the linked lists running from the bottom up
    */
    public void printBoard() {
        // fill this in
        for (int i = rows - 1; i >= 0; i--){
            for (int j = 0; j < columns; j++){
                if (board.get(j).size() > i){
                    System.out.print(board.get(j).get(i) + " ");
                } else{
                    System.out.print(0 + " ");
                }
            }
            System.out.println(" ");
        }
    }

    public static void main(String[] args) {
        Connect4 game = new Connect4(5,5);
        game.move(0,1);
        game.move(0,1);
        game.move(0,1);
        game.move(0,1);
        game.move(1,1);
        game.move(2,1);
        game.move(3,2);
        game.move(4,2);

        game.printBoard();

        System.out.println(game.checkForWinner());

    }
}
