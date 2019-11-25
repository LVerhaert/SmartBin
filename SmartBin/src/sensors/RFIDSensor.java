/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sensors;

import gnu.io.SerialPortEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Liza Verhaert
 */
public class RFIDSensor extends SerialConnector {

    private String chipnr;
    private final String REGEX = "(x\\d\\d\\s)+";
    
    /**
     * Handle an event on the serial port. Read the data and print it.
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        Pattern pattern = Pattern.compile(REGEX);
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine = input.readLine();
                System.out.println(inputLine);
                Matcher matcher = pattern.matcher(inputLine);
                while (matcher.find()) {
                    chipnr = matcher.group();
                    System.out.println("Chipnummer: " + chipnr);
                 }
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
        // Ignore all the other eventTypes, but you should consider the other ones.
    }
    
    public static void execute() throws Exception {
        RFIDSensor main = new RFIDSensor();
        main.initialize();
        Thread t = new Thread() {
            public void run() {
                // the following line will keep this app alive for 1000 seconds,
                // waiting for events to occur and responding to them (printing
                // incoming messages to console).
                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException ie) {
                }
            }
        };
        t.start();
        System.out.println("Started");
    }
    
}
