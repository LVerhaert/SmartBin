package sensors;

import gnu.io.SerialPortEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Subklasse van SerialConnector. Deze klasse werkt correct als het losgelaten
 wordt op de inputStream van de Kleursensor.
 *
 * @author Ketura Seedorf, adapted from Liza Verhaert
 */
public class KleurSensor extends SerialConnector {

    // de gewichtwaarde die ik uit de inputLine haal
    private static String kleurr = "";
    private static String kleurg = "";
    private static String kleurb = "";
    private static int rood;
    private static int groen;
    private static int blauw;
    // reguliere expressie die nodig is om de waarde van het gewicht uit de inputLine te halen:
    // "R: 0 G:0 B:0 .." " is wat ik zoek, dus "R:(\\d)G:(\\d)B:(\\d)" één of meerdere keren
    // achter elkaar
    // Zie https://www.vogella.com/tutorials/JavaRegularExpressions/article.html
    private final String REGEX = "R:\\d+,G:\\d+,B:\\d+";
    private final String REGEXR = "R:\\d+";
    private final String REGEXG = "G:\\d+";
    private final String REGEXB = "B:\\d+";

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
                //System.out.println("inputStream: " + inputLine);
                Pattern patternR = Pattern.compile(REGEXR);
                Pattern patternG = Pattern.compile(REGEXG);
                Pattern patternB = Pattern.compile(REGEXB);

                Matcher matcher = pattern.matcher(inputLine);

                while (matcher.find()) { // zolang er matches gevonden worden..
                    Matcher matcherr = patternR.matcher(inputLine);
                    Matcher matcherg = patternG.matcher(inputLine);
                    Matcher matcherb = patternB.matcher(inputLine);

                    while (matcherr.find()) {
                        kleurr = matcherr.group(); // wil ik deze opslaan in de variabele kleurr    
                        kleurr = kleurr.substring(2);
                        rood = Integer.parseInt(kleurr);
                        
                    }
                    while (matcherg.find()) {
                        kleurg = matcherg.group(); // wil ik deze opslaan in de variabele kleurr    
                        kleurg = kleurg.substring(2);  //je begint op positie 2.
                        groen = Integer.parseInt(kleurg);
                    }
                    while (matcherb.find()) {
                        kleurb = matcherb.group(); // wil ik deze opslaan in de variabele kleurr   
                        kleurb = kleurb.substring(2);
                        blauw = Integer.parseInt(kleurb);
                    }

                    System.out.println("Kleur: R:" + rood + " G:" + groen + " B:" + blauw); // en wil ik deze laten zien in de output
                }
            } catch (Exception e) {
                //System.err.println(e.toString());
            }
        }

    }

    public static void receiveInput() throws Exception {
        KleurSensor main = new KleurSensor();
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
            System.out.println("Kleursensor Arduino -> Java started");
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
