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
import java.io.OutputStream;
import java.net.Socket;
import java.util.Dictionary;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vito Corleone
 */
public class ServerRunnable implements Runnable {

    private Socket socket = null;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    private Database database = null;
    String id;
    String quantity;

    /**
     * Create object with a given socket.
     *
     * @param s Socket that is used to communicate with client
     */
    public ServerRunnable(Socket s) {
        this.socket = s;
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            //OutputStream os = socket.getOutputStream();

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
            int total = received.length();
            String id = received.substring(0, semicolon);
            String quantity = received.substring(semicolon + 1, total);
            System.out.println("id = " + id + " quantity = " + quantity);
            
            // call the database to get the HIGH resolution image path of the corresponding photoID;
            database = new Database(id, quantity);

        } catch (IOException ex) {
        }
    }
}
