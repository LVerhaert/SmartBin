package smartbin;

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
//        Data data = new Data();
        
        // De uiteindelijke functie die het hele programma gaat uitvoeren!
//        while (true) {
 //       verwerkAfval(data);
//        }

        /* Stuur een commando naar de arduino
        Op dit moment mogelijke commando's:
            gewicht1END     zet gewichtsensor 1 aan en geeft gewichtwaarden terug
            gewicht2END     zet gewichtsensor 2 aan en geeft gewichtwaarden terug
            gewicht3END     zet gewichtsensor 3 aan en geeft gewichtwaarden terug
            gewicht4END     zet gewichtsensor 4 aan en geeft gewichtwaarden terug
        */
//        SerialConnector.sendOutput("gewicht1END");
        GewichtSensor.sendOutput("gewicht1END");

        // Ontvang input van het gewichtsensorprogramma
//        GewichtSensor.receiveInput();
        // Ontvang input van het chipsensorprogramma
//        RFIDSensor.receiveInput();
        // Ontvang input van het kleursensorprogramma
//        KleurSensor.receiveInput();
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
            
            // Vind uit wat het chipnummer (de materiaalsoort dus) is
            RFIDSensor.receiveInput();
            do {
                Thread.sleep(1000);
                chipnr = RFIDSensor.getChipnr();
            } while (chipnr.isEmpty());
            Afval afval = data.getAfvalViaChipnr(chipnr);

            // Als het materiaal "glas" is, moet worden gekeken of het wit of
            // gekleurd glas is
//            while (afval.getAfvaltype().equals("glas")) {
//                KleurSensor.receiveInput();
//                Thread.sleep(1000);
//                if (KleurSensor.isWit()) {
//                    afval.setAfvaltype("glas wit");
//                } else if (KleurSensor.isKleur()) {
//                    afval.setAfvaltype("glas kleur");
//                }
//            }
            
            baktype = data.getAfvalInWelkeBak(afvaltype); // zoek in welk baktype dit afvaltype moet
            baknr = data.getBak(baktype); // zoek welke bak dit baktype heeft
            System.out.println("Afval met type " + afvaltype + " in bak #" + baknr + " met type " + baktype); // tijdelijk, om te kijken of het werkt

           //            openBak(bakType); // vindt en opent de juiste bak op basis van baktype, zet lampjes om, etc.
//           GewichtSensor.receiveInput();
//            Thread.sleep(1000);
//            if (gewicht > 15.0) {
//                System.out.println("Gewicht toegenomen.");
////            sluitBak(); // sluit de bak die open is gegaan, na toename van gewicht, zet lampjes om, etc.
//            }
 

            SerialConnector.sendOutput("open" + baknr + "END"); // open de juiste bak
            SerialConnector.sendOutput("gewicht" + baknr + "END"); // zet de juiste gewichtsensor aan
            SerialConnector.sendOutput("sluit" + baknr + "END"); // sluit de juiste bak
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

    }

}
