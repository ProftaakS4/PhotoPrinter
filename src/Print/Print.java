/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Print;

import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Vito Corleone
 */
public class Print {

    // define document object
    private final Document document;

    // define the properties
    private final String input; // .gif and .jpg are ok too!
    private final String photoID;

    // output is now set for demo purposes. Needs to change when live
    private final String output = "C:\\Users\\Vito Corleone\\Desktop\\PTS4OutputPDF\\";

    public Print(String photoID, String inputLocation) {
        this.input = inputLocation;
        this.photoID = photoID;
        document = new Document();
        printToPDF();
    }

    private void printToPDF() {
        // define pdfprinter and within try clause create
        PdfWriter writer = null;
        try {
            
            // define fileoutputstream and within try clause create
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(output + photoID + ".pdf");
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
                document.add(Image.getInstance(input));
            } catch (DocumentException ex) {
                System.out.println(ex.getMessage());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println("Printer done.");
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            // close the document and writer
            document.close();
            writer.close();
        }
    }
}
