import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class Board extends JPanel implements ActionListener {
    static final int DELAY = 100;
    static final int WIDTH = 700;
    static final int HEIGHT = 700;
    static final int UNIT_SIZE = 20;
    static final int NUMBER_OF_UNITS = (WIDTH * HEIGHT) / UNIT_SIZE;
    final int[] x = new int[NUMBER_OF_UNITS];
    final int[] y = new int[NUMBER_OF_UNITS];
    int snakeLength = 5;
    int applesEaten = 0;
    int appleX;
    int appleY;
    public char direction = 'D';
    boolean isRunning = false;
    Timer timer;
    Random random;

    Board() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.GRAY);
        this.setFocusable(true);
        this.addKeyListener(new KeyboardInput());
        startGame();
    }

    public void startGame() {
        timer = new Timer(DELAY, this);
        isRunning = true;
        createApple();
        timer.start();
    }

    public void createApple() {
        appleX = random.nextInt((WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);


        for (int i = 1; i < snakeLength; i++) {
            g.setColor(Color.green);
            g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void move() {
        for (int i = snakeLength; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }

    public void checkCollision() {
        if (x[0] < 0 || x[0] > WIDTH)
            isRunning = false;

        if (y[0] < 0 || y[0] > HEIGHT)
            isRunning = false;

        for (int i = 1; i < snakeLength; i++) {
            if (x[0] == x[i] && y[0] == y[i]) {
                isRunning = false;
                break;
            }
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            snakeLength++;
            applesEaten++;
            createApple();
        }
    }

    public class KeyboardInput extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> {
                    if (direction != 'R') {
                        direction = 'L';
                    }
                }
                case KeyEvent.VK_RIGHT -> {
                    if (direction != 'L') {
                        direction = 'R';
                    }
                }
                case KeyEvent.VK_UP -> {
                    if (direction != 'D') {
                        direction = 'U';
                    }
                }
                case KeyEvent.VK_DOWN -> {
                    if (direction != 'U') {
                        direction = 'D';
                    }
                }
            }
        }

    }
}
