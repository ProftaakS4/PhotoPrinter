/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Photofilters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Vito Corleone
 */
public class Sepia {

    private BufferedImage master = null;
    private String imagepath;

    public Sepia(String imagepath) {
        this.imagepath = imagepath;
        readOriginalImage();
    }

    public void readOriginalImage() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            master = ImageIO.read(new File(imagepath));
            System.out.println(imagepath);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public BufferedImage color2sepia() {

        try {
            BufferedImage sepia = new BufferedImage(master.getWidth(), master.getHeight(), BufferedImage.TYPE_INT_RGB);
            // Play around with this.  20 works well and was recommended
            //   by another developer. 0 produces black/white image
            int sepiaDepth = 20;

            int w = master.getWidth();
            int h = master.getHeight();

            WritableRaster raster = sepia.getRaster();

            // We need 3 integers (for R,G,B color values) per pixel.
            int[] pixels = new int[w * h * 3];
            master.getRaster().getPixels(0, 0, w, h, pixels);

            //  Process 3 ints at a time for each pixel.  Each pixel has 3 RGB
            //    colors in array
            for (int i = 0; i < pixels.length; i += 3) {
                int r = pixels[i];
                int g = pixels[i + 1];
                int b = pixels[i + 2];

                int gry = (r + g + b) / 3;
                r = g = b = gry;
                r = r + (sepiaDepth * 2);
                g = g + sepiaDepth;

                if (r > 255) {
                    r = 255;
                }
                if (g > 255) {
                    g = 255;
                }
                if (b > 255) {
                    b = 255;
                }

                // normalize if out of bounds
                if (b < 0) {
                    b = 0;
                }
                if (b > 255) {
                    b = 255;
                }

                pixels[i] = r;
                pixels[i + 1] = g;
                pixels[i + 2] = b;
            }
            raster.setPixels(0, 0, w, h, pixels);

            return sepia;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
