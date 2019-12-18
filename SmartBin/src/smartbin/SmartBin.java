package smartbin;

import model.Afval;
import sensors.GewichtSensor;
import sensors.KleurSensor;
import sensors.RFIDSensor;
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
        
//        SerialConnector.receiveInput();
//        while (true) {
//             verwerkAfval(data);
//        }
        SerialConnector.sendOutput("gewicht1END");
//        GewichtSensor.receiveInput();
        /**
         * Liza
         */
//        RFIDSensor.receiveInput();
        /**
         * Duygu
         */
//        KleurSensor.receiveInput();
        /**
         * Ketura
         */
//        GewichtSensor.receiveInput();
    }

    /**
     * Werkt alleen met in de database bekende stukken afval. Work in progress..
     */
    private static void verwerkAfval(Data data) {
        int baknr = 0;
        String afvaltype = "error";
        String baktype = "error";
        String chipnr = "";
        try {
            RFIDSensor.receiveInput();
            do {
                Thread.sleep(1000); // geen idee waarom dit werkt, maar zet dit
                // vrolijk overal tussen als je ergens anders een probleem hebt
                chipnr = RFIDSensor.getChipnr();
            } while (chipnr.isEmpty());

            Afval afval = data.getAfvalViaChipnr(chipnr);
//            if (afval.getKleur().equals("(0, 0, 0)")) { // als er geen kleur nodig is
                afvaltype = afval.getAfvaltype();
//            } else { // als er wel een kleur nodig is
//                KleurenSensor.receiveInput();
//                String kleur = KleurenSensor.getKleur();
//                afval = data.getAfvalViaKleur(kleur);
//                afvaltype = afval.getAfvaltype();
//            }
            baktype = data.getAfvalInWelkeBak(afvaltype);
            baknr = data.getBak(baktype);
            System.out.println("Afval met type " + afvaltype + " in bak #" + baknr + " met type " + baktype); // tijdelijk, om te kijken of het werkt

//            openBak(bakType); // vindt en opent de juiste bak op basis van baktype, zet lampjes om, etc.
//            sluitBak(); // sluit de bak die open is gegaan, na toename van gewicht, zet lampjes om, etc.
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

    }

}
