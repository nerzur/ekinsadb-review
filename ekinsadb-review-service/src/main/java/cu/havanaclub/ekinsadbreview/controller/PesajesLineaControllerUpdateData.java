package cu.havanaclub.ekinsadbreview.controller;

import cu.havanaclub.ekinsadbreview.util.UpdateLote;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface PesajesLineaControllerUpdateData {

    @Operation(summary = "Realiza un cambio del lote de los registros correspondientes a los tags indicados en una " +
            "línea específica (llenado o vaciado). Devuelve un JSON con la información de los registros que fueron " +
            "modificados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping(value = "/updateLote")
    ResponseEntity<?> updateLote(@RequestBody UpdateLote updateLoteData);

    @Operation(summary = "Realiza una prueba de cambio del lote de los registros correspondientes a los tags indicados en una " +
            "línea específica (llenado o vaciado). Devuelve un JSON con la información de los registros que serán " +
            "modificados de ejecutar la consulta /updateLote.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha realizado la consulta correctamente.",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping(value = "/testUpdateLote")
    ResponseEntity<?> testUpdateLote(@RequestBody UpdateLote updateLoteData);
}
