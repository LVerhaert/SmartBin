package smartbin;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.Afval;
import sensors.RFIDSensor;
import sensors.SerialConnector;

/**
 * Main klasse
 * @author Liza Verhaert
 */
public class SmartBin {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // Haal data uit de database en gebruik deze om de modelklassen te vullen
        Data data = new Data();
//        SerialConnector.execute();

//        while (true) {
            verwerkAfval(data);
//        }
        
        /**
         * Liza
         */
//        RFIDSensor.execute(115200);

        /**
         * Duygu
         */
//        KleurenSensor.execute();

        /**
         * Ketura
         */
//        GewichtSensor.execute();

    }

    /**
     * Werkt alleen met in de database bekende stukken afval.
     */
    private static void verwerkAfval(Data data) {
        int baknr = 0;
        String afvalType = "error";
        String bakType = "error";
        String chipnr = "";
        try {
            do {
                RFIDSensor.execute(115200);
                chipnr = RFIDSensor.getChipnr();
            } while (chipnr == "");
            
            Afval afval = data.getAfvalViaChipnr(chipnr);
            if (afval.getKleur().equals("(0, 0, 0)")) { // als er geen kleur nodig is
                afvalType = afval.getAfvaltype();
            } else { // als er wel een kleur nodig is
//                KleurenSensor.execute();
//                String kleur = KleurenSensor.getKleur();
//                afval = data.getAfvalViaKleur(kleur);
//                afvalType = afval.getAfvaltype();
            }
            bakType = data.getAfvalInWelkeBak(afvalType);
            baknr = data.getBak(bakType);
            System.out.println("Afval met type " + afvalType + " in bak #" + baknr + " met type " + bakType); // tijdelijk, om te kijken of het werkt
            
//            openBak(bakType); // vindt en opent de juiste bak op basis van baktype, zet lampjes om, etc.
//            sluitBak(); // sluit de bak die open is gegaan, na toename van gewicht, zet lampjes om, etc.
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        
        
    }
    
}
