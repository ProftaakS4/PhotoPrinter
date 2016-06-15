/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebSockets;

import Database.Database;
import Print.Print;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
    private int[] cropValues;
    private boolean newOrder = false;
    private ArrayList<Database> photoIDFromOrder = new ArrayList<>();
    private ArrayList<BufferedImage> images;

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
            
            System.out.println(received);
            
            // set a boolean for a new order            
            if (received.equals("BEGIN")) {
                System.out.println("BEGIN");
                newOrder = true;
                return;
            }
            else if (received.equals("END")) {
                System.out.println("END");
                images = new ArrayList<>();                
                Print print = null;
                for(Database database : photoIDFromOrder){
                    print = new Print(database.getImagePath(),database.getType(),database.getCropValues());
                    images.add(print.index());
                }
                if(images.size() > 0){
                   print.printIndex(images); 
                }                    
                photoIDFromOrder.clear();
                images.clear();
                newOrder = false;
                return;
            }
            if (!received.equals("BEGIN") || received.equals("END")) {
                System.out.println("processing");
                
                // break down the received chars into photoID and quantity
                int semicolon = received.indexOf(";");
                System.out.println(semicolon);
                int hashtag = received.indexOf("#");
                if (received.contains("&")) {
                    cropValues = getCropPositions(received);                    
                }

                int total = received.length();
                String id = received.substring(0, semicolon);
                String quantity = received.substring(semicolon + 1, hashtag);
                String type = received.substring(hashtag + 1, total);
                if (type.contains("BLACK")) {
                    type = "Black";
                } else if (type.contains("SEPIA")) {
                    type = "Sepia";
                } else {
                    type = "Color";
                }
                System.out.println("id = " + id + " quantity = " + quantity + " type = " + type);

                // call the database to get the HIGH resolution image path of the corresponding photoID;
                database = new Database(id, quantity, type, cropValues);
                photoIDFromOrder.add(database);
                database.executeQuery();                
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private int[] getCropPositions(String received) {
        received = received + " ";
        int[] values = new int[4];
        int arrayIndex = 0;
        int symbol = received.indexOf("&") + 1;
        System.out.println("symbol " + symbol);
        String value = "";
        for (int i = symbol; i < received.length(); i++) {
            char c = received.charAt(i);
            if (c != ' ') {
                value = value + c;
                System.out.println(value);
            } else {
                //System.out.println("index " + arrayIndex);
                values[arrayIndex] = (int) Math.round(Double.parseDouble(value) * 1.5);
                //System.out.println("" + values[arrayIndex]);
                arrayIndex++;
                value = "";
            }
        }
        return values;
    }
}
