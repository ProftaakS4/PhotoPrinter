/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import Print.Print;
import com.mysql.jdbc.CallableStatement;
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    // get the databasesingleton instance
    private DatabaseSingleton databaseSingleton = DatabaseSingleton.getInstanceSingleton();

    public Database(String id, String quantity, String type) {
        this.id = id;
        this.quantity = quantity;
        this.type = type;
        connection = databaseSingleton.getDatabaseConnection();
        executeQuery();
    }

    private void executeQuery() {
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
            String imagePath = stmt.getString(2);
            System.out.println("Image ID = " + id + " Imagepath = " + imagePath + "Type " + type);

            // create the printer object and print the file to pdf
            printer = new Print(id, "C:\\Users\\Vito Corleone\\Documents\\NetBeansProjects\\WebSocketsPTS4\\Godfather15_flip.jpg", quantity, type);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
