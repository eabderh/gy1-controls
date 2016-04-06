/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elias.test;


import jssc.*;
import jssc.SerialPortEventListener;

/**
 *
 * @author EABDERH
 */

public class SimpleRead {
    private SerialPort serialPort;
    private SerialPortEventListener listener;

    public SimpleRead(){
        String[] portNames = SerialPortList.getPortNames();

        if (portNames.length == 0) {
            System.out.println("There are no serial-ports :( You can use an emulator, such ad VSPE, to create a virtual serial port.");
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
            //We expose the settings. You can also use this line - serialPort.setParams(9600, 8, 1, 0);
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




