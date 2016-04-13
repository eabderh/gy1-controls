package gyrobot.communication;


import jssc.*;
import jssc.SerialPortEventListener;

/**
 *
 * @author EABDERH
 */

public class SerialPortIO {
    public SerialPort serialPort;
    public static boolean portOn;
    //SerialPortEventListener serialPortEventListener;

       // public abstract class myserialPortEventListener implements SerialPortEventListener {

       // }

    public SerialPortIO(){
        serialPort = new SerialPort("COM4");
        //portReadout();
    }


    public void portReadout() {
        String[] portNames = SerialPortList.getPortNames();
        if (portNames.length == 0) {
            System.out.println("No serial-ports were found");
            System.out.println("Press Enter to exit...");
            try {
                System.in.read();
            } catch (Exception e) {
                 // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return;
        }
        for (int i = 0; i < portNames.length; i++){
            System.out.println(portNames[i]);
        }
    }


    public void start() {
        while (true) {
            try {
                serialPort.openPort();
                // you can also use this line - serialPort.setParams(9600, 8, 1, 0);
                serialPort.setParams(   SerialPort.BAUDRATE_9600,
                                        SerialPort.DATABITS_8,
                                        SerialPort.STOPBITS_1,
                                        SerialPort.PARITY_NONE);
                break;
            }
            catch (SerialPortException ex) {
                System.out.println(ex);
            }
        }
        portOn = true;
    }


    public void write(byte c) {
        try {
            serialPort.writeByte(c);
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }
 
        public byte read() {
            byte c[] = null;
        try {
            c = serialPort.readBytes();
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
                if (c != null) {
                    return c[0];
                }
                return 0;
    }
}




