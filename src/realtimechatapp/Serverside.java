/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package realtimechatapp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author gehad
 */
public class Serverside {
    


        ServerSocket ss;

    public Serverside () {
        try {
            ss = new ServerSocket(5007);
            while (true) {
                Socket s = ss.accept();
                new ChatHandler(s);
            }

        } catch (IOException e) {
            

        }
    }

    public static void main(String[] args) {
        new Serverside ();
    }
}
    

