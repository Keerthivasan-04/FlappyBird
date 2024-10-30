import java.awt.Image;

public class Pipe {
    int x;
    int y;
    int width;
    int height;
    Image image;
    boolean passed = false;
    

    public Pipe(Image image, int x , int y, int width, int height){
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void move(int velocityX){
        this.x += velocityX;
    }

}