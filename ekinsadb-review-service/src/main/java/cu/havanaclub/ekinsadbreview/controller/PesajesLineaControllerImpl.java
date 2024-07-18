package cu.havanaclub.ekinsadbreview.controller;

import cu.havanaclub.ekinsadbreview.entity.EkPesajesLinea;
import cu.havanaclub.ekinsadbreview.service.PesajesLineaService;
import cu.havanaclub.ekinsadbreview.util.EntriesByDate;
import cu.havanaclub.ekinsadbreview.util.Searcher;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "${server.url.prefix}/pesajesLinea")
@PreAuthorize("hasRole('COMMON_USER_ROLE')")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class PesajesLineaControllerImpl implements PesajesLineaController {

    @Autowired
    PesajesLineaService pesajesLineaService;

    @Override
    public ResponseEntity<Integer> listAllPesajes() {
        List<EkPesajesLinea> pesajesLineaList = pesajesLineaService.listAllPesajes();
        return ResponseEntity.ok(pesajesLineaList.size());
    }

    @Override
    public ResponseEntity<Integer> countPesajesToday() {
        return ResponseEntity.ok(pesajesLineaService.countPesajesToday());
    }

    @Override
    public ResponseEntity<List<Searcher>> pesajesErroresZonaInRangeDate(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate,
            @Parameter(description = "Fecha de fin en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "endDate") String endDate) {

        long millis = Date.valueOf(startDate).getTime();
        java.sql.Date d1 = new java.sql.Date(millis);

        long millis1 = Date.valueOf(endDate).getTime();
        java.sql.Date d2 = new java.sql.Date(millis1);

        List<Searcher> pesajesLineaMap = null;
        pesajesLineaMap = pesajesLineaService.listAllPesajesWithZoneErrorsAfterADate(d1, d2);
        return ResponseEntity.ok(new ArrayList<>(pesajesLineaMap));
    }

    @Override
    public ResponseEntity<List<Searcher>> listAllPesajesWithLoteErrors() {
        long millis = Date.valueOf("2023-07-14").getTime();
        java.sql.Date d = new java.sql.Date(millis);

        List<Searcher> pesajesLineaMap = null;
        try {
            pesajesLineaMap = pesajesLineaService.listAllPesajesWithLoteErrors(d);
            return ResponseEntity.ok(new ArrayList<>(pesajesLineaMap));
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        }
    }

    @Override
    public ResponseEntity<Searcher> listAllRegistriesByTag(@RequestParam(value = "tag") String tag) {
        return ResponseEntity.ok(pesajesLineaService.listAllPesajesByTag(tag));
    }

    @Override
    public ResponseEntity<List<Searcher>> listAllPesajesWithErrorsInZoneByDates(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate,
            @Parameter(description = "Fecha de fin en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "endDate") String endDate) {
        long millis = Date.valueOf(startDate).getTime();
        java.sql.Date d1 = new java.sql.Date(millis);

        long millis1 = Date.valueOf(endDate).getTime();
        java.sql.Date d2 = new java.sql.Date(millis1);
        try {
            return ResponseEntity.ok(pesajesLineaService.listAllPesajesWithErrorsInZoneByDate(d1, d2));
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        }
    }

    @Override
    public ResponseEntity<Integer> countLotesByDates(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate,
            @Parameter(description = "Fecha de fin en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "endDate") String endDate) {
        long millis = Date.valueOf(startDate).getTime();
        java.sql.Date d1 = new java.sql.Date(millis);

        long millis1 = Date.valueOf(endDate).getTime();
        java.sql.Date d2 = new java.sql.Date(millis1);
        return ResponseEntity.ok(pesajesLineaService.countLotesByDates(d1, d2));
    }

    @Override
    public ResponseEntity<Integer> countErrorsInTagByDates(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate,
            @Parameter(description = "Fecha de fin en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "endDate") String endDate) {
        long millis = Date.valueOf(startDate).getTime();
        java.sql.Date d1 = new java.sql.Date(millis);

        long millis1 = Date.valueOf(endDate).getTime();
        java.sql.Date d2 = new java.sql.Date(millis1);
        return ResponseEntity.ok(pesajesLineaService.countErrorsTagByDates(d1, d2));
    }

    @Override
    public ResponseEntity<List<EkPesajesLinea>> findEkPesajesLineaByTag(
            @Parameter(description = "Tag del pallet", required = true, in = ParameterIn.QUERY) @RequestParam(value = "tag") String tag) {
        return ResponseEntity.ok(pesajesLineaService.findEkPesajesLineaByTag(tag));
    }

    @Override
    public ResponseEntity<Integer> countLotesWithErrorsByDate(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate,
            @Parameter(description = "Fecha de fin en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "endDate") String endDate) {
        long millis = Date.valueOf(startDate).getTime();
        java.sql.Date d1 = new java.sql.Date(millis);

        long millis1 = Date.valueOf(endDate).getTime();
        java.sql.Date d2 = new java.sql.Date(millis1);
        return ResponseEntity.ok(pesajesLineaService.findCountLotesWithErrorsByDates(d1, d2));
    }

    @Override
    public ResponseEntity<Integer> countLotesWithOutErrorsByDate(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate,
            @Parameter(description = "Fecha de fin en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "endDate") String endDate) {
        long millis = Date.valueOf(startDate).getTime();
        java.sql.Date d1 = new java.sql.Date(millis);

        long millis1 = Date.valueOf(endDate).getTime();
        java.sql.Date d2 = new java.sql.Date(millis1);
        return ResponseEntity.ok(pesajesLineaService.findCountLotesWithOutErrorsByDates(d1, d2));
    }

    @Override
    public ResponseEntity<Integer> countPesajesAfterDate(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate) {
        long millis = Date.valueOf(startDate).getTime();
        java.sql.Date d1 = new java.sql.Date(millis);

        return ResponseEntity.ok(pesajesLineaService.lisAllPesajesAfterDate(d1).size());
    }

    @Override
    public ResponseEntity<List<EntriesByDate>> countEntriesByDates() {
        return ResponseEntity.ok(pesajesLineaService.countEntriesByDates());
    }

    @Override
    public ResponseEntity<List<EntriesByDate>> countEntriesVaciadoOrLLenadoByDates(@PathVariable boolean isVaciado) {
        return ResponseEntity.ok(pesajesLineaService.countEntriesVaciadoOrLLenadoByDates(isVaciado));
    }

    @Override
    public ResponseEntity<?> findDistinctTagByNumeroLote(@RequestParam String numeroLote, @RequestParam boolean isVaciado) {
        return ResponseEntity.ok(pesajesLineaService.findDistinctTagByNumeroLote(numeroLote, isVaciado));
    }

    @Override
    public ResponseEntity<?> findDistinctNumeroLoteByDate(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate,
            @Parameter(description = "Fecha de fin en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "endDate") String endDate) {
        long millis = Date.valueOf(startDate).getTime();
        java.sql.Date d1 = new java.sql.Date(millis);

        long millis1 = Date.valueOf(endDate).getTime();
        java.sql.Date d2 = new java.sql.Date(millis1);

        return ResponseEntity.ok(pesajesLineaService.findDistinctNumeroLoteByDate(d1, d2));
    }
}
