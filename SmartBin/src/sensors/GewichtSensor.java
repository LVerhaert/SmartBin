package sensors;

import gnu.io.SerialPortEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Subklasse van SerialConnector. Deze klasse werkt correct als het losgelaten
 * wordt op de inputStream van de Gewichtsensor.
 *
 * @author Ketura Seedorf, adapted from Liza Verhaert
 */
public class GewichtSensor extends SerialConnector {

    // de gewichtwaarde die ik uit de inputLine haal
    private static String stringGewicht = "";
    private static Double gewicht;
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
                System.out.println("Input: " + inputLine);
                Matcher matcher = pattern.matcher(inputLine); // laat de reguliere expressie los op de inputLine
                while (matcher.find()) { // zolang er matches gevonden worden..
                    stringGewicht = matcher.group(); // wil ik deze opslaan in de variabele 
                    stringGewicht = stringGewicht.substring(0, stringGewicht.length() - 2); // de twee laatste waarden van de string weghalen
                    gewicht = Double.parseDouble(stringGewicht); // de string omzetten in een double
                    System.out.println("Gewicht: " + gewicht); // deze laten zien in de output

                    if (gewicht > 15.0) {
                        close();
                        System.out.println("Gewicht toegenomen.");
                    }

                }
            } catch (Exception e) {
                System.err.println(e.toString());
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

    public static void sendOutput(String outputMessage) {
        GewichtSensor main = new GewichtSensor();
        if (main.initialize(BAUD_RATE)) {
            System.out.println("Java -> Gewichtsensor Arduino started");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(GewichtSensor.class.getName()).log(Level.SEVERE, null, ex);
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
