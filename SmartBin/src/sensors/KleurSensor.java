package sensors;

import gnu.io.SerialPortEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Subklasse van SerialConnector. Deze klasse werkt correct als het losgelaten
 * wordt op de input van de Kleursensor.
 *
 * @author Ketura Seedorf, adapted from Liza Verhaert
 */
public class KleurSensor extends SerialConnector {

    // de gewichtwaarde die ik uit de inputLine haal
    private static String kleurr = "";
    private static String kleurg = "";
    private static String kleurb = "";
    // reguliere expressie die nodig is om de waarde van het gewicht uit de inputLine te halen:
    // "R: 0 G:0 B:0 .." " is wat ik zoek, dus "R:(\\d)G:(\\d)B:(\\d)" één of meerdere keren
    // achter elkaar
    // Zie https://www.vogella.com/tutorials/JavaRegularExpressions/article.html
    private final String REGEX = "R:(\\d)G:(\\d)B:(\\d)";

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
                System.out.println(inputLine);
                Matcher matcher = pattern.matcher(inputLine); // laat de reguliere expressie los op de inputLine
                while (matcher.find()) { // zolang er matches gevonden worden..
                    kleurr = matcher.group(); // wil ik deze opslaan in de variabele kleurr
                    kleurg = matcher.group(); // wil ik deze opslaan in de variabele kleurg
                    kleurb = matcher.group(); // wil ik deze opslaan in de variabele kleurb
                    System.out.println("Kleur: R:" + kleurr + " G:" + kleurg + " B:" + kleurg); // en wil ik deze laten zien in de output
                }
            } catch (Exception e) {
                //System.err.println(e.toString());
            }
        }

    }

    public static void execute(int BAUD_RATE) throws Exception {
        KleurSensor main = new KleurSensor();
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

    public static String getKleurr() {
        return kleurr;
    }

    public static String getKleurg() {
        return kleurg;
    }

    public static String getKleurb() {
        return kleurb;
    }
}
