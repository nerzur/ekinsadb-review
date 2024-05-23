package cu.havanaclub.ekinsadbreview.service;

import cu.havanaclub.ekinsadbreview.entity.EkPesajesLinea;
import cu.havanaclub.ekinsadbreview.repository.PesajesLineaRepository;
import cu.havanaclub.ekinsadbreview.util.*;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public List<Searcher> listAllPesajesWithZoneErrorsAfterADate(Date startDate, Date endDate) {
        Timestamp timestampStart = new java.sql.Timestamp(startDate.getTime());
        Timestamp timestampEnd = new java.sql.Timestamp(endDate.getTime());

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(timestampEnd);
        cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
        cal.set(java.util.Calendar.MINUTE, 59);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        timestampEnd.setTime(cal.getTime().getTime());

        List<EkPesajesLinea> pesajesLineaAfterDateList = pesajesLineaRepository.findEkPesajesLineaByFechaBetweenOrderByFecha(timestampStart, timestampEnd);
        Map<String, Searcher> pesajesLineaMap = Searcher.obtainSearcherMap(pesajesLineaAfterDateList);

        pesajesLineaMap.entrySet().removeIf(entry -> Verifier.verifyZone(entry.getValue().getEntriesList()));
        pesajesLineaMap.entrySet().removeIf(entry -> searchErrorsInRegistry(entry.getValue()));

        System.out.println("Detected " + pesajesLineaMap.values().size() + " tags with errors.");

        return new ArrayList<>(pesajesLineaMap.values());
    }

    private boolean searchErrorsInRegistry(Searcher searcher) {
        List<EkPesajesLinea> pesajesByTag = pesajesLineaRepository.findEkPesajesLineaByTagOrderByFecha(searcher.getTag());
        Map<String, Searcher> pesajesLineaMap = Searcher.obtainSearcherMap(pesajesByTag);
        return Verifier.verifyZone(pesajesLineaMap.get(searcher.getTag()).getEntriesList());
    }

    @Override
    public List<Searcher> listAllPesajesWithErrorsInZoneByDate(Date startDate, Date endDate) throws IOException {
        List<EkPesajesLinea> pesajesLineaList = pesajesLineaRepository.findEkPesajesLineaWithErrorsInZoneByDate(startDate, endDate);
        Map<String, Searcher> pesajesLineaMap = Searcher.obtainSearcherMap(pesajesLineaList);

        java.util.Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH mm ss");
        String formattedDate = formatter.format(now);
        CsvWriter.writeToCsv(new ArrayList<>(pesajesLineaMap.values()), "Errors in Zones (NEW API VERSION)", formattedDate);

        return new ArrayList<>(pesajesLineaMap.values());

    }

    @Override
    public List<Searcher> listAllPesajesWithLoteErrors(Date date) throws IOException {
        List<EkPesajesLinea> pesajesLineaAfterDateList = lisAllPesajesAfterDate(date);
        Map<String, Searcher> pesajesLineaMap = Searcher.obtainSearcherMap(pesajesLineaAfterDateList);

        pesajesLineaMap.entrySet().removeIf(entry -> Verifier.verifyLote(entry.getValue()));

        java.util.Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH mm ss");
        String formattedDate = formatter.format(now);

        CsvWriter.writeToCsv(new ArrayList<>(pesajesLineaMap.values()), "Errors in Lotes", formattedDate);
        System.out.println("Detected " + pesajesLineaMap.values().size() + " tags with errors.");
        return new ArrayList<>(pesajesLineaMap.values());
    }

    @Override
    public Searcher listAllPesajesByTag(String tag) {
        List<EkPesajesLinea> pesajesLineaAfterDateList = pesajesLineaRepository.findEkPesajesLineaByTagOrderByFecha(tag);
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

    @Override
    public Integer countPesajesToday() {
        return pesajesLineaRepository.countEkPesajesLineaToday();
    }

    @Override
    public Integer countLotesByDates(Date startDate, Date endDate) {
        return pesajesLineaRepository.countLotesByDates(startDate, endDate);
    }

    @Override
    public Integer countErrorsTagByDates(Date startDate, Date endDate) {
        return listAllPesajesWithZoneErrorsAfterADate(startDate, endDate).size();
    }

    @Override
    public List<EkPesajesLinea> findEkPesajesLineaByTag(String tag) {
        return pesajesLineaRepository.findEkPesajesLineaByTagOrderByFecha(tag);
    }

    @Override
    public Integer findCountLotesWithErrorsByDates(Date startDate, Date endDate) {
        List<Searcher> pesajesLineaWithErrorsList = listAllPesajesWithZoneErrorsAfterADate(startDate, endDate);

        Map<String, Integer> lotesErrorMap = new HashMap<>();

        pesajesLineaWithErrorsList.forEach(searcher -> {
            if (!lotesErrorMap.containsKey(searcher.getEntriesList().get(0).getLote())) {
                lotesErrorMap.put(searcher.getEntriesList().get(0).getLote(), 1);
            } else {
                lotesErrorMap.replace(searcher.getEntriesList().get(0).getLote(), lotesErrorMap.get(searcher.getEntriesList().get(0).getLote() + 1));
            }
        });
        return lotesErrorMap.values().size();
    }

    @Override
    public Integer findCountLotesWithOutErrorsByDates(Date startDate, Date endDate) {
        int countDistinctNumeroLoteByDate = pesajesLineaRepository.findDistinctNumeroLoteByDate(startDate, endDate).size();
        return countDistinctNumeroLoteByDate - findCountLotesWithErrorsByDates(startDate, endDate);
    }

    @Override
    public List<EntriesByDate> countEntriesByDates() {
        return pesajesLineaRepository.countEntriesByDates();
    }

    @Override
    public List<EntriesByDate> countEntriesVaciadoOrLLenadoByDates(boolean isVaciado) {
        List<EntriesByDate> entriesByDates = pesajesLineaRepository.countEntriesVaciadoOrLLenadoByDates(isVaciado?1:3, isVaciado?2:4);
        Collections.reverse(entriesByDates);
        return entriesByDates;
    }

    @Override
    public List<EkPesajesLinea> testUpdateLote(@NonNull UpdateLote updateLote) {
        List<EkPesajesLinea> ekPesajesLineaList = new ArrayList<>();
        for (String tag : updateLote.getTagsList()) {
            List<EkPesajesLinea> list = pesajesLineaRepository.findByTagAndNumeroLote(tag, updateLote.getPrevLote());
            for (EkPesajesLinea ekPesajesLinea : list) {
                boolean isVaciado = updateLote.getIsVaciado();
                int idZona = ekPesajesLinea.getIdZona();

                if ((isVaciado && (idZona == 1 || idZona == 2)) || (!isVaciado && (idZona == 3 || idZona == 4))) {
                    ekPesajesLinea.setNumeroLote(updateLote.getNewLote());
                    ekPesajesLineaList.add(ekPesajesLinea);
                }
            }
        }
        return ekPesajesLineaList;
    }

    @Override
    public List<EkPesajesLinea> updateLote(@NonNull UpdateLote updateLote) {
        List<EkPesajesLinea> ekPesajesLineaList = testUpdateLote(updateLote);
        List<EkPesajesLinea> ekPesajesLineaListOutput = new ArrayList<>();
        ekPesajesLineaList.forEach(ekPesajesLinea -> {
            ekPesajesLineaListOutput.add(pesajesLineaRepository.save(ekPesajesLinea));
        });
        return ekPesajesLineaListOutput;
    }
}
