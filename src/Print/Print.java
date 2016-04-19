/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Print;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vito Corleone
 */
public class Print {

    Document document = new Document();
    String input; // .gif and .jpg are ok too!
    String photoID;
    String output = "C:\\Users\\Vito Corleone\\Desktop\\PTS4OutputPDF\\";

    public Print(String photoID, String inputLocation) {
        this.input = inputLocation;  
        this.photoID = photoID;
        printToPDF();
    }
    
    private void printToPDF(){
        try {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(output+photoID+".pdf");
            } catch (FileNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
            PdfWriter writer = null;
            try {
                writer = PdfWriter.getInstance(document, fos);
            } catch (DocumentException ex) {
                System.out.println(ex.getMessage());
            }
            writer.open();
            document.open();
            try {
                document.add(Image.getInstance(input));
            } catch (DocumentException ex) {
                System.out.println(ex.getMessage());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            document.close();
            writer.close();
            System.out.println("Printer done.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
