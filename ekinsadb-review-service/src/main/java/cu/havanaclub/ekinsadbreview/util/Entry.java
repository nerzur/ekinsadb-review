package cu.havanaclub.ekinsadbreview.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Entry {

    Integer zone;
    Integer weigth;
    Timestamp date;
    String lote;
}
