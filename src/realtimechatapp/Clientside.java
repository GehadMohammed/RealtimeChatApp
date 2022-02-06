/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package realtimechatapp;


import javafx.application.Application;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.*;


public class Clientside extends Application {

    
    TextField message;
    Button send;
    TextArea chat_content;
    Label  label ;
    Socket clientSocket;
    DataInputStream dis;
    PrintStream ps;
    Button save;
    Button open;
    Thread socketThread;
    @Override
     public void init() throws IOException
    {
        chat_content = new TextArea();
        chat_content.setEditable(false);
        
        label = new Label("Enter Your Message");
        message = new TextField();
        message.setPromptText("Enter Your Message");
        
        send = new Button("Send");
        send.setDefaultButton(true);
        
        save= new Button("Save");        
        open= new Button("Open");
        
 
        clientSocket=new Socket("127.0.0.1", 5007);    
        dis=new DataInputStream(clientSocket.getInputStream());
        ps=new PrintStream(clientSocket.getOutputStream());
       
        
          socketThread= new Thread (new Runnable(){
            @Override
            public void run(){
                while(true){
                    String reply; 
                        
                    try {                    
                       if(clientSocket==null){
                        clientSocket.close();
                       }
                       else
                       {
                        reply=dis.readLine();
                        chat_content.appendText("\n"+reply);
                       }
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(Clientside.class.getName()).log(Level.SEVERE, null, ex);
                    }
                                       
                    
        }}});
          socketThread.start();
                
    }
        public void start(Stage primaryStage) throws IOException {
    
        
        FlowPane hor_root1 = new FlowPane(Orientation.HORIZONTAL);
        FlowPane hor_root2 = new FlowPane(Orientation.HORIZONTAL);
        hor_root2.getChildren().addAll(save,open);
       
        hor_root2.setVgap(5);
        hor_root2.setMargin(save, new Insets(20, 0, 20, 20));
        hor_root2.setMargin(open, new Insets(20, 0, 20, 20));
        
        BorderPane root = new BorderPane();
        
        hor_root1.getChildren().addAll(label ,message,send);
        hor_root2.setVgap(5);
        hor_root2.setMargin(label, new Insets(20, 0, 20, 20));
        hor_root2.setMargin(message, new Insets(20, 0, 20, 20));           
        hor_root2.setMargin(send, new Insets(20, 0, 20, 20));
        
        root.setTop(hor_root2);
        root.setCenter(chat_content);
        root.setBottom(hor_root1);
        
        Scene scene = new Scene(root, 500, 500);
        
        primaryStage.setTitle("Meesanger");
        primaryStage.setScene(scene);
        primaryStage.show();
        
            
        send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            
                ps.println(message.getText());
                message.setText("");
            }    
        });
        
        
         
        FileChooser fil_chooser = new FileChooser();
        open.setOnAction((ActionEvent event) -> 
        {
             
             File file = fil_chooser.showOpenDialog(primaryStage);
                         
             if (file != null) {
                    System.out.println("file==="+file.getAbsolutePath());                       
                    FileReader open_file = null;
                    chat_content.clear();
                    try {
                    
                        open_file = new FileReader(file.getAbsolutePath());
                        char [] file_content= new char [(int)file.length()];                           
                        open_file.read(file_content);
                        open_file.close();
                             System.out.println(file_content);
                        for(char c:file_content){
                            chat_content.appendText(Character.toString(c));
                        }
                    }
                    catch (FileNotFoundException ex) {
                        java.util.logging.Logger.getLogger(Clientside.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(Clientside.class.getName()).log(Level.SEVERE, null, ex);
                    }
                  
                
                        
                       
            
             }
        });
         
          save.setOnAction((ActionEvent event) -> {
  
              File file = fil_chooser.showSaveDialog(primaryStage);
                          System.out.println(file.getAbsolutePath());
             if (file != null) {
                    System.out.println("file==="+file.getAbsolutePath());                       
                    FileWriter save_file = null;
                  
                      try {
                    
                        save_file = new FileWriter(file.getAbsolutePath());
                        save_file.write(chat_content.getText());
                        save_file.close();
                           
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(Clientside.class.getName()).log(Level.SEVERE, null, ex);
                    }
             
             }
             });

        }
   
        public void stop() throws IOException, Exception
        {
                    super.stop();
                    socketThread.stop(); 
                    dis.close();
                    ps.close();            
                    clientSocket.close();
                   
          
        }

    public static void main(String[] args) {
        launch(args);
    }
    
}