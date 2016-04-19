/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

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

    String id = "";
    String quantity = "";
    Connection connection = null;

    DatabaseSingleton databaseSingleton = DatabaseSingleton.getInstanceSingleton();

    public Database(String id, String quantity) {
        this.id = id;
        this.quantity = quantity;
        connection = databaseSingleton.getDatabaseConnection();
        executeQuery();
    }

    private void executeQuery() {
        // 
        String query = "{call getPhotoPath(?,?)}";

        try {
            CallableStatement stmt = (CallableStatement) connection.prepareCall(query);

            stmt.setInt(1, Integer.parseInt(id));
            stmt.registerOutParameter(2, java.sql.Types.VARCHAR);
            stmt.execute();
            String imagePath = stmt.getString(2);
            System.out.println("Image ID = " + id+ " Imagepath = " + imagePath);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
