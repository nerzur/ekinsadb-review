package cu.havanaclub.ekinsadbreview.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UpdateLote {

    List<String> tagsList;
    String prevLote;
    String newLote;
    Boolean isVaciado;
}
