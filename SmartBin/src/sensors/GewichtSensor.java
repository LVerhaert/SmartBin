package sensors;

import gnu.io.SerialPortEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Subklasse van SerialConnector. Deze klasse werkt correct in combinatie met de
 * input van de Gewichtsensor.
 *
 * @author Ketura Seedorf, adapted from Liza Verhaert
 */
public class GewichtSensor extends SerialConnector {

    // de gewichtwaarde die ik uit de inputLine haal
    private static String gewicht = "";
    private static Double getal;
    // reguliere expressie die nodig is om de waarde van het gewicht uit de inputLine te halen:
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
                System.out.println("Input: " + inputLine);
                Matcher matcher = pattern.matcher(inputLine); // laat de reguliere expressie los op de inputLine
                while (matcher.find()) { // zolang er matches gevonden worden..
                    gewicht = matcher.group(); // wil ik deze opslaan in de variabele 
                    gewicht = gewicht.substring(0, gewicht.length() - 2); // de twee laatste waarden van de string weghalen
                    getal = Double.parseDouble(gewicht); // de string omzetten in een double
                    System.out.println("Gewicht: " + getal); // en deze laten zien in de output
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
