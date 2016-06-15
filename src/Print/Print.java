/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Print;

import Photofilters.Greyscale;
import Photofilters.Sepia;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Vito Corleone
 */
public class Print {

    // define document object
    private Document document;
    private Greyscale greyscale;
    private Sepia sepia;

    // define the properties
    private final String input; // .gif and .jpg are ok too!
    private final String photoID;
    private int quantity;
    private String type;
    private int[] cropValues;

    // output is now set for demo purposes. Needs to change when live
    private final String output = "/home/student/Pictures/";

    public Print(String photoID, String inputLocation, String quantity, String type, int[] cropValues) {
        this.photoID = photoID;
        this.input = inputLocation;
        this.quantity = Integer.parseInt(quantity);
        this.type = type;
        this.cropValues = cropValues;
        System.out.println("print class type " + type);
        BufferedImage result = checkPhotoFilter();
        printToPDF(result);
    }

    public Print(String photoPath, String type, int[] cropValues) {
        this.photoID = null;
        this.input = photoPath;
        this.type = type;
        this.cropValues = cropValues;
    }

    private void printToPDF(BufferedImage bufferedImage) {
        // define pdfprinter and within try clause create
        for (int i = 0; i < quantity; i++) {
            PdfWriter writer = null;
            try {
                // define fileoutputstream and within try clause create
                FileOutputStream fos = null;

                document = new Document(PageSize.A4.rotate());
                fos = new FileOutputStream(output + photoID + "-" + Integer.toString(i + 1) + ".pdf");

                // create the writer object
                try {
                    writer = PdfWriter.getInstance(document, fos);
                } catch (DocumentException ex) {
                    System.out.println(ex.getMessage());
                }

                // open the writer and the document and add the image into the document
                writer.open();
                document.open();
                try {
                    PdfContentByte pdfCB = new PdfContentByte(writer);
                    if (bufferedImage == null) {
                        Image image = Image.getInstance(input);
                        image.scaleToFit(640, 480);
                        document.add(image);
                    } else {
                        Image image = Image.getInstance(pdfCB, bufferedImage, 1);
                        image.scaleToFit(640, 480);
                        document.add(Image.getInstance(image));
                    }

                } catch (DocumentException ex) {
                    System.out.println(ex.getMessage());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                // close the document and writer
                document.close();
                writer.close();

                System.out.println("Printer done.");

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private BufferedImage checkPhotoFilter() {
        BufferedImage result = null;

        if (type.equals("Sepia")) {
            result = generateSepia();
        } else if (type.equals("Black")) {
            result = generateGreyscale();
        } else if (cropValues != null) {
            result = generateCrop(cropValues[0], cropValues[1], cropValues[2], cropValues[3]);
        } else {
            result = null;
        }
        return result;
    }

    private BufferedImage generateGreyscale() {
        BufferedImage greyImage = null;
        try {
            greyscale = new Greyscale(input);
            greyImage = greyscale.toGrayScale();
            //printToPDF(greyImage);
            System.out.println("print grey");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return greyImage;
    }

    private BufferedImage generateSepia() {
        BufferedImage sepiaImage = null;
        try {
            sepia = new Sepia(input);
            sepiaImage = sepia.color2sepia();
            //printToPDF(sepiaImage);
            System.out.println("print sepia");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return sepiaImage;
    }

    private BufferedImage generateColor() {
        BufferedImage in = null;
        try {
            in = ImageIO.read(new File(input));
            System.out.println(input);
            BufferedImage newImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
            //printToPDF(newImage);            
            //System.out.println("print color");
            return newImage;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return in;
    }

    private BufferedImage generateCrop(int x, int y, int w, int h) {
        BufferedImage in = null;
        System.out.println("incoming " + x + " " + y +" " + w + " " +h);
        try {
            //Rectangle newRectangle = new Rectangle(x, y, w, h);            
            in = ImageIO.read(new File(input));
            BufferedImage newImage = in.getSubimage(x, y, w, h);
            System.out.println("print crop");
            return newImage;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return in;
    }

    public BufferedImage index() {
        BufferedImage normal = null;
        BufferedImage index = null;
        normal = checkPhotoFilter();
        if (normal != null) {
            index = new BufferedImage(128, 96, BufferedImage.TYPE_INT_RGB);
            Graphics g = index.createGraphics();
            g.drawImage(normal, 0, 0, 128, 96, null);
            g.dispose();
        }
        return index;
    }

    public void printIndex(ArrayList<BufferedImage> bufferedImages) {
        // define pdfprinter and within try clause create
        PdfWriter writer = null;
        FileOutputStream fos = null;
        document = new Document(PageSize.A4.rotate());
        try {
            fos = new FileOutputStream("index.pdf");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Print.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (BufferedImage image : bufferedImages) {            
                try {
                    writer = PdfWriter.getInstance(document, fos);
                } catch (DocumentException ex) {
                    System.out.println(ex.getMessage());
                }
                writer.open();
                document.open();
                try {
                    PdfContentByte pdfCB = new PdfContentByte(writer);
                    Image newImage = Image.getInstance(pdfCB, image, 1);
                    document.add(Image.getInstance(newImage));
                } catch (DocumentException ex) {
                    System.out.println(ex.getMessage());
                } catch (IOException ex) {
                Logger.getLogger(Print.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        document.close();
        writer.close();
        System.out.println("Printer done.");
    }
}
