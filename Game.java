import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/* <applet code="Game.class" width="1000" height="800"> </applet> */
public class Game extends Applet implements Runnable, KeyListener {
    int score=0;
    int rocketX ;
    int rocketY ;
    boolean isPlay = false;
    private int x[] = new int[5];
    private int y[] = new int[5];
    private int xSpeed = -5;
    private int windowHeight;
    private int windowWidth;
    private Rectangle[] obstacleBounds; // Added
    Random rand;
    Image picture;
    Image pictureobs;
    Button button = new Button("START");
    Label label = new Label("SCORE : "+score);
    

    @Override
    public void init() {
        picture = getImage(getDocumentBase(), "spaceship.png");
        pictureobs = getImage(getDocumentBase(), "obs.png");

        windowHeight = getHeight();
        windowWidth = getWidth();

        rand = new Random();

        obstacleBounds = new Rectangle[5]; // Added
        for (int i = 0; i < obstacleBounds.length; i++) { // Added
            obstacleBounds[i] = new Rectangle(x[i], y[i], 50, 50); // Added
        } // Added

        x[0] = getWidth() - (int) (Math.random() * (100 - 50 + 1) + 50);
        x[1] = getWidth() - (int) (Math.random() * (300 - 350 + 1) + 350);
        x[2] = getWidth() - (int) (Math.random() * (500 - 450 + 1) + 450);
        x[3] = getWidth() - (int) (Math.random() * (200 - 250 + 1) + 250);
        x[4] = getWidth() - (int) (Math.random() * (600 - 550 + 1) + 550);

        y[0] = (int) (Math.random() * (600 - 525 + 1) + 525);
        y[1] = (int) (Math.random() * (425 - 375 + 1) + 375);
        y[2] = (int) (Math.random() * (175 - 125 + 1) + 125);
        y[3] = (int) (Math.random() * (350 - 225 + 1) + 225);
        y[4] = (int) (Math.random() * (75 - 0 + 1) + 0);

        addKeyListener(this);
        setFocusable(true); // Added
        setBackground(Color.BLACK);
        
        add(button);
        add(label);
        
        label.setBackground(Color.RED);
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isPlay = true; // Set isPlay to true when the button is clicked
                remove(button); // Remove the button after starting the game
                start(); // Start the game
                rocketX = 150;
                rocketY = 400;
                score=0;
            }
        });
    }

    @Override
    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        if (isPlay) {
            while (true) {
                moveObs();
                if (checkCollision()) {
                    // Handle collision as needed
                    isPlay = false;
                    System.out.println("Collision detected!");

                    break;
                } else if ((rocketY + 50 <= 0) || (rocketY >= windowHeight)) {
                    isPlay = false;
                    System.out.println("out side space");

                    break;
                }
                repaint(); // Redraw the applet
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rocketY += 10;
                score++;
                label.setText("SCORE : "+score);
            }
            repaint();
        }
        add(button);

    }

    private boolean checkCollision() {
        Rectangle rocketBounds = new Rectangle(rocketX, rocketY, 50, 50);
        for (int i = 0; i < obstacleBounds.length; i++) {
            if (rocketBounds.intersects(obstacleBounds[i])) {
                return true; // Collision detected
            }
        }

        return false; // No collision
    }

    private void moveObs() {
        for (int i = 0; i < x.length; i++) {
            x[i] += xSpeed;
            obstacleBounds[i].setBounds(x[i], y[i], 50, 50);

            if (x[i] <= 0) {
                x[i] = getWidth();
                y[i] = rand.nextInt(750);
                obstacleBounds[i].setBounds(x[i], y[i], 50, 50);
            }
        }
    }

    @Override
    public void paint(Graphics g) {

        if (isPlay) {
            g.setColor(Color.BLUE);

            for (int i = 0; i < x.length; i++) {
                g.drawImage(pictureobs, x[i], y[i], 50, 50, this);
            }

            g.setColor(Color.RED);
            g.drawImage(picture, rocketX - 50, rocketY - 25, 150, 100, this);

            g.setColor(Color.RED);
            g.fillRect(0, 1, windowWidth, 5);
            g.fillRect(0, windowHeight - 5, windowWidth, 5);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used in this example
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_UP) {
            rocketY -= 5;
        } else if (keyCode == KeyEvent.VK_DOWN) {
            rocketY += 5;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used in this example
    }
}
