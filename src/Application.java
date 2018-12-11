import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

public class Application extends JFrame {

    private static final int CELL_SIZE = 100;
    private static final int X_SHIFT = 40;
    private static final int Y_SHIFT = 40;
    private static final int PIECE_SPACE = 80;
    private static final int PIECE_WIDTH = 9;
    private static final int BOARD_WIDTH = 7;

    private static final int WIDTH = CELL_SIZE*3 + 2*X_SHIFT + 4*BOARD_WIDTH;
    private static final int HEIGHT = CELL_SIZE*3 + 2*Y_SHIFT + 4*BOARD_WIDTH + 100;

    private JLabel message = new JLabel("Display message here... ");
    private JButton playAgain = new JButton("Play Again");

    //the current game states
    private Game game;
    private Rectangle2D[][] cells = new Rectangle2D[3][3];

    //0 for PLAYING, 1 for X_WON, 2 for O_WON, 3 for DRAW
    protected int gameStatus;

    protected AIPlayer ai;

    private Application() {
        //Displaying messages about current game status
        message.setFont(new Font (Font.DIALOG_INPUT,Font.BOLD,25));
        message.setBorder(BorderFactory.createEmptyBorder(2,5,4,5));

        playAgain.setPreferredSize(new Dimension(100,40));
        playAgain.addActionListener(e -> {
            game = new Game();
            gameStatus = 0;
            repaint();
            message.setText("Starting a New Game");
        });

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        Board board = new Board();
        board.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                //Not within board
                if (mouseX < X_SHIFT || mouseY < Y_SHIFT) return;

                int x = (mouseX - X_SHIFT) / CELL_SIZE;
                int y = (mouseY - Y_SHIFT) / CELL_SIZE;
                System.out.println("X: " + x + ", Y: " + y);

                if (x < 3 && y < 3 && game.currentPlayer == Game.PLAYER_X && gameStatus == 0) {
                    if (game.placeMove(new Point(x, y), Game.PLAYER_X)) {
                        game.changePlayer();
                        //now is AiLin's turn
                        message.setText("AiLin is thinking...");
                        Point next = ai.calculateNextMove(game);
                        game.placeMove(next, Game.PLAYER_O);
                        game.changePlayer();
                    }
                }
                if (game.checkXWon()) {
                    gameStatus = 1;
                } else if (game.checkOWon()) {
                    gameStatus = 2;
                } else if (game.isGameOver()) {
                    gameStatus = 3;
                }
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        container.add(board,BorderLayout.CENTER);
        container.add(message,BorderLayout.PAGE_START);
        container.add(playAgain,BorderLayout.PAGE_END);

        setSize(WIDTH, HEIGHT);

        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        ai = new AIPlayer(Game.PLAYER_O);
        game = new Game();
        gameStatus = 0;


    }


    /*
    * Inner class Board
    */
    class Board extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.PINK);

            //draw the grids
            Graphics2D g2 = (Graphics2D) g;
            for (int i = 0; i < 3; i++) {
                g2.setStroke(new BasicStroke(BOARD_WIDTH));
                cells[i][0] = new Rectangle2D.Float(X_SHIFT + i*CELL_SIZE, Y_SHIFT, CELL_SIZE, CELL_SIZE);
                g2.draw(cells[i][0]);
                cells[i][1] = new Rectangle2D.Float(X_SHIFT + i*CELL_SIZE, Y_SHIFT + CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g2.draw(cells[i][1]);
                cells[i][2] = new Rectangle2D.Float(X_SHIFT + i*CELL_SIZE, Y_SHIFT + 2*CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g2.draw(cells[i][2]);
            }

            //draw the pieces on the board
            int[][] board = game.getGameBoard();
            g2.setStroke(new BasicStroke(PIECE_WIDTH, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    int x1 = c * CELL_SIZE + X_SHIFT + 10;
                    int y1 = r * CELL_SIZE + Y_SHIFT + 10;
                    if (board[c][r] == Game.PLAYER_X) {
                        int x2 = x1 + PIECE_SPACE;
                        int y2 = y1 + PIECE_SPACE;
                        g2.drawLine(x1,y1,x2,y2);
                        g2.drawLine(x2,y1,x1,y2);
                    } else if (board[c][r] == Game.PLAYER_O){
                        g2.drawOval(x1,y1,PIECE_SPACE,PIECE_SPACE);
                    }
                }
            }

            //Print message about status
            if (gameStatus == 0) {//playing
                if (game.currentPlayer == Game.PLAYER_X) {
                    message.setText("Your Turn");
                } else {
                    message.setText("AiLin's Turn");
                }
            } else if (gameStatus == 1) {//X Won
                message.setText("Congratulations! You Won!(ﾉ◕ヮ◕)ﾉ*:･ﾟ✧");
            } else if (gameStatus == 2) {//O Won
                message.setText("AiLin won!");
            } else {//draw
                message.setText("It's a draw!");
            }
        }

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Application::new);
    }
}
