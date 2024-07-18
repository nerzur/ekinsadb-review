package cu.havanaclub.ekinsadbreview.controller;

import cu.havanaclub.ekinsadbreview.service.PesajesLineaService;
import cu.havanaclub.ekinsadbreview.util.UpdateLote;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "${server.url.prefix}/pesajesLinea/update")
@PreAuthorize("hasRole('MODIFIER_USER_ROLE')")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class PesajesLineaControllerUpdateDataImpl implements PesajesLineaControllerUpdateData{

    @Autowired
    PesajesLineaService pesajesLineaService;

    @Override
    public ResponseEntity<?> updateLote(@RequestBody UpdateLote updateLoteData) {
        return ResponseEntity.ok(pesajesLineaService.updateLote(updateLoteData));
    }

    @Override
    public ResponseEntity<?> testUpdateLote(@RequestBody UpdateLote updateLoteData) {
        return ResponseEntity.ok(pesajesLineaService.testUpdateLote(updateLoteData));
    }
}
