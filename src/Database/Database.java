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
import java.sql.ResultSet;
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

    public Database(String id) {
        this.id = id;
    }

    public String executeQuery() {
        if (!id.isEmpty()) {
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
        }
        return imagePath;
    }
    
    public String getImagePath() {
        if (!id.isEmpty()) {
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

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return imagePath;
    }

    public BufferedImage createIndex(String photoPath) {
        printer = new Print(photoPath);
        return printer.index();
    }


    public String getType() {
        return this.type;
    }

    public int[] getCropValues() {
        return this.cropValues;
    }
    
    // CUSTOMER OVRAGEN EN GEVEN AAN DE INDEX METHODE OM EEN INDEXKAART OP NAAM TE MAKEN
    public String getCustomer(String id) {
        String firstName = "FirstName";
        String lastName = "LastName";
        String streetName = "StreetName";
        String houseNumber = "HouseNumber";
        String zipCode = "ZipCode";
        String city = "City";
        String phoneNumber = "PhoneNumber";
        String iban = "Iban";
                
        if (!id.isEmpty()) {
            connection = databaseSingleton.getDatabaseConnection();
            // the name of the stored procedure 
            String query = "{call getUserInformation(?)}";
            try {
                // create the statement
                java.sql.CallableStatement stmt = connection.prepareCall(query);

                // set the first procedure paramater
                stmt.setInt(1, Integer.parseInt(id));
                stmt.execute();
                
                // set the procedure output parameter
                //stmt.registerOutParameter(2, java.sql.Types.VARCHAR);

                // Resultset
                ResultSet rs = stmt.getResultSet();                
                
                while(rs.next()){
                    firstName = rs.getString(2);
                    lastName = rs.getString(3);
                    streetName = rs.getString(4);
                    houseNumber = rs.getString(5);
                    zipCode = rs.getString(6);
                    city = rs.getString(7);
                    phoneNumber = rs.getString(8);
                    iban = rs.getString(9);
                }
                rs.close();
                
                return firstName + " " + lastName + " " + streetName + " " + houseNumber + " \n"
                        + zipCode + " " + city + " \n" + phoneNumber + " " + iban;

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return firstName;
    }
}
