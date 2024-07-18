package cu.havanaclub.ekinsadbreview.controller;

import cu.havanaclub.ekinsadbreview.entity.EkPesajesLinea;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface PesajesLineaController {

    @Operation(summary = "Devuelve la cantidad de pesajes existentes en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La consulta se ha realizado correctamente",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping
    ResponseEntity<Integer> listAllPesajes();

    @Operation(summary = "Devuelve la cantidad de pesajes realizados hoy.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La consulta se ha realizado correctamente",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping(value = "/pesajesToday")
    ResponseEntity<Integer> countPesajesToday();

    @Operation(summary = "Lista los errores existentes en las zonas de los registros de forma manual. Suma 1 cuando se encuentra un" +
            "registro por una zona de entrada de línea y resta 1 cuando sale el registro por la zona homóloga.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Searcher.class)))})
    })
    @GetMapping(value = "/pesajesErroresZonaInRangeDate")
    ResponseEntity<List<Searcher>> pesajesErroresZonaInRangeDate(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate,
            @Parameter(description = "Fecha de fin en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "endDate") String endDate);

    @Operation(summary = "Devuelve las entradas existentes en la base de datos a partir de una fecha inficada que tienen problemas de cambio de lote." +
            "Se tiene en cuenta que un parlet haya entrado por una línea (zona 1 por ejemplo) tenga un número de lote y que " +
            "al salir por otra zona (zona 2 por ejemplo) tenga otro lote.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Searcher.class)))})
    })
    @GetMapping(value = "/verifyErroresLotesAfterDate")
    ResponseEntity<List<Searcher>> listAllPesajesWithLoteErrors();

    @Operation(summary = "Devuelve los registros en el sistema a partir de un TAG.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Searcher.class)))})
    })
    @GetMapping(value = "/registriesByTag")
    ResponseEntity<Searcher> listAllRegistriesByTag(@RequestParam(value = "tag") String tag);

    @Operation(summary = "Devuelve los registros que tienen errores en las Zonas, dígase un TAG que ha entrado por una zona y no ha salido por otra o viceversa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Searcher.class)))})
    })
    @GetMapping(value = "/listAllPesajesWithErrorsInZoneByDates")
    ResponseEntity<List<Searcher>> listAllPesajesWithErrorsInZoneByDates(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate,
            @Parameter(description = "Fecha de fin en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "endDate") String endDate);

    @Operation(summary = "Cantidad de lotes procesados en un rango de fecha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping(value = "/countLotesByDates")
    ResponseEntity<Integer> countLotesByDates(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate,
            @Parameter(description = "Fecha de fin en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "endDate") String endDate);

    @Operation(summary = "Cantidad de registros con errores (tags con registros huérfanos) en un rango de fechas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping(value = "/countErrorsInTagByDates")
    ResponseEntity<Integer> countErrorsInTagByDates(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate,
            @Parameter(description = "Fecha de fin en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "endDate") String endDate);

    @Operation(summary = "Devuelve los registros de un tag.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EkPesajesLinea.class)))})
    })
    @GetMapping(value = "/findEkPesajesLineaByTag")
    ResponseEntity<List<EkPesajesLinea>> findEkPesajesLineaByTag(
            @Parameter(description = "Tag del pallet", required = true, in = ParameterIn.QUERY) @RequestParam(value = "tag") String tag);

    @Operation(summary = "Cantidad de lotes con errores en un rango de fechas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping(value = "/countLotesWithErrorsInTagByDates")
    ResponseEntity<Integer> countLotesWithErrorsByDate(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate,
            @Parameter(description = "Fecha de fin en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "endDate") String endDate);

    @Operation(summary = "Cantidad de lotes sin errores en un rango de fechas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping(value = "/countLotesWithOutErrorsInTagByDates")
    ResponseEntity<Integer> countLotesWithOutErrorsByDate(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate,
            @Parameter(description = "Fecha de fin en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "endDate") String endDate);

    @Operation(summary = "Cantidad de pesajes posteriores a una fecha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping(value = "/countPesajesAfterDate")
    ResponseEntity<Integer> countPesajesAfterDate(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate);

    @Operation(summary = "Cantidad de pesajes por meses y años.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping(value = "/countEntriesByDates")
    ResponseEntity<List<EntriesByDate>> countEntriesByDates();

    @Operation(summary = "Cantidad de pesajes por meses y años teniendo en cuenta el tipo de operación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping(value = "/countEntriesVaciadoOrLLenadoByDates/{isVaciado}")
    ResponseEntity<List<EntriesByDate>> countEntriesVaciadoOrLLenadoByDates(@PathVariable boolean isVaciado);

    @GetMapping(value = "/findTagsByNúmeroLote")
    ResponseEntity<?> findDistinctTagByNumeroLote(@RequestParam String numeroLote, @RequestParam boolean isVaciado);

    @GetMapping(value = "/findNumeroLoteByDate")
    ResponseEntity<?> findDistinctNumeroLoteByDate(
            @Parameter(description = "Fecha de inicio en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "startDate") String startDate,
            @Parameter(description = "Fecha de fin en formato yyyy-MM-dd", required = true, in = ParameterIn.QUERY) @RequestParam(value = "endDate") String endDate);


    }
