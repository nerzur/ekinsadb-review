package cu.havanaclub.ekinsadbreview.controller;

import cu.havanaclub.ekinsadbreview.entity.EkPesajesLinea;
import cu.havanaclub.ekinsadbreview.service.PesajesLineaService;
import cu.havanaclub.ekinsadbreview.util.CsvWriter;
import cu.havanaclub.ekinsadbreview.util.Searcher;
import cu.havanaclub.ekinsadbreview.util.Verifier;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "pesajesLinea")
public class PesajesLineaController {

    @Autowired
    PesajesLineaService pesajesLineaService;

    @Operation(summary = "Devuelve la cantidad de pesajes existentes en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La consulta se ha realizado correctamente",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping
    public ResponseEntity<Integer> listAllPesajes() {
        List<EkPesajesLinea> pesajesLineaList = pesajesLineaService.listAllPesajes();
        return ResponseEntity.ok(pesajesLineaList.size());
    }

    @Operation(summary = "Devuelve las entradas existentes en la base de datos a partir de una fecha inficada que tienen problemas en las zonas." +
            "Se tiene en cuenta que un parlet haya entrado por una zona y no haya salido o que la cantidad de entradas por las zonas" +
            "difiere de las cuatro habituales (1,2,3,4). Este servicio también genera un fichero .csv con las entradas detectadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Searcher.class)))})
    })
    @GetMapping(value = "/pesajesErroresZonaAfterDate")
    public ResponseEntity<List<Searcher>> listAllPesajesWithZoneErrorsAfterADate() {
        long millis = Date.valueOf("2023-07-14").getTime();
        java.sql.Date d = new java.sql.Date(millis);

        List<EkPesajesLinea> pesajesLineaAfterDateList = pesajesLineaService.lisAllPesajesAfterDate(d);
        Map<String, Searcher> pesajesLineaMap = Searcher.obtainSearcherMap(pesajesLineaAfterDateList);

        pesajesLineaMap.entrySet().removeIf(entry -> Verifier.verifyZone(entry.getValue().getZones()));

        java.util.Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = formatter.format(now);

        try {
            CsvWriter.writeToCsv(new ArrayList<>(pesajesLineaMap.values()), "Tags with Errors ", "Errors in Zones", formattedDate);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        }
        return ResponseEntity.ok(new ArrayList<>(pesajesLineaMap.values()));
    }

    @Operation(summary = "Devuelve las entradas existentes en la base de datos a partir de una fecha inficada que tienen problemas de cambio de lote." +
            "Se tiene en cuenta que un parlet haya entrado por una línea (zona 1 por ejemplo) tenga un número de lote y que " +
            "al salir por otra zona (zona 2 por ejemplo) tenga otro lote.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Searcher.class)))})
    })
    @GetMapping(value = "/verifyErroresLotesAfterDate")
    public ResponseEntity<List<Searcher>> listAllPesajesWithLoteErrors() {
        long millis = Date.valueOf("2023-07-14").getTime();
        java.sql.Date d = new java.sql.Date(millis);

        List<EkPesajesLinea> pesajesLineaAfterDateList = pesajesLineaService.lisAllPesajesAfterDate(d);
        Map<String, Searcher> pesajesLineaMap = Searcher.obtainSearcherMap(pesajesLineaAfterDateList);

        pesajesLineaMap.entrySet().removeIf(entry -> Verifier.verifyLote(entry.getValue()));

        java.util.Date now = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = formatter.format(now);

        try {
            CsvWriter.writeToCsv(new ArrayList<>(pesajesLineaMap.values()), "Tags with Errors ", "Errors in Lotes", formattedDate);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        }

        return ResponseEntity.ok(new ArrayList<>(pesajesLineaMap.values()));

    }
}
