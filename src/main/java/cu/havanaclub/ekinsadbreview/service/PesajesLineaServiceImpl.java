package cu.havanaclub.ekinsadbreview.service;

import cu.havanaclub.ekinsadbreview.entity.EkPesajesLinea;
import cu.havanaclub.ekinsadbreview.repository.PesajesLineaRepository;
import cu.havanaclub.ekinsadbreview.util.CsvWriter;
import cu.havanaclub.ekinsadbreview.util.Entry;
import cu.havanaclub.ekinsadbreview.util.Searcher;
import cu.havanaclub.ekinsadbreview.util.Verifier;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class PesajesLineaServiceImpl implements PesajesLineaService {

    private final PesajesLineaRepository pesajesLineaRepository;

    @Override
    public List<EkPesajesLinea> listAllPesajes() {
        return pesajesLineaRepository.findAll();
    }

    @Override
    public List<EkPesajesLinea> lisAllPesajesAfterDate(Date date) {
        return pesajesLineaRepository.findEkPesajesLineaByFechaAfterOrderByFecha(date);
    }

    @Override
    public List<Searcher> listAllPesajesWithZoneErrorsAfterADate(Date date) throws IOException {
        List<EkPesajesLinea> pesajesLineaAfterDateList = lisAllPesajesAfterDate(date);
        Map<String, Searcher> pesajesLineaMap = Searcher.obtainSearcherMap(pesajesLineaAfterDateList);

        pesajesLineaMap.entrySet().removeIf(entry -> Verifier.verifyZone(entry.getValue().getEntriesList()));

        java.util.Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd hh mm ss");
        String formattedDate = formatter.format(now);

        CsvWriter.writeToCsv(new ArrayList<>(pesajesLineaMap.values()), "Errors in Zones", formattedDate);
        System.out.println("Detected " + pesajesLineaMap.values().size() + " tags with errors.");

        return new ArrayList<>(pesajesLineaMap.values());
    }

    @Override
    public List<Searcher> listAllPesajesWithLoteErrors(Date date) throws IOException {
        List<EkPesajesLinea> pesajesLineaAfterDateList = lisAllPesajesAfterDate(date);
        Map<String, Searcher> pesajesLineaMap = Searcher.obtainSearcherMap(pesajesLineaAfterDateList);

        pesajesLineaMap.entrySet().removeIf(entry -> Verifier.verifyLote(entry.getValue()));

        java.util.Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd hh mm ss");
        String formattedDate = formatter.format(now);

        CsvWriter.writeToCsv(new ArrayList<>(pesajesLineaMap.values()), "Errors in Lotes", formattedDate);
        System.out.println("Detected " + pesajesLineaMap.values().size() + " tags with errors.");
        return new ArrayList<>(pesajesLineaMap.values());
    }

    @Override
    public Searcher listAllPesajesByTag(String tag) {
        List<EkPesajesLinea> pesajesLineaAfterDateList = pesajesLineaRepository.findEkPesajesLineaByTag(tag);
        List<Entry> entriesList = new ArrayList<>();
        for (EkPesajesLinea ekPesajesLinea : pesajesLineaAfterDateList) {
            entriesList.add(
                    Entry.builder()
                            .zone(ekPesajesLinea.getIdZona())
                            .lote(ekPesajesLinea.getNumeroLote())
                            .weigth(ekPesajesLinea.getPeso())
                            .date(ekPesajesLinea.getFecha())
                            .build()
            );
        }
        return Searcher.builder()
                .tag(tag)
                .countEntries(pesajesLineaAfterDateList.size())
                .entriesList(entriesList)
                .build();
    }
}
