import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

public class Board extends JPanel {

    public static final int WIDTH = 700;
    public static final int HEIGHT = 600;

    private Game game;

    private static final int CELL_SIZE = 100;
    private static final int X_SHIFT = 60;
    private static final int Y_SHIFT = 100;

    //text boxes
    private JLabel title = new JLabel("Tic-Tac-Toe");

    //buttons
    private JButton playAgain = new JButton("Play Again");

    private Rectangle2D[][] cells = new Rectangle2D[3][3];


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //draw the grids
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < 3; i++) {
            g2.setStroke(new BasicStroke(7));
            cells[i][0] = new Rectangle2D.Float(X_SHIFT + i*CELL_SIZE, Y_SHIFT, CELL_SIZE, CELL_SIZE);
            g2.draw(cells[i][0]);
            cells[i][1] = new Rectangle2D.Float(X_SHIFT + i*CELL_SIZE, Y_SHIFT + CELL_SIZE, CELL_SIZE, CELL_SIZE);
            g2.draw(cells[i][1]);
            cells[i][2] = new Rectangle2D.Float(X_SHIFT + i*CELL_SIZE, Y_SHIFT + 2*CELL_SIZE, CELL_SIZE, CELL_SIZE);
            g2.draw(cells[i][2]);
        }

        initBoard();
    }

    private void initBoard() {
        setBackground(Color.CYAN);

        title.setSize(60,30);
        title.setFont(new Font("Serif", Font.BOLD, 12));
        add(title);

        add(playAgain);

        addMouseListener(new XOListener());

        playAgain.addActionListener(e -> {
            game = new Game();
            repaint();
        });

        game = new Game();
    }

    private class XOListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            System.out.println("X: "+ x + ", Y: " + y);

            Point point = new Point(x,y);
            if (game.isOTurn()){
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (cells[i][j].contains(point)) {
                            game.placeMove(new Point(i, j), Game.PLAYER_O);
                            break;
                        }
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    }
}
