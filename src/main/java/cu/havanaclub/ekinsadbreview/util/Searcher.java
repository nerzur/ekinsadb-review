package cu.havanaclub.ekinsadbreview.util;

import cu.havanaclub.ekinsadbreview.entity.EkPesajesLinea;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Builder
public class Searcher {

    String tag;
    Integer countEntries;
    List<Integer> zones;
    List<Integer> weigth;
    List<Date> dates;
    List<String> lotes;


    @Override
    public String toString() {
        return tag + "," + countEntries + "," + dates.toString() + "," + lotes.toString();
    }

    public static Map<String, Searcher> obtainSearcherMap(List<EkPesajesLinea> searcherList){
        Map<String, Searcher> pesajesLineaMap = new HashMap<>();

        for (EkPesajesLinea ekPesajesLinea : searcherList) {
            if (pesajesLineaMap.get(ekPesajesLinea.getTag()) == null) {
                List<Integer> zones = new ArrayList<>();
                zones.add(ekPesajesLinea.getIdZona());
                List<Integer> weigths = new ArrayList<>();
                weigths.add(ekPesajesLinea.getPeso());
                List<Date> dates = new ArrayList<>();
                dates.add(ekPesajesLinea.getFecha());
                List<String> lotes = new ArrayList<>();
                lotes.add(ekPesajesLinea.getNumeroLote());
                pesajesLineaMap.put(ekPesajesLinea.getTag(),
                        Searcher.builder()
                                .zones(zones)
                                .countEntries(1)
                                .weigth(weigths)
                                .dates(dates)
                                .lotes(lotes)
                                .tag(ekPesajesLinea.getTag())
                                .build()
                );
            } else {
                Searcher searched = pesajesLineaMap.get(ekPesajesLinea.getTag());
                searched.setCountEntries(searched.getCountEntries() + 1);
                List<Integer> zones = searched.getZones();
                zones.add(ekPesajesLinea.getIdZona());
                List<Integer> weigths = searched.getWeigth();
                weigths.add(ekPesajesLinea.getPeso());
                List<Date> dates = searched.getDates();
                dates.add(ekPesajesLinea.getFecha());
                List<String> lotes = searched.getLotes();
                lotes.add(ekPesajesLinea.getNumeroLote());
                searched.setZones(zones);
                searched.setWeigth(weigths);
                searched.setDates(dates);
                searched.setLotes(lotes);
                pesajesLineaMap.replace(ekPesajesLinea.getTag(), searched);
            }
        }
        return pesajesLineaMap;
    }
}
