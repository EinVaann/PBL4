package t;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class ádn {

    private Image loadImage(String imgFileName) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resURL = classLoader.getResource(imgFileName);
        if (resURL == null) {
            return null;
        } else {
            File imgFile = new File(resURL.toURI());
            return ImageIO.read(imgFile);
        }
    }

    private void drawImage(Graphics2D g2, Image img) {
        g2.drawImage(img, 1, 1, 100, 100, null);
    }

    public void main(String[] args) throws Exception {
        Image img = loadImage("Bishop-white.png");
        JFrame frame = new JFrame("Chess");
        frame.setSize(600, 600);
        frame.setResizable(false);
        Graphics2D g;
    }
}
