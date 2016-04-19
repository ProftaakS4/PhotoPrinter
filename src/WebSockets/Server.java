/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebSockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Vito Corleone
 */
public class Server {
    
    
    public static void main(String[] args) {
        try {
            // Establish server socket
            ServerSocket serverSocket = new ServerSocket(4343,10);
            
            while (true) {
                try {
                    // Wait for client connection
                    Socket incomingSocket = serverSocket.accept();
                    
                    // Handle client request in a new thread
                    Thread t = new Thread(new ServerRunnable(incomingSocket));
                    t.start();
                } catch (IOException e) {
                }
            }

        } catch (IOException ex) {
        }
    }
}
