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

            System.out.println("Received " + received);

            // set a boolean for a new order            
            if (received.equals("BEGIN")) {
                System.out.println("BEGIN");
                newOrder = true;
                return;
            } else if (received.contains("INDEX")) {
                int X = received.indexOf("X");
                int customerIndex = received.indexOf("?");
                String photoIds = received.substring(X+1, customerIndex);
                String customer = received.substring(customerIndex+ 1, received.length());
                System.out.println("CustomerID = " + customer);
                ArrayList<String> ids = getIDSForIndex(photoIds);
                ArrayList<String> imagesPath = new ArrayList<String>();
                Database database = null;
                if (ids.size() > 1) {
                    for (String id : ids) {
                        if (!id.isEmpty()) {
                            database = new Database(id);
                            String imagepath = database.getImagePath();
                            imagesPath.add(imagepath);
                        }
                    }
                }
                if (imagesPath.size() > 1) {
                    Print printer = new Print();
                    printer.printIndex(imagesPath, database.getCustomer(customer));
                }
                return;
            } else if (received.equals("END")) {
                System.out.println("END");
                newOrder = false;
                return;
            }
            if (!received.equals("BEGIN") || received.equals("END") || !received.contains("INDEX")) {
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
                database.executeQuery();
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private int[] getCropPositions(String received) {
        int[] values = null;
        if (received.contains("&")) {
            received = received + " ";
            values = new int[4];
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
        }
        return values;
    }

    private ArrayList<String> getIDSForIndex(String received) {
        String text = received + " ";
        int index = text.indexOf("X") + 2;
        ArrayList<String> temp = new ArrayList<String>();
        String id = "";
        for (int i = index; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c != ' ') {
                id = id + c;
            } else {
                temp.add(id);
                System.out.println(id);
                id = "";
            }
        }
        return temp;
    }
}
