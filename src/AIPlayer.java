import java.awt.*;
import java.util.List;

public class AIPlayer {
    private int playerType;
    private Game game;

    public Point nextMove;

    public AIPlayer(int playerType, Game game) {
        this.playerType = playerType;
        this.game = game;
    }

    //TODO
    //return coordinate of next move
    public Point calculateNextMove() {

        return new Point();
    }

    //TODO
    //if there are two of the same player type on one line, return the
    // third spot, else return null
    public Point findTwoInALine(int playerType) {
        return null;
    }

    //TODO
    //evaluate points of current board
    public void evaluate(){

    }

    private int alphaBeta(int depth, int alpha, int beta, int playerType) {
        if (game.checkXWon())
            return 20;
        if (game.checkOWon())
            return -20;

        List<Point> blankCells = game.findEmptyCells();

        if (blankCells.isEmpty())
            return 0;

        if (playerType == Game.PLAYER_X) {
            for (Point point : blankCells) {

                game.placeMove(point, Game.PLAYER_X);
                int result = alphaBeta(depth + 1, alpha, beta, Game.PLAYER_O);
                alpha = Integer.max(alpha, result);

                if (result >= 0 && depth == 0)
                    nextMove = point;

                if (beta <= alpha) {
                    game.placeMove(point, 0);
                    break;
                }

                if (point.equals(blankCells.get(blankCells.size()-1))
                        && alpha < 0 && depth == 0)
                    nextMove = point;

                game.placeMove(point, 0);
            }
            return alpha;
        } else {
            for (Point point : blankCells) {

                game.placeMove(point, Game.PLAYER_O);
                beta = Integer.min(beta, alphaBeta(depth + 1, alpha, beta, Game.PLAYER_X));

                if (beta <= alpha) {
                    game.placeMove(point, 0);
                    break;
                }

                game.placeMove(point, 0);
            }
            return beta;
        }
    }
}
