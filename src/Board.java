import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class Board extends JPanel implements ActionListener {
    static final int DELAY = 210;
    static final int WIDTH = 700;
    static final int HEIGHT = 700;
    static final int UNIT_SIZE = 35;
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
    Music startSound = new Music();
    Music beepSound = new Music();
    Music endSound = new Music();
    JLabel scoreLabel = new JLabel("Score: " + applesEaten);
    JLabel startLabel = new JLabel("Snake Game");
    JButton startButton = new JButton("Play");
    boolean gameStarted = false;
    JLabel gameOverLabel = new JLabel("Game Over");
    JButton restartButton = new JButton("Play again");

    Board() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.WHITE);
        this.setFocusable(true);
        this.addKeyListener(new KeyboardInput());
        this.setLayout(null);
        gameStartScreen();
    }

    public void startGame() {
        this.setBackground(Color.BLACK);
        timer = new Timer(DELAY, this);
        isRunning = true;
        createApple();
        timer.start();
        startSound.setFile("game_start.wav");
        startSound.play();
        this.remove(startLabel);
        this.remove(startButton);
        scoreLabel.setBounds(310,5,400,20);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        scoreLabel.setForeground(Color.WHITE);
        this.add(scoreLabel);
        gameStarted = true;
    }

    public void restart() {
        this.setBackground(Color.BLACK);
        timer.start();
        createApple();
        isRunning = true;
        gameStarted = true;
        this.remove(restartButton);
        this.remove(gameOverLabel);
        snakeLength = 5;
        for (int i = 0; i < NUMBER_OF_UNITS; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        direction = 'D';
        applesEaten = 0;
        scoreLabel.setText("Score: " + applesEaten);
    }

    public void createApple() {
        appleX = random.nextInt((WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

        for (int i = 0; i < snakeLength; i++) {
            if (appleX == x[i] && appleY == y[i]) {
                createApple();
            }
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

        for (int i = 1; i < snakeLength; i++) {
            g.setColor(Color.GREEN);
            g.fillRect(x[i], y[i], UNIT_SIZE - 1, UNIT_SIZE - 1);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameStarted)
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
        } else {
            endSound.play();
            gameOverScreen();
        }
        repaint();
    }

    public void checkCollision() {
        endSound.setFile("game_over.wav");

        if (x[0] < 0 || x[0] >= WIDTH)
            isRunning = false;

        if (y[0] < 0 || y[0] >= HEIGHT)
            isRunning = false;

        for (int i = 1; i < snakeLength; i++) {
            if (x[0] == x[i] && y[0] == y[i]) {
                isRunning = false;
                break;
            }
        }
    }

    public void checkApple() {
        beepSound.setFile("beep.wav");
        if (x[0] == appleX && y[0] == appleY) {
            beepSound.play();
            snakeLength++;
            applesEaten++;
            createApple();
            scoreLabel.setText("Score: " + applesEaten);
        }
        if (applesEaten == NUMBER_OF_UNITS - 5) {
            isRunning = false;
        }
    }

    public class KeyboardInput extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT, KeyEvent.VK_A -> {
                    if (direction != 'R') {
                        direction = 'L';
                    }
                }
                case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
                    if (direction != 'L') {
                        direction = 'R';
                    }
                }
                case KeyEvent.VK_UP, KeyEvent.VK_W -> {
                    if (direction != 'D') {
                        direction = 'U';
                    }
                }
                case KeyEvent.VK_DOWN, KeyEvent.VK_S -> {
                    if (direction != 'U') {
                        direction = 'D';
                    }
                }
            }
        }
    }

    public void gameStartScreen() {
        startLabel.setBounds(200,100,400,100);
        startButton.setBounds(250,300,200,100);
        startLabel.setFont(new Font("Arial", Font.PLAIN, 50));
        startButton.setFont(new Font("Arial", Font.PLAIN, 30));
        this.add(startLabel);
        this.add(startButton);
        startButton.addActionListener(f -> startGame());
    }

    public void gameOverScreen() {
        gameOverLabel.setBounds(220,100,400,100);
        restartButton.setBounds(250,300,200,100);
        gameOverLabel.setFont(new Font("Arial", Font.PLAIN, 50));
        gameOverLabel.setForeground(Color.WHITE);
        restartButton.setFont(new Font("Arial", Font.PLAIN, 30));
        this.add(gameOverLabel);
        this.add(restartButton);
        restartButton.addActionListener(g -> restart());
    }
}
