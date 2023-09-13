package cu.havanaclub.ekinsadbreview.util;

import cu.havanaclub.ekinsadbreview.controller.PesajesLineaController;
import cu.havanaclub.ekinsadbreview.service.PesajesLineaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

/**
 * This class contains the scheduled task to search errors in tag and lotes every 1 day.
 *
 * @author Gabriel
 * @version 1.0.0
 */
@Component
public class ScheduleTask {

    @Autowired
    private PesajesLineaService pesajesLineaService;

    /**
     * Scheduled task to save the CSV file every day.
     *
     * @throws IOException This exception is thrown if it is impossible to write to the log file due to insufficient permissions.
     */
    @Scheduled(cron = "@daily")
    public void saveDronesBatteryStatus() throws IOException {
        long millis = Date.valueOf("2023-07-14").getTime();
        java.sql.Date startDate = new java.sql.Date(millis);
        java.sql.Date now = new java.sql.Date(System.currentTimeMillis());

        try {
            System.out.println("Detected " + pesajesLineaService.listAllPesajesWithLoteErrors(startDate).size() + " tags with errors in Lotes changes.");
//            System.out.println("Detected " + pesajesLineaService.listAllPesajesWithZoneErrorsAfterADate(startDate).size() + " tags with errors in zones.");
            System.out.println("Detected " + pesajesLineaService.listAllPesajesWithErrorsInZoneByDate(startDate, now).size() + " tags with errors in zones using the new API.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
