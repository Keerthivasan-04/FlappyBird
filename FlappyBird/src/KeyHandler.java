import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class KeyHandler implements KeyListener {
    FlappyBird flappyBird;
    int velocityY;

    // Constructor that accepts FlappyBird instances
    public KeyHandler(FlappyBird flappyBird) {
        this.flappyBird = flappyBird;
    }

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (flappyBird.gameOver) {
                flappyBird.resetGame();  // Reset game if it's over
            }

            flappyBird.jump(); // Trigger the jump in the game logic
        }
    }

    public void keyReleased(KeyEvent e) {}
}
