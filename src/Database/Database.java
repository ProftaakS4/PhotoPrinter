/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Print.Print;
import com.mysql.jdbc.CallableStatement;
import com.mysql.jdbc.Connection;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Vito Corleone
 */
public class Database {

    // set the database properties
    private String id = "";
    private String quantity = "";
    private String type = "";

    // define the connection
    private Connection connection = null;

    // define the printer object
    private Print printer = null;
    private int[] cropValues;
    private String imagePath = "";

    // get the databasesingleton instance
    private DatabaseSingleton databaseSingleton = DatabaseSingleton.getInstanceSingleton();

    public Database(String id, String quantity, String type, int[] cropValues) {
        this.id = id;
        this.quantity = quantity;
        this.type = type;
        this.cropValues = cropValues;        
    }

    public Database() {

    }

    public String executeQuery() {
        connection = databaseSingleton.getDatabaseConnection();
        // the name of the stored procedure 
        String query = "{call getPhotoPath(?,?)}";
        try {
            // create the statement
            CallableStatement stmt = (CallableStatement) connection.prepareCall(query);

            // set the first procedure paramater
            stmt.setInt(1, Integer.parseInt(id));

            // set the procedure output parameter
            stmt.registerOutParameter(2, java.sql.Types.VARCHAR);

            // execute the query
            stmt.execute();

            // get the output parameter
            imagePath = stmt.getString(2);
            int totalChars = imagePath.length();
            imagePath = imagePath.substring(2, totalChars);
            System.out.println("Image ID = " + id + " Imagepath = /home/student" + imagePath + " Type " + type);

            // create the printer object and print the file to pdf
            printer = new Print(id, "/home/student" + imagePath, quantity, type, cropValues);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return imagePath;
    }

    public BufferedImage createIndex(String photoPath, String type, int[] cropValues) {
        printer = new Print(photoPath, type, cropValues);
        return printer.index();
    }
    
    public String getImagePath(){
        return this.imagePath;
    }
    
    public String getType(){
        return this.type;
    }
    
    public int[] getCropValues(){
        return this.cropValues;
    }
}
