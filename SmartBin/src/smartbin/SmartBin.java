package smartbin;

import sensors.SerialConnector;

/**
 * Main klasse
 *
 * @author Liza Verhaert
 */
public class SmartBin {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // Haal data uit de database en gebruik deze om de modelklassen te vullen
        Data data = new Data();

        /* Stuur een commando naar de arduino
        Mogelijke commando's:
            gewicht1END     haal de gewichtwaarden van gewichtsensor 1 op
            gewichtnEND     haal de gewichtwaarden van gewichtsensor n op
            open1END        open deksel van bak 1
            opennEND        open deksel van bak n
            dicht1END       sluit deksel van bak 1
            dichtnEND       sluit deksel van bak n
            rfidEND         haal de waarden van de RFID-sensor op
            kleurEND        haal de waarden van de kleursensor op
            stopEND         zet de RFID-, kleur- en gewichtinformatiestroom stop
         */
//        SerialConnector.sendOutput("open1END");
//        SerialConnector.sendOutput("dicht1END");

        // De uiteindelijke functie die het hele programma gaat uitvoeren!
//        while (true) {
            if (SerialConnector.afvalSysteemGereed()) {
                SerialConnector.verwerkAfval(data);
            }
//        }

    }
}
