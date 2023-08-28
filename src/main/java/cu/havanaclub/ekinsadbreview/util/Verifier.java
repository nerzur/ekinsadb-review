package cu.havanaclub.ekinsadbreview.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Verifier {

    public static boolean verifyZone(List<Entry> zones) {
        if (zones.size() % 2 != 0)
            return false;
        AtomicInteger zonesSum = new AtomicInteger();
        zones.forEach(zone -> {
            zonesSum.addAndGet(zone.getZone());
        });
        //Verificar si los tags pasan por zonas que son las adecuadas 1,2,3 o 4
        switch (zones.size()) {
            case 2 -> {
                if (zonesSum.get() != 3 && zonesSum.get() != 7)
                    return false;
            }
            case 4 -> {
                if (zonesSum.get() != 10)
                    return false;
            }
            default -> {
                return false;
            }
        }
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

    public static boolean verifyLote(Searcher searcher) {
        if (searcher.getEntriesList().size() % 2 != 0)
            return true;
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
