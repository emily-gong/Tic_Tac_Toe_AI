import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game {
    public static final int PLAYER_X = 1;
    public static final int PLAYER_O = 2;

    private int[][] gameBoard;

    private boolean isPlayerO;

    public Game() {
        gameBoard = new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        isPlayerO = true;
    }

    //EFFECTS: return a list of all empty cells in the game as points
    public List<Point> findEmptyCells() {
        List<Point> emptyCells = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (gameBoard[i][j] == 0) {
                    emptyCells.add(new Point(i,j));
                }
            }
        }
        return emptyCells;
    }

    public boolean checkXWon() {
        //check vertical and horizontal, rightDiagonal
        int rDiagonal = 0;
        for (int i = 0; i < 3; i++) {
            int verCount = 0;
            int horCount = 0;
            for (int j = 0; j < 3; j++) {
                if (gameBoard[i][j] == PLAYER_X)
                    verCount++;
                if (gameBoard[j][i] == PLAYER_X)
                    horCount++;
                if (i == j) {
                    if (gameBoard[i][j] == PLAYER_X)
                        rDiagonal++;
                }
            }
            if (verCount == 3 || horCount == 3)
                return true;
        }
        return rDiagonal == 3 || (gameBoard[1][1] == 1 && gameBoard[2][0] == 1 && gameBoard[0][2] == 1);
    }

    public boolean checkOWon() {
        int rDiagonal = 0;
        for (int i = 0; i < 3; i++) {
            int verCount = 0;
            int horCount = 0;
            for (int j = 0; j < 3; j++) {
                if (gameBoard[i][j] == 2)
                    verCount++;
                if (gameBoard[j][i] == 2)
                    horCount++;
                if (i == j && gameBoard[i][j] == PLAYER_O)
                    rDiagonal++;
            }
            if (verCount == 0 || horCount == 0)
                return true;
        }
        return rDiagonal == 3 || (gameBoard[1][1] == 2 && gameBoard[2][0] == 2 && gameBoard[0][2] == 2);
    }

    public boolean isGameOver() {
        return findEmptyCells().isEmpty();
    }

    public boolean isOTurn() {
        return isPlayerO;
    }

    public boolean placeMove(Point point, int playerType) {
        if (gameBoard[point.x][point.y] != 0) {
            System.out.println("Can't place here!");
            return false;
        }
        gameBoard[point.x][point.y] = playerType;
        isPlayerO = !isPlayerO;
        return true;
    }



}
