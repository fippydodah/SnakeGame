import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.Random;
import java.util.Scanner;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    int highScore = 0;
    int loopint = 0;
    int loopint1 = 0;
    File file;
    String score;
    int scoreFIN;
    BufferedImage bgImage;

    GamePanel() {

        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }

    public void startGame() {
        try {
            File file = new File("HighScore.txt");
            Scanner inputFile = new Scanner(file);
            score = inputFile.nextLine();
            scoreFIN = Integer.parseInt(score);
            inputFile.close();
            bgImage = ImageIO.read(new File("leaf.png"));
        } catch (Exception e) {
            // TODO: handle exception
        }

        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g) {
        if (running) {
            g.drawImage(bgImage, -4, -1, null);
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (loopint = 0; loopint < bodyParts; loopint++) {

                if (loopint == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[loopint], y[loopint], UNIT_SIZE, UNIT_SIZE);
                } else {
                    int red = new Random().nextInt(50, 200);
                    int green = new Random().nextInt(50, 200);
                    int blue = new Random().nextInt(50, 200);
                    g.setColor(new Color(red, green, blue));
                    g.fillRect(x[loopint], y[loopint], UNIT_SIZE, UNIT_SIZE);
                }
            }

            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics metricsz = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metricsz.stringWidth("Score: " + applesEaten)) / 2,
                    g.getFont().getSize());
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.ITALIC, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("HighScore: " + score, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                    g.getFont().getSize() + 500);

            // In your rendering method

        } else {

            gameOver(g);
            // Opening HighScore.txt file and changing high score if higher.
            try {
                if (applesEaten > scoreFIN) {
                    String fileName = "HighScore.txt";
                    PrintWriter highScoreFile = new PrintWriter(fileName);
                    highScoreFile.print(Integer.toString(applesEaten));
                    highScoreFile.close();
                }

            } catch (Exception e) {
                System.out.println("High Score File was not found");
            }

        }

    }

    // ***********************************************************************************
    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    // ------------------------------------------------------------------------------------
    public void move() {
        for (int loopint1 = bodyParts; loopint1 > 0; loopint1--) {
            x[loopint1] = x[loopint1 - 1];
            y[loopint1] = y[loopint1 - 1];

        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;

            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;

        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();

        }
    }

    public void checkCollisons() {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
            // check if head touches left border
            if (x[0] < 0) {
                running = false;
            }
            // check if head touches right border if(×[0] > SCREEN_WIDTH) {
            if (x[0] > SCREEN_WIDTH) {
                running = false;
            }
            // check if head touches top border if(y[0] ‹ 0) {
            if (y[0] < 0) {
                running = false;
            }

            // check if head touches bottom border if(y[0] ‹0) K
            if (y[0] > SCREEN_HEIGHT) {
                running = false;
            }
            if (!running) {
                // timer.stop();
            }
        }
    }

    public void gameOver(Graphics g) {

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER")) / 2, SCREEN_HEIGHT / 2);

        // display score at end of game.
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2,
                g.getFont().getSize());

        // High Score display text.
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("High Score: " + score, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten)) / 2,
                g.getFont().getSize() + 100);

        // Restart prompt
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.ITALIC, 20));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Press R to Restart ", (SCREEN_WIDTH - metrics3.stringWidth("Score: " + applesEaten)) / 2,
                g.getFont().getSize() + 300);

    }

    public void restart() {
        // change direction back to right.
        direction = 'R';
        // reset the body and apples eaten back to starting size.
        bodyParts = 6;
        applesEaten = 0;
        // start running the game again.
        running = true;
        // Reset the position of the snake (just the head the body will follow).
        x[0] = 0;
        y[0] = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        if (running) {
            move();
            checkApple();
            checkCollisons();

        }

    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_D:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_W:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_S:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
            if (running == false) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    restart();
                }
            }

        }

    }
}
