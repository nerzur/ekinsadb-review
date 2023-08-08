package cu.havanaclub.ekinsadbreview.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.util.List;

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
}
