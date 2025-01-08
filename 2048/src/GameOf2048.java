import java.util.Random;
import java.util.Scanner;

public class GameOf2048 {

    public static final char MOVE_LEFT = 'A';
    public static final char MOVE_RIGHT = 'D';
    public static final char MOVE_UP = 'W';
    public static final char MOVE_DOWN = 'S';

    private int board[][];
    private Random random;
    private Scanner scanner;

    public GameOf2048() {
        // all locations of this 2D array is 0
        board = new int[4][4];

        // create a random object
        random = new Random();

        // create the Scanner object to read from the user
        scanner = new Scanner(System.in);
    }

    public void showBoard() {
        // print top separator
        for( int i=0; i<4; i++ ) {
            System.out.print("-------");
        }
        System.out.println();

        // print each row
        for( int i=0; i<4; i++ ) {
            // print blank space before the row
            System.out.print("|");
            for( int j=0; j<4; j++ ) {
                System.out.print("      |");
            }
            System.out.println();

            // print first pipe symbol
            System.out.print("|");
            for( int j=0; j<4; j++ ) {
                if( board[i][j]==0 ) {
                    System.out.printf("  %-3s |", "");
                }
                else {
                    System.out.printf("  %-3s |", "" + board[i][j]);
                }
            }
            System.out.println();

            // print blank space after the row
            System.out.print("|");
            for( int j=0; j<4; j++ ) {
                System.out.print("      |");
            }
            System.out.println();

            // print bottom separator
            for( int j=0; j<4; j++ ) {
                System.out.print("-------");
            }
            System.out.println();
        }
    }

    public void addRandomNumber(int digit) {

        // generate a pair of coordinates
        int i = random.nextInt(4);
        int j = random.nextInt(4);

        // generate coordinates as long as location is on the board
        while( board[i][j] != 0 ) {
            i = random.nextInt(4);;
            j = random.nextInt(4);
        }

        // set the digit at this location
        board[i][j] = digit;
    }

