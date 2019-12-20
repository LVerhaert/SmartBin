package sensors;

import gnu.io.SerialPortEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static javax.sound.sampled.LineEvent.Type.STOP;

/**
 * Subklasse van SerialConnector. Deze klasse werkt correct als het losgelaten
 * wordt op de inputStream van de Gewichtsensor.
 *
 * @author Ketura Seedorf, adapted from Liza Verhaert
 */
public class GewichtSensor extends SerialConnector {

    // de gewichtwaarde die ik uit de inputLine haal
    private static String gewichtString = "";
    private static Double gewicht = 0.0;
    // reguliere expressie die nodig is om de waarde van het gewicht uit de inputLine te halen:
    // "0.0 g " is wat ik zoek, dus "-?(\\d)+.\\d g" één of meerdere keren
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
                String inputLine = inputStream.readLine();
                Thread.sleep(500);
                System.out.println("Input: " + inputLine);
                Matcher matcher = pattern.matcher(inputLine); // laat de reguliere expressie los op de inputLine
                while (matcher.find()) { // zolang er matches gevonden worden..
                    gewichtString = matcher.group(); // wil ik deze opslaan in de variabele 
                    gewichtString = gewichtString.substring(0, gewichtString.length() - 2); // de twee laatste waarden van de string weghalen
                    gewicht = Double.parseDouble(gewichtString); // de string omzetten in een double
                    System.out.println("Gewicht: " + gewicht + "g"); // deze laten zien in de output
                    if (gewicht > 15.0) { // zodra de waarde groter is dan 15
                        close(); // de gewichtsensor stoppen
                        System.out.println("Gewicht is toegenomen.");
                    }
                }
            } catch (Exception e) {
                //System.err.println(e.toString());
            }
        }

    }

    public static void receiveInput() throws Exception {
        GewichtSensor main = new GewichtSensor();
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
            System.out.println("Gewichtsensor Arduino -> Java started");
        }
    }

    public static Double getGewicht() {
        return gewicht;
    }

}
