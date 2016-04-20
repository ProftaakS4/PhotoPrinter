/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebSockets;

import Database.Database;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Vito Corleone
 */
public class ServerRunnable implements Runnable {

    // define socket object
    private Socket socket = null;
    
    // define object outputstream
    private final ObjectOutputStream out = null;
    
    // define objectinputstream
    private final ObjectInputStream in = null;
    
    // define database object
    private Database database = null;
    
    // define id and quantity references
    private String id;
    private String quantity;

    // constructon with incoming socket
    public ServerRunnable(Socket s) {
        this.socket = s;
    }

    // implemented runnable.run method
    @Override
    public void run() {
        try {
            // get the correct inputstream
            InputStream is = socket.getInputStream();

            // Receiving
            byte[] lenBytes = new byte[4];
            is.read(lenBytes, 0, 4);
            int len = (((lenBytes[3] & 0xff) << 24) | ((lenBytes[2] & 0xff) << 16)
                    | ((lenBytes[1] & 0xff) << 8) | (lenBytes[0] & 0xff));
            byte[] receivedBytes = new byte[len];
            is.read(receivedBytes, 0, len);
            String received = new String(receivedBytes, 0, len);

            // break down the received chars into photoID and quantity
            int semicolon = received.indexOf(";");
            int hashtag = received.indexOf("#");
            int total = received.length();
            String id = received.substring(0, semicolon);
            String quantity = received.substring(semicolon + 1, hashtag);
            String type = received.substring(hashtag +1, total);
            System.out.println("id = " + id + " quantity = " + quantity + " type = " + type);
            
            // call the database to get the HIGH resolution image path of the corresponding photoID;
            database = new Database(id, quantity, type);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
