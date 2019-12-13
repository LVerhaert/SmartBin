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

    private static SerialPort serialPort;
    // The port we're normally going to use.
    private static final String PORT_NAMES[] = {
        "/dev/cu.usbmodem1411", // Mac OS X
        "/dev/cu.usbmodem1421",// Mac OS X
        "COM5", // Windows
        "COM4", // Windows
        "COM3", // Windows
        "COM6",};
    // A BufferedReader which will be fed by an InputStreamReader converting the
    // bytes into characters making the displayed results codepage independent
    protected BufferedReader input;
    // The output stream to the port
    private OutputStream output;
    // Milliseconds to block while waiting for port open
    private static final int TIME_OUT = 2000;
    // Default bits per second for COM port

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

            // Open the streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            // Add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
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
                String inputLine = input.readLine();
                System.out.println(inputLine);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }

    public static void execute(int BAUD_RATE) throws Exception {
        SerialConnector main = new SerialConnector();
        if (main.initialize(BAUD_RATE)) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    // the following line will keep this app alive for 1000 seconds,
                    // waiting for events to occur and responding to them (printing
                    // incoming messages to console).
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ie) {
                    }
                }
            };
            t.start();
            System.out.println("Started");
        }
    }

    private static Enumeration portList;
    private static CommPortIdentifier portId;
    private static String messageString = "color FF00FFEND";
    private static OutputStream outputStream;

    public static void executeOutput(int BAUD_RATE) {
        portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {

            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {

                if (portId.getName().equals("COM3")) {

                    try {
                        serialPort = (SerialPort) portId.open("SimpleWriteApp", 2000);

                        serialPort.setSerialPortParams(9600,
                                SerialPort.DATABITS_8,
                                SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);
                        Thread.sleep(4000);

                        outputStream = serialPort.getOutputStream();
                        Thread.sleep(4000);
                        


                        outputStream.write(messageString.getBytes());
                        System.out.println(messageString);

                        outputStream.close();
                        serialPort.close();
                    } catch (IOException e) {
                        System.out.println("err3");
                    } catch (PortInUseException e) {
                        System.out.println("err");
                    } catch (UnsupportedCommOperationException e) {
                        System.out.println("err2");
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SerialConnector.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
}
