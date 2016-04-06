package gyrobot.communication;

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

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author EABDERH
 */
public class appserver {

	private SerialPortIO COMPort;
	private Socket webSocket;

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
		COMPort.start();
		webSocket.connect();
		while(!webSocket.connected()) {}

		startGUI();

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


	public initCommunication() {
		COMPort = new SerialPortIO();
		webSocket = IO.socket("http://www.gyrobot.tech:80");


		webSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				System.out.println("SOCK: connected");
				webSocket.emit("message","bluetooth_server");
			}
		});
		webSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				System.out.println("SOCK: disconnected");
				// garbage? supposed to reconnect
				//if (exitLatch.getCount() != 0) {
				//	// reconnect
				//}
			}
		});
		webSocket.on(Socket.EVENT_ERROR, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				System.out.println("SOCK: error");
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

		COMPort.serialPortEventListener = new SerialPortEventListener() {
			@Override
			public void serialEvent(SerialPortEvent serialPortEvent) {
				byte dataByte;
				String dataStr;
				System.out.println("SPIO: event");
				if (serialPortEvent.isRXCHAR()) {
				System.out.println("SPIO: rxchar");
					System.out.println(	"SPIO: data bytes: %d",
										serialPortEvent.getEventValue);
					if (serialPortEvent.getEventValue == 1) {
						try {
							dataByte = serialPort.readByte();
						}
						catch (SerialPortException ex) {
							System.out.println(ex);
						}
					}
					dataStr = new String(dataByte);
					webSocket.emit("angle",dataStr);
				}
			}
		};
	}


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



