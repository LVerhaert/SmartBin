package sensors;

import gnu.io.SerialPortEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Subklasse van SerialConnector. Deze klasse werkt alleen correct als deze 
 * losgelaten wordt op de input van de RFIDSensor.
 * @author Liza Verhaert, adapted from Unknown
 */
public class RFIDSensor extends SerialConnector {

    // het chipnummer dat ik uit de inputLine haal
    private String chipnr;
    // reguliere expressie die nodig is om het chipnummer uit de inputLine te halen:
    // "x01 x02 x03 ..." is wat ik zoek, dus "(x\d\d\s)" één of meerdere keren
    // achter elkaar
    // Zie https://www.vogella.com/tutorials/JavaRegularExpressions/article.html
    private final String REGEX = "(x\\d\\d\\s)+";
    
    /**
     * @Override serialEvent van de superklasse. Ik heb deze methode aangepast 
     * zodat ik niet alleen de inputLine print, maar ook de gewenste data eruit 
     * haal en opsla.
     * De regels waar geen commentaar achter staat, zijn onveranderd ten opzichte
     * van de serialEvent-methode in de superklasse
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
                    chipnr = matcher.group(); // wil ik deze opslaan in de variabele chipnr
                    System.out.println("Chipnummer: " + chipnr); // en wil ik deze laten zien in de output
                 }
            } catch (Exception e) {
                System.err.println(e.toString());
            }
            
        }
        
    }
    
}
