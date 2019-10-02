package AI;

import java.awt.image.BufferedImage;

public class ShapeDetection {

    public void findSquares(BufferedImage image) {

        int startX = 0;
        int startY = 0;
        int currentPixel = 0;

        for (int y = 0; y < image.getWidth(); y++) {
            for (int x = 0; x < image.getHeight(); x++) {

                float pixelColor = image.getRGB(x, y);
                if (pixelColor < 0.01) {

                }


            }
        }

    }


}
