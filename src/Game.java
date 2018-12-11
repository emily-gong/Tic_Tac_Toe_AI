import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game {
    public static final int PLAYER_X = 1;
    public static final int PLAYER_O = 2;

    //x,y gameBoard -- 3 long rectangles of height 3
    private int[][] gameBoard;

    protected int currentPlayer;

    public Game() {
        gameBoard = new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        currentPlayer = 1;
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
        return checkPlayerWon(PLAYER_X);
    }

    public boolean checkOWon() {
        return checkPlayerWon(PLAYER_O);
    }

    private boolean checkPlayerWon(int playerType) {
        int rDiagonal = 0;
        for (int i = 0; i < 3; i++) {
            int verCount = 0;
            int horCount = 0;
            for (int j = 0; j < 3; j++) {
                if (gameBoard[i][j] == playerType)
                    verCount++;
                if (gameBoard[j][i] == playerType)
                    horCount++;
                if (i == j && gameBoard[i][j] == playerType)
                    rDiagonal++;
            }
            if (verCount == 3 || horCount == 3)
                return true;
        }
        return rDiagonal == 3 ||
                (gameBoard[1][1] == playerType && gameBoard[2][0] == playerType && gameBoard[0][2] == playerType);
    }

    public boolean isGameOver() {
        return findEmptyCells().isEmpty();
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }

    public boolean placeMove(Point point, int playerType) {
        if (gameBoard[point.x][point.y] != 0 && playerType != 0) {
            System.out.println("Can't place here!");
            return false;
        }
        gameBoard[point.x][point.y] = playerType;
        return true;
    }


    public void changePlayer() {
        currentPlayer = (currentPlayer == PLAYER_X)? PLAYER_O : PLAYER_X;
    }
}
