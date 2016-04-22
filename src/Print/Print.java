/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Print;

import Photofilters.Greyscale;
import Photofilters.Sepia;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    // output is now set for demo purposes. Needs to change when live
    private final String output = "C:\\Users\\Vito Corleone\\Desktop\\PTS4OutputPDF\\";

    public Print(String photoID, String inputLocation, String quantity, String type) {
        this.photoID = photoID;
        this.input = inputLocation;
        this.quantity = Integer.parseInt(quantity);
        this.type = type;
        System.out.println("print class type " + type);
        checkPhotoFilter();
        //printToPDF();
    }

    private void printToPDF(BufferedImage bufferedImage) {
        // define pdfprinter and within try clause create
        for (int i = 0; i < quantity; i++) {
            PdfWriter writer = null;
            try {

                // define fileoutputstream and within try clause create
                FileOutputStream fos = null;

                document = new Document(PageSize.A4.rotate());
                try {
                    fos = new FileOutputStream(output + photoID + "-" + Integer.toString(i + 1) + ".pdf");
                } catch (FileNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }

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

    private void checkPhotoFilter() {
        if (type.equals("Sepia")) {
            generateSepia();
        } else if (type.equals("Black")) {
            generateGreyscale();
        } else {
            printToPDF(null);
            //generateColor();
        }
    }

    private void generateGreyscale() {
        greyscale = new Greyscale(input);
        BufferedImage greyImage = greyscale.toGrayScale();
        printToPDF(greyImage);
        System.out.println("print grey");
    }

    private void generateSepia() {
        sepia = new Sepia(input);
        BufferedImage sepiaImage = sepia.color2sepia();
        printToPDF(sepiaImage);
        System.out.println("print sepia");
    }

    private void generateColor() {

        try {
            BufferedImage in = ImageIO.read(new File(input));
            System.out.println(input);
            BufferedImage newImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
            printToPDF(newImage);
            System.out.println("print color");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
