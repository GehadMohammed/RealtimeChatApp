/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package realtimechatapp;


import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ChatHandler extends Thread {

    DataInputStream dis;
    PrintStream ps;
    Socket currentcs;
    static public int counter=1;
    String id;
    
    static Vector<ChatHandler> clientsVector =  new Vector <ChatHandler>();
    
    
    public ChatHandler (Socket cs) throws IOException
    {
        currentcs=cs;
        try
        {
            dis = new DataInputStream (cs.getInputStream());
            ps= new PrintStream(cs.getOutputStream());
            id="client "+String.valueOf(counter);
            clientsVector.add(this);
            start();
 
        }
        catch (Exception ex)
        {
        }    
        counter++;
    }
    
    @Override
    public void run () 
    {
        while(true)
        {
            try
            {
                String str=dis.readLine();  // Get massage from the client
                if(str!=null)
                {
                sendMessageToAll(str);
                }
                else
                {
                check();
                break;
             
                }
            }
            catch(IOException ex)
            {
               
               check();
               break;
              
            }
        
        }
    
    }

    public void check()
    {
    
                try {
                     this.stop();
                     dis.close();
                     ps.close();
                     currentcs.close();
                     clientsVector.remove(this);
                } catch (IOException ex1) {
                    Logger.getLogger(ChatHandler.class.getName()).log(Level.SEVERE, null, ex1);
                }
               
    }
    
    private void sendMessageToAll(String str) {
        
        
        for(ChatHandler ch : clientsVector)
        {
          
            ch.ps.println(id+":  "+str);
          
            }
        
        }
        

    }
    
    
    
    
    
    

