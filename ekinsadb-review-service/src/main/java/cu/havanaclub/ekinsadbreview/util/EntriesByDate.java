package cu.havanaclub.ekinsadbreview.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EntriesByDate {

    Integer yearEntries;
    Integer monthEntries;
    Long cantidadRegistros;
}
