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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "${server.url.prefix}/user")
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin
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
    public ResponseEntity<?> getUsername() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        Jwt principal = (Jwt) auth.getPrincipal();

        UserRepresentation userRepresentations = keycloakService.searchUserByUsername(auth.getName()).get(0);

        if(userRepresentations == null)
            log.error("An error has been occurred");

        UserDTO jwtUser = UserDTO.builder()
                .username(auth.getName())
                .email(principal.getClaim("email"))
                .firstName(userRepresentations.getFirstName())
                .lastName(userRepresentations.getLastName())
                .enabled(userRepresentations.isEnabled())
                .password("RESTRICTED")
                .roles(auth.getAuthorities().stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority().endsWith("_ROLE")?
                                grantedAuthority.getAuthority().substring(0, grantedAuthority.getAuthority().lastIndexOf("_")):
                                grantedAuthority.getAuthority())
                        .toList())
                .build();
        return ResponseEntity.ok(jwtUser);
    }
}
