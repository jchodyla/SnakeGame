import javax.swing.JPanel;
import java.awt.*;
import java.util.Random;

public class Board extends JPanel{
    static final int WIDTH = 500;
    static final int HEIGHT = 500;
    static final int UNIT_SIZE = 20;
    static final int NUMBER_OF_UNITS = (WIDTH * HEIGHT) / UNIT_SIZE;

    Board() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.GRAY);
        this.setFocusable(true);
    }
}
