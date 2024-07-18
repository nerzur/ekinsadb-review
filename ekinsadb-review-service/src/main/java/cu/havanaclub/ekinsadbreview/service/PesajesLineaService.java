package cu.havanaclub.ekinsadbreview.service;

import cu.havanaclub.ekinsadbreview.entity.EkPesajesLinea;
import cu.havanaclub.ekinsadbreview.util.EntriesByDate;
import cu.havanaclub.ekinsadbreview.util.Searcher;
import cu.havanaclub.ekinsadbreview.util.UpdateLote;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

public interface PesajesLineaService {

    List<EkPesajesLinea> listAllPesajes();

    List<EkPesajesLinea> lisAllPesajesAfterDate(Date date);

    List<Searcher> listAllPesajesWithZoneErrorsAfterADate(Date startDate, Date endDate);
    List<Searcher> listAllPesajesWithErrorsInZoneByDate(Date startDate, Date endDate) throws IOException;

    List<Searcher> listAllPesajesWithLoteErrors(Date date) throws IOException;

    Searcher listAllPesajesByTag(String tag);

    Integer countPesajesToday();

    Integer countLotesByDates(Date startDate, Date endDate);

    Integer countErrorsTagByDates(Date startDate, Date endDate);

    List<EkPesajesLinea> findEkPesajesLineaByTag(String tag);

    Integer findCountLotesWithErrorsByDates(Date startDate, Date endDate);

    Integer findCountLotesWithOutErrorsByDates(Date startDate, Date endDate);

    List<EntriesByDate> countEntriesByDates();

    List<EntriesByDate> countEntriesVaciadoOrLLenadoByDates(boolean isVaciado);

    List<EkPesajesLinea> updateLote(@NonNull UpdateLote updateLote);

    List<EkPesajesLinea> testUpdateLote(@NonNull UpdateLote updateLote);

    List<String> findDistinctTagByNumeroLote(String numeroLote, boolean isVaciado);

    List<String> findDistinctNumeroLoteByDate(Date startDate, Date endDate);
}
