package cu.havanaclub.ekinsadbreview.controller;

import cu.havanaclub.ekinsadbreview.entity.EkPesajesLinea;
import cu.havanaclub.ekinsadbreview.service.PesajesLineaService;
import cu.havanaclub.ekinsadbreview.util.CsvWriter;
import cu.havanaclub.ekinsadbreview.util.Searcher;
import cu.havanaclub.ekinsadbreview.util.Verifier;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
import org.springframework.web.bind.annotation.RequestParam;
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
        List<Searcher> pesajesLineaMap = null;
        try {
            pesajesLineaMap = pesajesLineaService.listAllPesajesWithZoneErrorsAfterADate(d);
            return ResponseEntity.ok(new ArrayList<>(pesajesLineaMap));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        }
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

        List<Searcher> pesajesLineaMap = null;
        try {
            pesajesLineaMap = pesajesLineaService.listAllPesajesWithLoteErrors(d);
            return ResponseEntity.ok(new ArrayList<>(pesajesLineaMap));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        }
    }

    @Operation(summary = "Devuelve los registros en el sistema a partir de un TAG.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Searcher.class)))})
    })
    @GetMapping(value = "/registriesByTag")
    public ResponseEntity<Searcher> listAllRegistriesByTag(@RequestParam(value = "tag") String tag) {
        return ResponseEntity.ok(pesajesLineaService.listAllPesajesByTag(tag));
    }

    @Operation(summary = "Devuelve los registros que tienen errores en las Zonas, dígase un TAG que ha entrado por una zona y no ha salido por otra o viceversa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Searcher.class)))})
    })
    @GetMapping(value = "/listAllPesajesWithErrorsInZoneByDates")
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
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        }
    }
}
