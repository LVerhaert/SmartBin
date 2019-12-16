package smartbin;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.Afval;
import model.Bak;
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
//        SerialConnector.execute();
        verwerkAfval(data);
        //      while (true) {
        // verwerkAfval(data);
        //      }
        /**
         * Liza
         */
        //       RFIDSensor.execute(115200);
        /**
         * Duygu
         */
        //       KleurSensor.execute(9600);
        /**
         * Ketura
         */
        //       GewichtSensor.execute(9600);
    }

    /**
     * Werkt alleen met in de database bekende stukken afval. Work in progress..
     */
    private static void verwerkAfval(Data data) {
        int baknr = 0;
        String afvaltype = "error";
        String baktype = "error";
        String chipnr = "";
        String dekselpos = "";
        double gewicht = 0.0;

        try {
            RFIDSensor.execute(115200);
            do {
                Thread.sleep(1000); // geen idee waarom dit werkt, maar zet dit
                // vrolijk overal tussen als je ergens anders een probleem hebt
                chipnr = RFIDSensor.getChipnr();
            } while (chipnr == "");

            Afval afval = data.getAfvalViaChipnr(chipnr);
//            if (afval.getKleur().equals("(0, 0, 0)")) { // als er geen kleur nodig is
            afvaltype = afval.getAfvaltype();
//            } else { // als er wel een kleur nodig is
//                KleurenSensor.execute();
//                String kleur = KleurenSensor.getKleur();
//                afval = data.getAfvalViaKleur(kleur);
//                afvaltype = afval.getAfvaltype();
//            }
            baktype = data.getAfvalInWelkeBak(afvaltype);
            baknr = data.getBak(baktype);
            System.out.println("Afval met type " + afvaltype + " in bak #" + baknr + " met type " + baktype); // tijdelijk, om te kijken of het werkt         

            //           openBak(baktype); // vindt en opent de juiste bak op basis van baktype, zet lampjes om, etc.
            gewicht = GewichtSensor.getGetal();
            if (gewicht++ < 0.5) {
                System.out.println("De prullenbak is leeg.");

            } else {
                System.out.println("Gewicht: " + gewicht); // en deze laten zien in de output
                //                    sluitBak(); // sluit de bak die open is gegaan, na toename van gewicht, zet lampjes om, etc.
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

    }

}
