import javax.swing.*;

public class Frame extends JFrame {
    Frame() {
        Board board = new Board();
        this.add(board);
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
