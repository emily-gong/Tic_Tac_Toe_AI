import javax.swing.*;
import java.awt.*;

public class Application extends JFrame {

    public Application () {
        initUI();
    }

    private void initUI() {
        add(new Board());

        setSize(Board.WIDTH, Board.HEIGHT);

        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Application exe = new Application();
            exe.setVisible(true);
        });
    }
}
