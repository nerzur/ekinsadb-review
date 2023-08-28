package cu.havanaclub.ekinsadbreview.service;

import cu.havanaclub.ekinsadbreview.entity.EkPesajesLinea;
import cu.havanaclub.ekinsadbreview.util.Searcher;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

public interface PesajesLineaService {

    public List<EkPesajesLinea> listAllPesajes();

    public List<EkPesajesLinea> lisAllPesajesAfterDate(Date date);

    public List<Searcher> listAllPesajesWithZoneErrorsAfterADate(Date date) throws IOException;

    List<Searcher> listAllPesajesWithLoteErrors(Date date) throws IOException;

    Searcher listAllPesajesByTag(String tag);
}
