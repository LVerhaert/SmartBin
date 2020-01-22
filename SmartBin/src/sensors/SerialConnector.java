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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Afval;
import smartbin.Data;

/**
 * @author mechjesus, edited by Liza Verhaert, Ketura Seedorf, Duygu Tas
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

    private static double gewicht;
    private static String chipnr = "";
    private static int rood;
    private static int groen;
    private static int blauw;
    private static int startRood;
    private static int startGroen;
    private static int startBlauw;
    private static boolean isWit = false;
    private static boolean isKleur = false;
    private static boolean afvalVerwerkt = true;

    // Standard baud rate
    protected static final int BAUD_RATE = 9600;
    // A BufferedReader which will be fed by an InputStreamReader converting the
    // bytes into characters making the displayed results codepage independent
    protected BufferedReader inputStream;
    // The output stream to the port
    protected static OutputStream outputStream;
    // Wait-time
    protected static final int TIME_OUT = 1000;

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
            // Open serial port
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
  //          System.err.println(e.toString());
        } catch (IOException e) {
//            System.err.println(e.toString());
        } catch (UnsupportedCommOperationException e) {
            System.err.println("5" + e.toString());
        } catch (Exception e) {
            System.err.println("3" + e.toString());
        }
        return true;
    }

    /**
     * This should be called when you stop using the port. This will prevent
     * port locking on platforms like Linux.
     */
    public static synchronized void close() {
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
        try {
            if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                try {
                    String inputLine = inputStream.readLine();
                    if (inputLine.startsWith("g")) {
                        processGewicht(inputLine.substring(1));
                    } else if (inputLine.startsWith("r")) {
                        processRFID(inputLine.substring(1));
                    } else if (inputLine.startsWith("k")) {
                        processKleur(inputLine.substring(1));
//                        System.out.println(inputLine);
                    } else {
                        System.out.println("    ARDUINO: " + inputLine);
                    }
                } catch (Exception e) {
  //                  System.err.println(e.toString());
                }
            }
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(SerialConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void sendOutput(String outputMessage) {
        SerialConnector main = new SerialConnector();
        if (main.initialize(BAUD_RATE)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SerialConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                outputStream.write(outputMessage.getBytes());
            } catch (IOException e) {
                System.err.println("1" + e.getMessage());
            }
            System.out.print("Sending \"" + outputMessage + "\"...     ");

            try {
                outputStream.close();
                System.out.println("Message sent.");
            } catch (IOException e) {
                System.out.println();
                System.err.println("2" + e.getMessage());
            }

        }
    }

    private void processGewicht(String inputLine) {
        String REGEX = "-?(\\d)+.\\d g";
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(inputLine); // laat de reguliere expressie los op de inputLine
        while (matcher.find()) { // zolang er matches gevonden worden..
            String gewichtString = matcher.group(); // wil ik deze opslaan in de variabele gewicht
            gewichtString = gewichtString.substring(0, gewichtString.length() - 2); // haal ik de twee laatste waarden weg
            gewicht = Double.parseDouble(gewichtString); // zet ik de string om in een double
            System.out.println("Gewicht: " + gewicht); // en wil ik deze laten zien in de output
        }
    }

    public static boolean isGewichtToegenomen() { 
        return (gewicht >= 18.0);
    }

    private void processRFID(String inputLine) {
        String REGEX = "(\\s0x[\\d\\w][\\d\\w])+";
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(inputLine); // laat de reguliere expressie los op de inputLine
        while (matcher.find()) { // zolang er matches gevonden worden..
            chipnr = matcher.group(); // wil ik deze opslaan in de variabele chipnr
            chipnr = chipnr.substring(1); // haal ik de eerste spatie weg
            System.out.println("Chipnummer: " + chipnr); // en wil ik deze laten zien in de output
        }

    }

    private void processKleur(String inputLine) {
        String REGEX = "R:\\d+,G:\\d+,B:\\d+";
        String REGEXR = "R:\\d+";
        String REGEXG = "G:\\d+";
        String REGEXB = "B:\\d+";
        String REGEXSTART = "startwaarde R:\\d+,G:\\d+,B:\\d+";
        Pattern pattern = Pattern.compile(REGEX);
        Pattern patternR = Pattern.compile(REGEXR);
        Pattern patternG = Pattern.compile(REGEXG);
        Pattern patternB = Pattern.compile(REGEXB);
        Pattern patternStart = Pattern.compile(REGEXSTART);
        Matcher matcher = pattern.matcher(inputLine); // laat de reguliere expressie los op de inputLine
        Matcher matcherR = patternR.matcher(inputLine);
        Matcher matcherG = patternG.matcher(inputLine);
        Matcher matcherB = patternB.matcher(inputLine);
        Matcher matcherstart = patternStart.matcher(inputLine);
        while (matcherstart.find()) { 

            while (matcherR.find()) { // zolang er matches gevonden worden...
                String startR = matcherR.group(); // wil ik deze opslaan in de variabele startR    
                startRood = Integer.parseInt(startR.substring(2)); // haal ik 2 waarden weg

            }
            while (matcherG.find()) {
                String startG = matcherG.group();
                startGroen = Integer.parseInt(startG.substring(2));

            }
            while (matcherB.find()) {
                String startB = matcherB.group();  
                startBlauw = Integer.parseInt(startB.substring(2)); 

            }

        }
        while (matcher.find()) {
            while (matcherR.find()) {
                String roodString = matcherR.group();
                rood = Integer.parseInt(roodString.substring(2));
            }
            while (matcherG.find()) {
                String groenString = matcherG.group();
                groen = Integer.parseInt(groenString.substring(2));
            }
            while (matcherB.find()) {
                String blauwString = matcherB.group();
                blauw = Integer.parseInt(blauwString.substring(2));
            }
        }
        System.out.println(inputLine);
    }

    public static boolean isWit() { // bepaalt wanneer een glas afvalitem wit is
        if (rood <= startRood - 8 && rood >= startRood - 40
                && groen >= startGroen - 5 && groen <= startGroen + 25
                && blauw >= startBlauw + 5 && blauw <= startBlauw + 30) {
            return true;
        }
        return false;
    }

    public static boolean isKleur() { // bepaalt wanneer een glas afvalitem kleur is
        if ((rood <= startRood - 28 || rood >= startRood + 40
                && groen <= startGroen - 25 || groen >= startGroen + 5
                && blauw <= startBlauw || blauw >= startBlauw + 35)) {
            return true;

        }
        return false;
    }

    /**
     * Werkt alleen met in de database bekende stukken afval
     */
    public static void verwerkAfval(Data data) {
        afvalVerwerkt = false; // bezig met deze functie (nodig ivm multithreading)

        sendOutput("rfidEND"); // zet RFID-informatiestroom aan
        while (chipnr.isEmpty()) { // wacht op het signaal
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(SerialConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        sendOutput("stopEND"); // zet RFID-informatiestroom uit

        Afval afval = data.getAfvalViaChipnr(chipnr);

        // Als het materiaal "glas" is, moet worden gekeken of het wit of
        // gekleurd glas is
        if (afval.getAfvaltype().equals("glas")) {
            sendOutput("kleurEND");
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SerialConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (afval.getAfvaltype().equals("glas")) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SerialConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (isWit()) {
                afval.setAfvaltype("glas wit");
                sendOutput("stopEND");
            } else if (isKleur()) {
                afval.setAfvaltype("glas kleur");
                sendOutput("stopEND");
            }
             System.out.println("Kleur: R:" + rood + " G:" + groen + " B:" + blauw); // en wil ik deze laten zien in de output
             System.out.println("Startkleur: R:" + startRood + " G:" + startGroen + " B:" + startBlauw); // en wil ik deze laten zien in de output
             
        }

        sendOutput("stopEND"); // zet RFID-informatiestroom uit

        String afvaltype = afval.getAfvaltype(); // zoek afvaltype
        String baktype = data.getAfvalInWelkeBak(afvaltype); // zoek in welk baktype dit afvaltype moet
        int baknr = data.getBak(baktype); // zoek welke bak dit baktype heeft
        System.out.println("Afval met type " + afvaltype + " in bak #" + baknr + " met type " + baktype);
        sendOutput("gewicht" + baknr + "END"); // zet de informatiestroom van de juiste gewichtsensor aan
        sendOutput("open" + baknr + "END"); // open de juiste bak

        while (!isGewichtToegenomen()) { // wacht op het signaal
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(SerialConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        sendOutput("stopEND"); // zet de gewichtinformatiestroom uit
        sendOutput("dicht" + baknr + "END"); // sluit de juiste bak
        try { // geef de Arduino de tijd om het deksel helemaal te sluiten
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SerialConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        data.voegAfvalToeAanBak(baknr);

        System.out.println("Afval werwerkt!"); // klaar!

        chipnr = ""; // reset chipnr zodat een ander afvalitem gescant kan worden
        gewicht = 0.0; // reset gewicht 
        if (afvaltype.startsWith("glas")) {
            afval.setAfvaltype("glas");
            
        }
        
        afvalVerwerkt = true; // klaar met deze functie (nodig ivm multithreading)

    }

    public static boolean afvalSysteemGereed() {
        return afvalVerwerkt;

    }
}
