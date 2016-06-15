/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Photofilters;

import java.awt.EventQueue;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Vito Corleone
 */
public class Greyscale {

    private BufferedImage master = null;
    private String imagepath;

    public Greyscale(String imagepath) {
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

    public BufferedImage toGrayScale() {

        try {
            BufferedImage gray = new BufferedImage(master.getWidth(), master.getHeight(), BufferedImage.TYPE_INT_ARGB);
            // Automatic converstion....
            ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
            op.filter(master, gray);

            return gray;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
