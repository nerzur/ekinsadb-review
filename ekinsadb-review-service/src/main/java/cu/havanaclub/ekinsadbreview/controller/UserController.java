package cu.havanaclub.ekinsadbreview.controller;

import cu.havanaclub.ekinsadbreview.util.JwtUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping(value = "${server.url.suffix}/user")
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Operation(summary = "Devuelve los datos de sesión del usuario actual.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La consulta se ha realizado correctamente",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping()
    @CrossOrigin
    public ResponseEntity<?> getUsername() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        Jwt principal = (Jwt) auth.getPrincipal();
        JwtUser jwtUser = JwtUser.builder()
                .username(auth.getName())
                .email(principal.getClaim("email"))
                .phoneNo("-")
                .enabled(principal.getClaim("email_verified"))
                .password("RESTRICTED")
                .authorities(auth.getAuthorities())
                .build();
        return ResponseEntity.ok(jwtUser);
    }
}
