package sensors;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author mechjesus
 */
public class SerialConnector implements SerialPortEventListener {

    // The port we're normally going to use.
    private static SerialPort serialPort;
    // Possible port names
    private static final String PORT_NAMES[] = {
        "/dev/cu.usbmodem1411", // Mac OS X
        "/dev/cu.usbmodem1421", // Mac OS X
        "COM5", // Windows
        "COM4", // Windows
        "COM3", // Windows
        "COM6", // Windows
    };

    // Standard baud rate
    protected static final int BAUD_RATE = 9600;
    // A BufferedReader which will be fed by an InputStreamReader converting the
    // bytes into characters making the displayed results codepage independent
    protected BufferedReader inputStream;
    // The output stream to the port
    protected static OutputStream outputStream;
    // Milliseconds to block while waiting for port open
    protected static final int TIME_OUT = 2000;

    public boolean initialize(int DATA_RATE) {

        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        // First, find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return false;
        }

        try {
            // Open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);

            // Set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            Thread.sleep(TIME_OUT);

            // Open the streams
            inputStream = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            outputStream = serialPort.getOutputStream();

            // Add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (PortInUseException e) {
            System.err.println(e.toString());
        } catch (IOException e) {
            System.err.println(e.toString());
        } catch (UnsupportedCommOperationException e) {
            System.err.println(e.toString());
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return true;
    }

    /**
     * This should be called when you stop using the port. This will prevent
     * port locking on platforms like Linux.
     */
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine = inputStream.readLine();
                System.out.println(inputLine);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }

    public static void receiveInput() throws Exception {
        SerialConnector main = new SerialConnector();
        if (main.initialize(BAUD_RATE)) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    // the following line will keep this app alive for 60 seconds,
                    // waiting for events to occur and responding to them (printing
                    // incoming messages to console).
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                    }
                }
            };
            t.start();
            System.out.println("Arduino -> Java started");
        }
    }

    public static void sendOutput(String outputMessage) {
        SerialConnector main = new SerialConnector();
        if (main.initialize(BAUD_RATE)) {
            System.out.println("Java -> Arduino started");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SerialConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                outputStream.write(outputMessage.getBytes());
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            System.out.println(outputMessage);
            try {
                outputStream.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
