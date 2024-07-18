package cu.havanaclub.ekinsadbreview.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Esta clase contiene funcionalidades que permiten realizar verificaciones de errores en las zonas y lotes de los pallets.
 * @author Nerzur
 */
public class Verifier {

    /**
     * Esta función realiza una búsqueda en una lista de @{@link Entry} para detectar inconsistencias en las zonas de los
     * registros teniendo en cuenta que por cada registro realizado que tiene como zona 1 deberá existir otro registro
     * que deberá tener como zona 2 y de igual forma con las zonas 3 y 4.
     * @param zones Lista de zonas que será procesada.
     * @deprecated Esta función no es funcional debido a que no encuentra todos los errores de entrada y salida.
     * @return Devuelve un <em>boolean</em> indicando si existen errores o no en el listado de zonas.
     */
    public static boolean verifyZone(List<Entry> zones) {
        if (zones.size() % 2 != 0)
            return false;
        AtomicInteger zonesSum = new AtomicInteger();
        zones.forEach(zone -> {
            zonesSum.addAndGet(zone.getZone());
        });
        //Verificar si los tags pasan por zonas que son las adecuadas 1,2,3 o 4
//        switch (zones.size()) {
//            case 2 -> {
//                if (zonesSum.get() != 3 && zonesSum.get() != 7)
//                    return false;
//            }
//            case 4 -> {
//                if (zonesSum.get() != 10)
//                    return false;
//            }
//            default -> {
//                return false;
//            }
//        }
        //Verificar si los tags tienen una entrada o salida irregular, o sea, entran y no salen en las lineas o viceversa.
        int line1 = 0, line2 = 0;
        for (Entry zone : zones) {
            switch (zone.getZone()) {
                case 1:
                    line1++;
                case 2:
                    line1--;
                case 3:
                    line2++;
                case 4:
                    line2--;
            }
        }
        return line1 != 0 || line2 != 0;
    }

    /**
     * Esta función analiza un objeto {@link Searcher}, específicamente en una lista de {@link Entry} que se encuentra
     * dentro de este objeto para detectar inconsistencias en los registros del lote referentes a los lotes. Se tiene en
     * cuenta si un Tag ha ingresado a la línea por una zona y ha salido de esta con otro lote.
     * @param searcher Objeto que será analizado y que contiene los registros del Tag.
     * @return Devuelve un <em>boolean</em> indicando si existen errores o no en el listado de lotes.
     */
    public static boolean verifyLote(Searcher searcher) {
        if (searcher.getEntriesList().size() % 2 != 0)
            return false;
        Map<String, Integer> lotesMap = new HashMap<>();
        for (int i = 0; i < searcher.entriesList.size(); i++) {
            switch (searcher.getEntriesList().get(i).getZone()) {
                case 1:
                case 3:
                    if (lotesMap.get(searcher.getEntriesList().get(i).getLote()) == null)
                        lotesMap.put(searcher.getEntriesList().get(i).getLote(), 1);
                    else
                        lotesMap.replace(searcher.getEntriesList().get(i).getLote(), lotesMap.get(searcher.getEntriesList().get(i).getLote()) + 1);
                    break;
                case 2:
                case 4:
                    if (lotesMap.get(searcher.getEntriesList().get(i).getLote()) == null)
                        lotesMap.put(searcher.getEntriesList().get(i).getLote(), -1);
                    else if (lotesMap.get(searcher.getEntriesList().get(i).getLote()) == 1)
                        lotesMap.remove(searcher.getEntriesList().get(i).getLote());
                    else
                        lotesMap.replace(searcher.getEntriesList().get(i).getLote(), lotesMap.get(searcher.getEntriesList().get(i).getLote()) - 1);
                    break;
            }
        }
        return lotesMap.values().isEmpty();
    }
}
