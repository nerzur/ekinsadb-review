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
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(value = "${server.url.prefix}/keycloak/user")
@PreAuthorize("hasRole('ADMIN_ROLE')")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin
@Slf4j
public class KeycloakController {

    @Autowired
    private KeycloakService keycloakService;

    @Operation(summary = "Devuelve todos los usuarios del REALM actual.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La consulta se ha realizado correctamente",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping()
    public ResponseEntity<List<UserRepresentation>> findAllUsers(){
        return ResponseEntity.ok(keycloakService.findAllUsers());
    }

    @Operation(summary = "Devuelve la información del usuario a partir de su username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La consulta se ha realizado correctamente",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping("/search/{username}")
    public ResponseEntity<List<UserRepresentation>> searchUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(keycloakService.searchUserByUsername(username));
    }

    @Operation(summary = "Crea un nuevo usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La consulta se ha realizado correctamente",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping("/create")
    public ResponseEntity<List<UserRepresentation>> createUser(@RequestBody UserDTO userDTO) throws URISyntaxException {
        return ResponseEntity.created(new URI("user/create")).body(keycloakService.createUser(userDTO));
    }

    @Operation(summary = "Actualiza los datos de un usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La consulta se ha realizado correctamente",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping("/update")
    public ResponseEntity<UserRepresentation> updateUser(@RequestBody UserDTO userDTO) throws URISyntaxException {
        return ResponseEntity.ok(keycloakService.updateUser(userDTO));
    }

    @Operation(summary = "Elimina un usuario a partir de su username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La consulta se ha realizado correctamente",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping("/delete/{username}")
    public ResponseEntity<UserRepresentation> deleteUser(@PathVariable String username){
        return ResponseEntity.ok(keycloakService.deleteUser(username));
    }
}
