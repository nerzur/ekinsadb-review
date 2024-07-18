package cu.havanaclub.ekinsadbreview.controller;

import cu.havanaclub.ekinsadbreview.entity.EkPesajesLinea;
import cu.havanaclub.ekinsadbreview.service.PesajesLineaService;
import cu.havanaclub.ekinsadbreview.util.EntriesByDate;
import cu.havanaclub.ekinsadbreview.util.Searcher;
import cu.havanaclub.ekinsadbreview.util.UpdateLote;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@PreAuthorize("hasRole('USER_ROLE')")
@SecurityRequirement(name = "Bearer Authentication")
//@CrossOrigin(origins = "*")
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

    @Operation(summary = "Devuelve la cantidad de pesajes realizados hoy.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La consulta se ha realizado correctamente",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping(value = "/pesajesToday")
    public ResponseEntity<Integer> countPesajesToday() {
        return ResponseEntity.ok(pesajesLineaService.countPesajesToday());
    }

    @Operation(summary = "Lista los errores existentes en las zonas de los registros de forma manual. Suma 1 cuando se encuentra un" +
            "registro por una zona de entrada de línea y resta 1 cuando sale el registro por la zona homóloga.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Searcher.class)))})
    })
    @GetMapping(value = "/pesajesErroresZonaInRangeDate")
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

    @Operation(summary = "Cantidad de lotes procesados en un rango de fecha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping(value = "/countLotesByDates")
    public ResponseEntity<Integer> countLotesByDates(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate,
            @Parameter(description = "Fecha de fin en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "endDate") String endDate) {
        long millis = Date.valueOf(startDate).getTime();
        java.sql.Date d1 = new java.sql.Date(millis);

        long millis1 = Date.valueOf(endDate).getTime();
        java.sql.Date d2 = new java.sql.Date(millis1);
        return ResponseEntity.ok(pesajesLineaService.countLotesByDates(d1, d2));
    }

    @Operation(summary = "Cantidad de registros con errores (tags con registros huérfanos) en un rango de fechas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping(value = "/countErrorsInTagByDates")
    public ResponseEntity<Integer> countErrorsInTagByDates(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate,
            @Parameter(description = "Fecha de fin en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "endDate") String endDate) {
        long millis = Date.valueOf(startDate).getTime();
        java.sql.Date d1 = new java.sql.Date(millis);

        long millis1 = Date.valueOf(endDate).getTime();
        java.sql.Date d2 = new java.sql.Date(millis1);
        return ResponseEntity.ok(pesajesLineaService.countErrorsTagByDates(d1, d2));
    }

    @Operation(summary = "Devuelve los registros de un tag.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EkPesajesLinea.class)))})
    })
    @GetMapping(value = "/findEkPesajesLineaByTag")
    public ResponseEntity<List<EkPesajesLinea>> findEkPesajesLineaByTag(
            @Parameter(description = "Tag del pallet", required = true, in = ParameterIn.QUERY) @RequestParam(value = "tag") String tag){
        return ResponseEntity.ok(pesajesLineaService.findEkPesajesLineaByTag(tag));
    }

    @Operation(summary = "Cantidad de lotes con errores en un rango de fechas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping(value = "/countLotesWithErrorsInTagByDates")
    public ResponseEntity<Integer> countLotesWithErrorsByDate(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate,
            @Parameter(description = "Fecha de fin en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "endDate") String endDate) {
        long millis = Date.valueOf(startDate).getTime();
        java.sql.Date d1 = new java.sql.Date(millis);

        long millis1 = Date.valueOf(endDate).getTime();
        java.sql.Date d2 = new java.sql.Date(millis1);
        return ResponseEntity.ok(pesajesLineaService.findCountLotesWithErrorsByDates(d1, d2));
    }

    @Operation(summary = "Cantidad de lotes sin errores en un rango de fechas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping(value = "/countLotesWithOutErrorsInTagByDates")
    public ResponseEntity<Integer> countLotesWithOutErrorsByDate(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate,
            @Parameter(description = "Fecha de fin en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "endDate") String endDate) {
        long millis = Date.valueOf(startDate).getTime();
        java.sql.Date d1 = new java.sql.Date(millis);

        long millis1 = Date.valueOf(endDate).getTime();
        java.sql.Date d2 = new java.sql.Date(millis1);
        return ResponseEntity.ok(pesajesLineaService.findCountLotesWithOutErrorsByDates(d1, d2));
    }

    @Operation(summary = "Cantidad de pesajes posteriores a una fecha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping(value = "/countPesajesAfterDate")
    public ResponseEntity<Integer> countPesajesAfterDate(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate) {
        long millis = Date.valueOf(startDate).getTime();
        java.sql.Date d1 = new java.sql.Date(millis);

        return ResponseEntity.ok(pesajesLineaService.lisAllPesajesAfterDate(d1).size());
    }

    @Operation(summary = "Cantidad de pesajes por meses y años.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping(value = "/countEntriesByDates")
    public ResponseEntity<List<EntriesByDate>> countEntriesByDates(){
        return ResponseEntity.ok(pesajesLineaService.countEntriesByDates());
    }

    @Operation(summary = "Cantidad de pesajes por meses y años teniendo en cuenta el tipo de operación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping(value = "/countEntriesVaciadoOrLLenadoByDates/{isVaciado}")
    public ResponseEntity<List<EntriesByDate>> countEntriesVaciadoOrLLenadoByDates(@PathVariable boolean isVaciado){
        return ResponseEntity.ok(pesajesLineaService.countEntriesVaciadoOrLLenadoByDates(isVaciado));
    }

    @Operation(summary = "Realiza un cambio del lote de los registros correspondientes a los tags indicados en una " +
            "línea específica (llenado o vaciado). Devuelve un JSON con la información de los registros que fueron " +
            "modificados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping(value = "/updateLote")
    public ResponseEntity<?> updateLote(@RequestBody UpdateLote updateLoteData){
        return ResponseEntity.ok(pesajesLineaService.updateLote(updateLoteData));
    }

    @Operation(summary = "Realiza una prueba de cambio del lote de los registros correspondientes a los tags indicados en una " +
            "línea específica (llenado o vaciado). Devuelve un JSON con la información de los registros que serán " +
            "modificados de ejecutar la consulta /updateLote.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping(value = "/testUpdateLote")
    public ResponseEntity<?> testUpdateLote(@RequestBody UpdateLote updateLoteData){
        return ResponseEntity.ok(pesajesLineaService.testUpdateLote(updateLoteData));
    }

    @GetMapping(value = "/findTagsByNúmeroLote")
    public ResponseEntity<?> findDistinctTagByNumeroLote(@RequestParam String numeroLote, @RequestParam boolean isVaciado){
        return ResponseEntity.ok(pesajesLineaService.findDistinctTagByNumeroLote(numeroLote, isVaciado));
    }

    @GetMapping(value = "/findNumeroLoteByDate")
    public ResponseEntity<?> findDistinctNumeroLoteByDate(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate,
            @Parameter(description = "Fecha de fin en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "endDate") String endDate){
        long millis = Date.valueOf(startDate).getTime();
        java.sql.Date d1 = new java.sql.Date(millis);

        long millis1 = Date.valueOf(endDate).getTime();
        java.sql.Date d2 = new java.sql.Date(millis1);

        return ResponseEntity.ok(pesajesLineaService.findDistinctNumeroLoteByDate(d1, d2));
    }
}
