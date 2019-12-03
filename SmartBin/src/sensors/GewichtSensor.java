package sensors;

import gnu.io.SerialPortEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Subklasse van SerialConnector. Deze klasse werkt correct als het losgelaten
 * wordt op de input van de Gewichtsensor.
 *
 * @author Ketura Seedorf, adapted from Liza Verhaert
 */
public class GewichtSensor extends SerialConnector {

    // de gewichtwaarde die ik uit de inputLine haal
    private static String gewicht = "";
    // reguliere expressie die nodig is om de waarde van het gewicht uit de inputLine te halen:
    // "0.0 g " is wat ik zoek, dus "-?(\\d)+.\\d g" één of meerdere keren
    // achter elkaar
    // Zie https://www.vogella.com/tutorials/JavaRegularExpressions/article.html
    private final String REGEX = "-?(\\d)+.\\d g";

    /**
     * @Override serialEvent van de superklasse. Ik heb deze methode aangepast
     * omdat ik alleen de gewenste data uit de input haal en opsla. De regels
     * waar geen commentaar achter staat, zijn onveranderd ten opzichte van de
     * serialEvent-methode in de superklasse
     */
    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        // Opzetten van de pattern om de reguliere expressie te gebruiken
        Pattern pattern = Pattern.compile(REGEX);

        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine = input.readLine();
                //System.out.println(inputLine);
                Matcher matcher = pattern.matcher(inputLine); // laat de reguliere expressie los op de inputLine
                while (matcher.find()) { // zolang er matches gevonden worden..
                    gewicht = matcher.group(); // wil ik deze opslaan in de variabele 
                    System.out.println("Gewicht: " + gewicht); // en wil ik deze laten zien in de output
                }
            } catch (Exception e) {
                //System.err.println(e.toString());
            }
        }

    }

    public static void execute(int BAUD_RATE) throws Exception {
        GewichtSensor main = new GewichtSensor();
        if (main.initialize(BAUD_RATE)) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    // the following line will keep this app alive for 1000 seconds,
                    // waiting for events to occur and responding to them (printing
                    // incoming messages to console).
                    try {
                        Thread.sleep(1000000);
                    } catch (InterruptedException ie) {
                        System.err.println(ie.toString());
                    }
                }
            };
            t.start();
            System.out.println("Started");
        }
    }

    public static String getGewicht() {
        return gewicht;
    }

}