    public boolean searchOnBoard(int x) {
        for( int i=0; i<4; i++ ) {
            for( int j=0; j<4; j++ ) {
                if( board[i][j] == x ) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean gameWon() {
        return searchOnBoard(2048);
    }

    public boolean userCanMakeAMove() {
        // check 3x3 board
        for( int i=0; i<3; i++ ) {
            for( int j=0; j<3; j++ ){
                // if two adjacent locations have equal value, return true
                if( board[i][j] == board[i][j+1] ||
                        board[i][j] == board[i+1][j]
                ) {
                    return true;
                }
            }
        }
        // check if two equal adjacent values in the last row
        for( int j=0; j<3; j++ ) {
            if( board[3][j] == board[3][j+1] ) {
                return true;
            }
        }

        // check if two equal adjacent values in the last column
        for( int i=0; i<3; i++ ) {
            if( board[i][3] == board[i+1][3] ) {
                return true;
            }
        }

        return false;
    }

    public boolean isGameOver() {
        // game is over if there is a 2048 on the board
        if( gameWon() ) {
            return true;
        }

        // game is not over if there is a blank tile on the board
        if( searchOnBoard(0) ) {
            return false;
        }

        // game is over if user can make a move
        return userCanMakeAMove();
    }

    public char getUserMove() {
        // show all possible moves
        System.out.println("Choose a move: ");
        System.out.println("W/w: Up");
        System.out.println("S/s: Down");
        System.out.println("A/a: Left");
        System.out.println("D/d: Right");
        System.out.print("Enter move: ");

        // read the move from the user
        String moveInput = scanner.nextLine();
        if( moveInput.equalsIgnoreCase("a") ||
                moveInput.equalsIgnoreCase("w") ||
                moveInput.equalsIgnoreCase("s") ||
                moveInput.equalsIgnoreCase("d")
        ) {
            return moveInput.toUpperCase().charAt(0);
        }

        // if input is invalid
        System.out.println("Invalid Option!");
        System.out.println();

        // show board
        showBoard();

        return getUserMove();
    }

    public int[] processLeftMove(int row[]) {
        // copy non 0 values
        int newRow[] = new int[4];
        int j = 0;
        for( int i=0; i<4; i++ ) {
            if( row[i]!=0 ) {
                newRow[j++] = row[i];
            }
        }

        // merge values in new row
        for( int i=0; i<3; i++ ) {
            if( newRow[i]!=0 && newRow[i]==newRow[i+1]) {
                newRow[i] = 2*newRow[i];
                // copy the remaining values
                for( j=i+1; j<3; j++ ) {
                    newRow[j] = newRow[j+1];
                }
                // set the last location of this row to 0
                newRow[3] = 0;
            }
        }
        return newRow;
    }

    public int[] reverseArray(int arr[]) {
        int[] reverseArr = new int[arr.length];
        for( int i=arr.length-1; i>=0; i-- ) {
            reverseArr[i] = arr[arr.length - i - 1];
        }
        return reverseArr;
    }

    public int[] processRightMove(int row[]) {
        // copy all the non 0 values
        int newRow[] = new int[4];
        int j = 0;
        for( int i=0; i<4; i++ ) {
            if( row[i]!=0 ) {
                newRow[j++] = row[i];
            }
        }

        // reverse row
        newRow = reverseArray(newRow);

        // process left move
        newRow = processLeftMove(newRow);

        // return reversed row
        return reverseArray(newRow);
    }

    public void processMove(char move) {
        switch(move) {
            case MOVE_LEFT:
            {
                // for each row
                for( int i=0; i<4; i++ ){
                    // get  new row
                    int newRow[] = processLeftMove(board[i]);
                    // copy values from new row to row
                    for( int j=0; j<4; j++ ) {
                        board[i][j] = newRow[j];
                    }
                }
            }
            break;
            case MOVE_RIGHT:
            {
                // for each row
                for( int i=0; i<4; i++ ){
                    // get new row
                    int newRow[] = processRightMove(board[i]);
                    // copy values from new row to row
                    for( int j=0; j<4; j++ ) {
                        board[i][j] = newRow[j];
                    }
                }
            }
            break;
            case MOVE_UP:
            {
                // for each column
                for( int j=0; j<4; j++ ) {
                    // create a row from column values
                    int row[] = new int[4];
                    for( int i=0; i<4; i++ ) {
                        row[i] = board[i][j];
                    }

                    // process left move on this row
                    int newRow[] = processLeftMove(row);

                    // copy values back to column
                    for( int i=0; i<4; i++ ) {
                        board[i][j] = newRow[i];
                    }
                }
            }
            break;
            case MOVE_DOWN:
            {
                // for each column
                for( int j=0; j<4; j++ ) {
                    // create a row from column values
                    int row[] = new int[4];
                    for( int i=0; i<4; i++ ) {
                        row[i] = board[i][j];
                    }

                    // process right move on this row
                    int newRow[] = processRightMove(row);

                    // copy values to the column
                    for( int i=0; i<4; i++ ) {
                        board[i][j] = newRow[i];
                    }
                }
            }
            break;
        }
    }

    public int getHighestValue() {
        int highestValue = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] > highestValue) {
                    highestValue = board[i][j];
                }
            }
        }
        return highestValue;
    }

    public void play() {

        //set up the board - set random locations for 2 values
        addRandomNumber(2);
        addRandomNumber(4);

        while( !isGameOver() ) {
            showBoard();

            // ask user to make a move
            char move = getUserMove();

            // process move
            processMove(move);

            // add random 2 or 4
            int r = random.nextInt(100);
            if( r%2==0 ) {
                addRandomNumber(2);
            }
            else {
                addRandomNumber(4);
            }
        }

        if( gameWon() ) {
            System.out.println("You Won!");
        }
        else {
            System.out.println("You Lost!");
            System.out.println("Highest Score Achieved: " + getHighestValue());
        }
    }

    public static void main(String args[]) {
        GameOf2048 gameOf2048 = new GameOf2048();
        gameOf2048.play();
    }
}
