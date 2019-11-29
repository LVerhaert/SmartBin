package sensors;

import gnu.io.SerialPortEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sensors.SerialConnector;

/**
 * Subklasse van SerialConnector. Deze klasse werkt alleen correct als deze
 * losgelaten wordt op de input van de RFIDSensor.
 *
 * @author Ketura Seedorf, adapted from Liza Verhaert
 */
public class GewichtSensor extends SerialConnector {

    // het chipnummer dat ik uit de inputLine haal
    private String gewicht;
    // reguliere expressie die nodig is om het chipnummer uit de inputLine te halen:
    // "x01 x02 x03 ..." is wat ik zoek, dus "(x\d\d\s)" één of meerdere keren
    // achter elkaar
    // Zie https://www.vogella.com/tutorials/JavaRegularExpressions/article.html
    private final String REGEX = "-?(\\d)+.\\d g";

    /**
     * @Override serialEvent van de superklasse. Ik heb deze methode aangepast
     * zodat ik niet alleen de inputLine print, maar ook de gewenste data eruit
     * haal en opsla. De regels waar geen commentaar achter staat, zijn
     * onveranderd ten opzichte van de serialEvent-methode in de superklasse
     */
    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        // Opzetten van de pattern om de reguliere expressie te gebruiken
        Pattern pattern = Pattern.compile(REGEX);

        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine = input.readLine();
                Matcher matcher = pattern.matcher(inputLine); // laat de reguliere expressie los op de inputLine
                while (matcher.find()) { // zolang er matches gevonden worden..
                    gewicht = matcher.group(); // wil ik deze opslaan in de variabele 
                    System.out.println("Gewicht: " + gewicht); // en wil ik deze laten zien in de output
                }
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }

    }

    public static void execute() throws Exception {
        GewichtSensor main = new GewichtSensor();
        if (main.initialize()) {
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
}
