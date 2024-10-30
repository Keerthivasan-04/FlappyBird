import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;


public class FlappyBird extends JPanel implements ActionListener{
    int boardWidth = 360;
    int boardHeight = 640;

    // Images
    Image backgroundImage;
    Image birdImage;
    Image topPipeImage;
    Image bottomPipeImage;

    // Bird
    int birdX = boardWidth / 8;
    int birdY = boardHeight / 2;
    int birdWidth = 34;
    int birdHeight = 24;

    // Pipe
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64; //scaled by 1/6 actual size is 384 x 3072
    int pipeHeight = 512;



    // Game logic and bird object
    Bird bird;
    Timer gameLoop;  // FPS Timer
    Timer placePipesTimer;
    boolean gameOver = false;
    double score = 0;

    int velocityX = -4; // move pipes to the left
    int velocityY = 0; // move bird up / down
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();


    FlappyBird(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);

        // Load Images
        backgroundImage = new ImageIcon("D:\\Projects\\FlappyBird\\src\\flappybirdbg.png").getImage(); 
        birdImage = new ImageIcon("D:\\Projects\\FlappyBird\\src\\flappybird.png").getImage();
        topPipeImage = new ImageIcon("D:\\Projects\\FlappyBird\\src\\toppipe.png").getImage();
        bottomPipeImage = new ImageIcon("D:\\Projects\\FlappyBird\\src\\bottompipe.png").getImage();

        // Create Bird object with the loaded bird image
        bird = new Bird(birdImage,birdX,birdY,birdWidth,birdHeight); 
        pipes = new ArrayList<Pipe>();

        // Placce pipes timer
        placePipesTimer = new Timer(1500,new ActionListener() { // 15000 is 1.5 sec
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });
        placePipesTimer.start();

        // Add KeyHandler for key events
        KeyHandler keyHandler = new KeyHandler(this);  // this refers to actionPerformed 
        addKeyListener(keyHandler);


        // Game timer
        gameLoop = new Timer(1000 / 60, this); // Runs at 60 FPS => this refers to action performed funtion which is below.
        gameLoop.start(); // Used to continously run the game.
    }

    public void placePipes(){
        // (0 -1) * pipeHeight/2 --> (0 - 256)
        // 128
        // 0 - 128 - (0 -> 256) --> 1/4 pipeheight : 3/4 pipeHeight

        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight / 4; 

        
        Pipe topPipe = new Pipe(topPipeImage, pipeX, randomPipeY, pipeWidth, pipeHeight);
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImage, pipeX, topPipe.y + pipeHeight + openingSpace, pipeWidth, pipeHeight);
        pipes.add(bottomPipe);

    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }


    public void draw(Graphics g){ 
        // Draw Background
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);

        // Draw Bird
        g.drawImage(bird.image, bird.x, bird.y, bird.width, bird.height, null);

        // Draw Pipes
        for (Pipe pipe : pipes) {
            g.drawImage(pipe.image, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        // Score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameOver){
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        }
        else{
            g.drawString(String.valueOf((int) score), 10, 35);
        }

    }

    public void move() {
        // Bird
        velocityY += gravity;  // Gravity pulls the bird down
        bird.y += velocityY;    // Update bird's Y position
        bird.y = Math.max(bird.y, 0);  // Prevent the bird from going off the top of the screen

        if(bird.y > boardHeight){
            gameOver = true;
        }

        // Pipes
        for (Pipe pipe : pipes) {
            pipe.move(velocityX);

            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score += 0.5; // 1 point for each setof pipes
            }

            if(collison(bird, pipe)){
                gameOver = true;
            }
        }
    }
   
    public boolean collison(Bird a, Pipe b) {
        return  a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
                a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
                a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
                a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner
    }

    public void actionPerformed(ActionEvent e) {
        move();     // Update game logic
        repaint();  // Repaint the game screen

        if(gameOver){
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }

    public void jump() {
        velocityY = -8;
    }


    public void resetGame() {
        // Reset bird position and velocity
        bird.y = birdY;
        velocityY = 0;
    
        // Clear pipes and reset score
        pipes.clear();
        score = 0;
    
        // Reset game status
        gameOver = false;
    
        // Restart timers
        placePipesTimer.start();
        gameLoop.start();
    }
    
}