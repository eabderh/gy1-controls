/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elias.test;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.awt.event.*;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

import org.json.*;

//import java.io.InputStream;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.comm.*;
//import java.util.*; 

//import org.json.JSONObject;
/**
 *
 * @author EABDERH
 */
public class AcknowledgeExample {
    
    private TextField textField;
    private Panel controlPanel;
    private Frame mainFrame;
    private SimpleRead comport;
    
    public static void main(String[] args) {


        System.out.println("Client started");
        try {
            new AcknowledgeExample();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Socket mysocket;

    public AcknowledgeExample() throws Exception {
        comport = new SimpleRead();
        mysocket = IO.socket("http://www.gyrobot.tech:80");
        mysocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Connected");
                mysocket.emit("message","bluetooth_server");
            }
        });
        
        mysocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Socket Disconnected");
            }
        });
        
        mysocket.on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Error");
            }
        });
        
        mysocket.on("assignment", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println(args[0]);
            }
        });
        mysocket.on("command", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Command");
                String command = args[0].toString();
                System.out.println(command);
                if (command.equals("on")) {
                    comport.write((byte) 1);
                }
                else {
                    comport.write((byte) 0);
                }
            }
        });
        
        mysocket.connect();
        while(!mysocket.connected()) {
        }

        System.out.println("test");

        exitlatch = new CountDownLatch (1);
        

        
        
        
        
        
        gui();
        
        try {
            exitlatch.await();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //Thread.sleep(5000);
        System.out.println("Shutdown");
        mysocket.disconnect();
        while(mysocket.connected()) {
        }
        mysocket.off();
        
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        
        
        
    }
    private JFrame frame;
    private JPanel panel;
    private JButton button;
    
    private final CountDownLatch exitlatch;
    
    private void gui() {
        System.out.println("gui");
        frame = new JFrame("Bluetooth Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        
        panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.LIGHT_GRAY);
        frame.add(panel);

        
        button = new JButton("Close");
        button.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("stopserver init");
                exitlatch.countDown();
            }
        }
        );
        button.setBorder(new LineBorder(Color.BLACK));
        button.setPreferredSize(new Dimension(100,100));
        button.setFocusPainted(false);
        //button.setContentAreaFilled(false);
        button.setBackground(Color.WHITE);
        panel.add(button);
        
        frame.setVisible(true);
    }
    
    
}
