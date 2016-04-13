package gyrobot.communication;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.awt.event.*;
import java.awt.*;
import java.net.URISyntaxException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

import org.json.*;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;


import jssc.*;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortEvent.*;

/**
 *
 * @author EABDERH
 */
public class appserver {

    static SerialPortIO COMPort;
    static Socket webSocket;

    private JFrame jFrame;
    private JPanel jPanel;
    private JButton jButton;

    private final CountDownLatch exitLatch;



    public static void main(String[] args) {
        System.out.println("Client started");
        try {
            new appserver();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public appserver() throws Exception {

        initCommunication();

        startGUI();
        webSocket.connect();
        while(!webSocket.connected()) {}

        serverReset();

        exitLatch = new CountDownLatch(1);

        try {
            exitLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Shutdown");

        webSocket.disconnect();
        while(webSocket.connected()) {}

        webSocket.off();
        jFrame.dispatchEvent(
            new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING)
        );
    }


    public void initCommunication() throws URISyntaxException {
        COMPort = new SerialPortIO();
        webSocket = IO.socket("http://www.gyrobot.tech:80");


        webSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("SOCK: connected");
            }
        });
        webSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("SOCK: disconnected");
                // garbage? supposed to reconnect
                //if (exitLatch.getCount() != 0) {
                //    // reconnect
                //}
            }
        });
        webSocket.on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("SOCK: error");
                webSocket.emit("message","appserver");
            }
        });
        webSocket.on("assignment", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("SOCK: assignment");
                System.out.println(args[0]);
            }
        });
        webSocket.on("command", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("SOCK: command");
                String command = args[0].toString();
                System.out.println(command);
                if (command.equals("on")) {
                    COMPort.write((byte) 1);
                }
                else {
                    COMPort.write((byte) 0);
                }
            }
        });
    }

    public serverReset() {
        webSocket.emit("status","disconnected");
        COMPort.start();
        webSocket.emit("status","connected");
    }


    static class myclass implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent serialPortEvent) {
            byte dataByte = 0;
            String dataStr;
            System.out.println("SPIO: event");
            if (serialPortEvent.isRXCHAR()) {
                System.out.println("SPIO: rxchar");
                System.out.printf( "SPIO: data bytes: %d",
                                    (int) serialPortEvent.getEventValue());
            if (serialPortEvent.getEventValue() == 1) {
                dataByte = COMPort.read();
            }
            dataStr = String.valueOf((int) dataByte);
            webSocket.emit("angle",dataStr);
            }
        }
    };

        

    private void startGUI() {
        jFrame = new JFrame("Bluetooth Server");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(400, 400);

        jPanel = new JPanel(new GridBagLayout());
        jPanel.setBackground(Color.LIGHT_GRAY);
        jFrame.add(jPanel);

        jButton = new JButton("Close");
        jButton.setBorder(new LineBorder(Color.BLACK));
        jButton.setPreferredSize(new Dimension(100,100));
        jButton.setFocusPainted(false);
        jButton.setBackground(Color.WHITE);
        jButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("GUI: server shutdown initialized");
                exitLatch.countDown();
            }
        });

        jPanel.add(jButton);
        jFrame.setVisible(true);
        System.out.println("GUI: start");
    }
}



