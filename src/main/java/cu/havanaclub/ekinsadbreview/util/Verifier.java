package cu.havanaclub.ekinsadbreview.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Verifier {

    public static boolean verifyZone(List<Integer> zones) {
        if (zones.size() % 2 != 0)
            return false;
        AtomicInteger zonesSum = new AtomicInteger();
        zones.forEach(zonesSum::addAndGet);
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
        for (Integer zone : zones) {
            switch (zone) {
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
        if (searcher.getLotes().size() % 2 != 0)
            return true;
        Map<String, Integer> lotesMap = new HashMap<>();
        for (int i = 0; i < searcher.getZones().size(); i++) {
            switch (searcher.getZones().get(i)) {
                case 1:
                case 3:
                    if (lotesMap.get(searcher.getLotes().get(i)) == null)
                        lotesMap.put(searcher.getLotes().get(i), 1);
                    else
                        lotesMap.replace(searcher.getLotes().get(i), lotesMap.get(searcher.getLotes().get(i)) + 1);
                    break;
                case 2:
                case 4:
                    if (lotesMap.get(searcher.getLotes().get(i)) == null)
                        lotesMap.put(searcher.getLotes().get(i), -1);
                    else if (lotesMap.get(searcher.getLotes().get(i)) == 1)
                        lotesMap.remove(searcher.getLotes().get(i));
                    else
                        lotesMap.replace(searcher.getLotes().get(i), lotesMap.get(searcher.getLotes().get(i)) - 1);
                    break;

            }
        }
        return lotesMap.values().isEmpty();
    }
}
