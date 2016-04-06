package appserver.communication;


import jssc.*;
import jssc.SerialPortEventListener;

/**
 *
 * @author EABDERH
 */

public class SerialPortIO {
    private SerialPort serialPort;
    private SerialPortEventListener listener;

    public SerialPortIO(){
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


        serialPort = new SerialPort("COM4");
        try {
            //Open port
            serialPort.openPort();
            // you can also use this line - serialPort.setParams(9600, 8, 1, 0);
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                                 SerialPort.DATABITS_8,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_NONE);
            //Writes data to port


        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }

        listener = new SerialPortEventListener() {
            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                    return;
            }
        };

    }


    public void write(byte c) {
        try {
            serialPort.writeByte(c);
        }
        catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }
}




