package smartbin;

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
        SerialConnector.execute();

        /**
         * Liza
         */
//        RFIDSensor.execute();

        /**
         * Duygu
         */
//        KleurenSensor.execute();

        /**
         * Ketura
         */
//        GewichtSensor.execute();

    }
    
}
