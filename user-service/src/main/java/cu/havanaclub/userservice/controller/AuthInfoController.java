package cu.havanaclub.userservice.controller;

import cu.havanaclub.userservice.model.UserDTO;
import cu.havanaclub.userservice.service.KeycloakService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "${server.url.prefix}/user")
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class AuthInfoController {

    @Autowired
    private KeycloakService keycloakService;

    @Operation(summary = "Devuelve los datos de sesión del usuario actual.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La consulta se ha realizado correctamente",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping()
    public ResponseEntity<?> getAuthenticatedUserInfo() {
        return ResponseEntity.ok(keycloakService.getAuthenticatedUserData());
    }

    @PostMapping(value = "/changeUserInformation")
    public ResponseEntity<?> changeUserInformation(@RequestBody UserDTO newUserInformation){
        UserDTO authenticatedUserData = keycloakService.getAuthenticatedUserData();
        if(!authenticatedUserData.username().equals(newUserInformation.username())){
            return ResponseEntity
                    .badRequest()
                    .body("The username provided does not match with the authenticated user.");
        }
        return ResponseEntity.ok(keycloakService.updateUser(newUserInformation));
    }

    @GetMapping(value = "/getUserLang")
    public ResponseEntity<?> getUserLang(){
        return ResponseEntity.ok(keycloakService.getUserLang());
    }

    @PostMapping(value = "/setUserLang/{newLang}")
    public ResponseEntity<?> setUserLang(@PathVariable String newLang){
        keycloakService.setUserLang(newLang);
        return ResponseEntity.ok(keycloakService.getUserLang());
    }

}
