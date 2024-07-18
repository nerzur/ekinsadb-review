package cu.havanaclub.ekinsadbreview.util;

import cu.havanaclub.ekinsadbreview.entity.EkPesajesLinea;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Esta clase contiene la función de convertir los registros RAW de los pallets en registros agrupados por Tag. Además,
 * contiene la función <em>ToStringFile</em> que permite obtener un <em>String</em> con para escribirlo en el fichero
 * <em>.csv</em> correctamente.
 * @author Nerzur
 */
@Data
@AllArgsConstructor
@Builder
public class Searcher {

    /**
     * Tag del registro.
     */
    String tag;

    /**
     * Cantidad de registros asociados al tag.
     */
    Integer countEntries;

    /**
     * Listado de registros asociados al tag.
     */
    List<Entry> entriesList;

    @Override
    public String toString() {
        return toStringFile();
    }

    /**
     * Esta función se encarga de obtener un <em>String</em> con el formato adecuado para ser escrito en un fichero
     * <em>.csv</em>
     * @return Devuelve el <em>String</em> formateado para ser escrito en un fichero <em>.csv</em>
     */
    public String toStringFile() {
        StringBuilder text = new StringBuilder(tag + "," + countEntries + " entries\n");
        StringBuilder line = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        for (int i = 0; i < countEntries; i++) {
            String formattedDate = formatter.format(entriesList.get(i).date);
            line.append(",").append(tag).append(",").append(formattedDate).append(",").append(entriesList.get(i).lote).append(",").append(entriesList.get(i).invoice).append(",").append(entriesList.get(i).weigth).append(",").append(entriesList.get(i).zone).append("\n");
            text.append(line);
            line = new StringBuilder();
        }
        return text.toString();
    }

    /**
     * Esta función permite obtener un {@link Map} que contiene una agrupación de los registros por
     * Tag, de forma tal que la clave será el identificador del Tag y el valor serán los registros asociados a este.
     * @param searcherList Lista de registros a procesar.
     * @return Devuelve un {@link Map} con los registros asociados por Tag.
     */
    public static Map<String, Searcher> obtainSearcherMap(List<EkPesajesLinea> searcherList) {
        Map<String, Searcher> pesajesLineaMap = new HashMap<>();
        for (EkPesajesLinea ekPesajesLinea : searcherList) {
            if (pesajesLineaMap.get(ekPesajesLinea.getTag()) == null) {
                List<Entry> entriesList = new ArrayList<>();
                entriesList.add(Entry.builder()
                        .weigth(ekPesajesLinea.getPeso())
                        .date(ekPesajesLinea.getFecha())
                        .lote(ekPesajesLinea.getNumeroLote())
                        .invoice(ekPesajesLinea.getFolio())
                        .zone(ekPesajesLinea.getIdZona())
                        .build());
                pesajesLineaMap.put(ekPesajesLinea.getTag(),
                        Searcher.builder()
                                .countEntries(1)
                                .tag(ekPesajesLinea.getTag())
                                .entriesList(entriesList)
                                .tag(ekPesajesLinea.getTag())
                                .build()
                );
            } else {
                Searcher searched = pesajesLineaMap.get(ekPesajesLinea.getTag());
                searched.setCountEntries(searched.getCountEntries() + 1);
                searched.entriesList.add(
                        Entry.builder()
                                .weigth(ekPesajesLinea.getPeso())
                                .date(ekPesajesLinea.getFecha())
                                .lote(ekPesajesLinea.getNumeroLote())
                                .zone(ekPesajesLinea.getIdZona())
                                .invoice(ekPesajesLinea.getFolio())
                                .build()
                );
                pesajesLineaMap.replace(ekPesajesLinea.getTag(), searched);
            }
        }
        return pesajesLineaMap;
    }
}
