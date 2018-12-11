import com.sun.deploy.util.ArrayUtil;

import java.awt.*;
import java.util.List;

public class AIPlayer {
    private int playerType; //PLAYER_O
    private Game game;

    //This is where we will store the best next move we found
    public Point nextMove;

    public AIPlayer(int playerType) {
        this.playerType = playerType;
    }

    //return coordinate of next move
    public Point calculateNextMove(Game game) {
        this.game = game;
        minimax(2, Integer.MIN_VALUE, Integer.MAX_VALUE, playerType);
        return nextMove;
    }




    private int minimax(int depth, int alpha, int beta, int playerType) {

        List<Point> blankCells = game.findEmptyCells();

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
//            if (beta <= alpha) {
//                break;
//            }
        }
        return (playerType == Game.PLAYER_O) ? alpha : beta;
    }

    //assuming board is full
    private int evaluate(int[][] board, int playerType) {
        int result = 0;
        int[] array1 = {0,1,2};
        for (int i = 0; i < 3; i++) {
            int[] array2 = {i,i,i};
            //evaluate all rows
            result += getResultForOneLine(board, playerType,array2,array1);
            //evaluate all cols
            result += getResultForOneLine(board, playerType, array1,array2);
        }
        //evaluate diagonals
        int[] array3 = {2,1,0};
        result += getResultForOneLine(board, playerType, array1, array1);
        result += getResultForOneLine(board, playerType, array1, array3);

        return result;
    }

    //xValues and yValues corresponds to the x and y coordinate of a point in the board,
    //  they are expected to have array length of 3
    private int getResultForOneLine(int[][] board, int playerType, int[] xValues, int[] yValues) {
        int result = 0;
        int myCount = 0;
        int enemyCount = 0;
        for (int i = 0; i < 3; i++) {
            if (board[xValues[i]][yValues[i]] == playerType)
                myCount++;
            else
                enemyCount++;
        }

        if (myCount == 3) {
            result += 1000;
        } else if (myCount == 2) {
            result += 20;
        } else if (myCount == 1) {
            result += 5;
        } else if (enemyCount == 3) {
            result += -1000;
        } else if (enemyCount == 2) {
            result += -20;
        } else if (enemyCount == 1) {
            result += -5;
        }

        return result;
    }


}
