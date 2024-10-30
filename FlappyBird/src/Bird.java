import java.awt.Image;
public class Bird {
    int x;
    int y;
    int width;
    int height;
    Image image;
    

    Bird(Image image, int x, int y, int width, int height){
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
