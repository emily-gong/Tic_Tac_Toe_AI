
import java.awt.*;
import java.util.List;

public class AIPlayer {
    public static final int TWO_O_IN_A_LINE = 555;
    public static final int TWO_X_IN_A_LINE = -555;
    private int playerType; //PLAYER_O
    private Game game;

    /* This is where we will store the best next move we found
     * updated by calculateNextMove
     */
    private Point nextMove;

    public AIPlayer(int playerType) {
        this.playerType = playerType;
    }

    /* EFFECT:return coordinate of next move
     * NOTE: checkTwoInALine first checks to see if there are two's with a blank already, in which case
     * we can proceed right away, otherwise use minimax to calculate next move
     */
    public Point calculateNextMove(Game game) {
        this.game = game;
        if (!checkTwoInALine(playerType)) {
            minimax(2, Integer.MIN_VALUE, Integer.MAX_VALUE, playerType);
        }
        return nextMove;
    }

    /*check if there are 2 O's or 2 X's in a row already
     * return true if there are 2 O or 2 X in that order, update nextMove
     * return false if no two's, nextMove is not modified
     */
    private boolean checkTwoInALine(int playerType) {
        int[][] board = game.getGameBoard();
        int enemyType = (playerType == Game.PLAYER_X) ? Game.PLAYER_O:Game.PLAYER_X;

        int[] array1 = {0,1,2};
        Point myPoint = null;
        Point enemyPoint = null;
        int result;

        for (int i = 0; i < 3; i++) {
            int[] array2 = {i,i,i};
            //evaluate all cols
            result = getResultForOneLine(board, playerType, enemyType,array2,array1);
            if (result == TWO_O_IN_A_LINE) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == 0) {
                        myPoint = new Point(i,j);
                    }
                }
            } else if (result == TWO_X_IN_A_LINE) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == 0) {
                        enemyPoint = new Point(i,j);
                    }
                }
            }
            //evaluate all rows
            result = getResultForOneLine(board, playerType, enemyType, array1,array2);
            if (result == TWO_O_IN_A_LINE) {
                for (int j = 0; j < 3; j++) {
                    if (board[j][i] == 0) {
                        myPoint = new Point(j,i);
                    }
                }
            } else if (result == TWO_X_IN_A_LINE) {
                for (int j = 0; j < 3; j++) {
                    if (board[j][i] == 0) {
                        enemyPoint = new Point(j,i);
                    }
                }
            }
        }
        //evaluate diagonals
        // right-down diagonal
        result = getResultForOneLine(board, playerType, enemyType, array1, array1);
        if (result == TWO_O_IN_A_LINE) {
            for (int j = 0; j < 3; j++) {
                if (board[j][j] == 0) {
                    myPoint = new Point(j,j);
                }
            }
        } else if (result == TWO_X_IN_A_LINE) {
            for (int j = 0; j < 3; j++) {
                if (board[j][j] == 0) {
                    enemyPoint = new Point(j,j);
                }
            }
        }
        //left-up diagonal
        int[] array3 = {2,1,0};
        result = getResultForOneLine(board, playerType, enemyType, array1, array3);
        if (result == TWO_O_IN_A_LINE) {
            for (int j = 0; j < 3; j++) {
                if (board[2-j][j] == 0) {
                    myPoint = new Point(2-j,j);
                }
            }
        } else if (result == TWO_X_IN_A_LINE) {
            for (int j = 0; j < 3; j++) {
                if (board[2-j][j] == 0) {
                    enemyPoint = new Point(2-j,j);
                }
            }
        }

        if (myPoint != null){
            nextMove = myPoint;
            return true;
        } else if (enemyPoint != null) {
            nextMove = enemyPoint;
            return true;
        }
        return false;
    }



    /* minimax algorithm with alpha-beta pruning, will update field nextMove
     */
    private int minimax(int depth, int alpha, int beta, int playerType) {

        List<Point> blankCells = game.findEmptyCells();

        //if center point is empty, put piece on center
        if (blankCells.size() == 8 && game.placeMove(new Point(1,1),this.playerType)) {
            Point centerPoint = new Point(1,1);
            game.placeMove(centerPoint, 0);
            nextMove = centerPoint;
            return 0;
        }

        if (blankCells.isEmpty()) { // || depth == 0)
            return evaluate(game.getGameBoard(), Game.PLAYER_O);
        }

        for (Point point : blankCells) {
            if (playerType == Game.PLAYER_O) {//We want to maximize this
                game.placeMove(point, Game.PLAYER_O);
                int result = minimax(depth - 1, alpha, beta, Game.PLAYER_X);
                if (result > alpha) {
                    alpha = result;
                    nextMove = point;
                }
            } else { //minimize this
                game.placeMove(point, Game.PLAYER_X);
                int result = minimax(depth - 1, alpha, beta, Game.PLAYER_O);
                if (result < beta) {
                    beta = result;
                    nextMove = point;
                }
            }
            game.placeMove(point,0);
            if (beta <= alpha) {
                break;
            }
        }
        return (playerType == Game.PLAYER_O) ? alpha : beta;
    }

    //assuming board is full
    private int evaluate(int[][] board, int playerType) {
        int result = 0;
        int enemyType = (playerType == Game.PLAYER_X) ? Game.PLAYER_O:Game.PLAYER_X;
        int[] array1 = {0,1,2};
        for (int i = 0; i < 3; i++) {
            int[] array2 = {i,i,i};
            //evaluate all rows
            result += getResultForOneLine(board, playerType, enemyType,array2,array1);
            //evaluate all cols
            result += getResultForOneLine(board, playerType, enemyType, array1,array2);
        }
        //evaluate diagonals
        int[] array3 = {2,1,0};
        result += getResultForOneLine(board, playerType, enemyType, array1, array1);
        result += getResultForOneLine(board, playerType, enemyType, array1, array3);

        return result;
    }

    //xValues and yValues corresponds to the x and y coordinate of a point in the board,
    //  they are assumed to have array length of 3
    private int getResultForOneLine(int[][] board, int playerType, int enemyType, int[] xValues, int[] yValues) {
        int result = 0;
        int myCount = 0;
        int enemyCount = 0;
        int blankCount = 0;
        for (int i = 0; i < 3; i++) {
            if (board[xValues[i]][yValues[i]] == playerType)
                myCount++;
            else if (board[xValues[i]][yValues[i]] == enemyType)
                enemyCount++;
            else
                blankCount++;
        }
        if (board[1][1] == playerType) {
            result += 10;
        }
//        if (board[0][0] == playerType || board[2][0] == playerType || board[0][2] == playerType || board[2][2] == playerType) {
//            result += 5;
//        }
        if (myCount == 3) {
            result += 500;
        }
        if (myCount == 2) {
            //used in checkTwoInALine
            if (blankCount == 1) {
                return TWO_O_IN_A_LINE;
            }
            result += 20;
        }
        if (myCount == 1) {
            result += 5;
        }
        if (enemyCount == 3) {
            result += -1000;
        }
        if (enemyCount == 2) {
            //used in checkTwoInALine
            if (blankCount == 1) {
                return TWO_X_IN_A_LINE;
            }
            result += -20;
        }
        if (enemyCount == 1) {
            result += -5;
        }

        return result;
    }
}
