package cu.havanaclub.ekinsadbreview.config;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Configuration
@Component
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationConverter jwtAuthenticationConverter;

    @Value("${server.url.prefix}")
    private String SERVER_URL_PREFIX;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        System.out.println("/"+SERVER_URL_PREFIX+"/pesajesLinea/swagger-ui/**");
        System.out.println("/"+SERVER_URL_PREFIX+"/pesajesLinea/api-docs/**");
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(http -> {
//                    http.anyRequest().permitAll();
                    http.requestMatchers("/"+SERVER_URL_PREFIX+"/pesajesLinea/swagger-ui/**").permitAll();
                    http.requestMatchers("/"+SERVER_URL_PREFIX+"/pesajesLinea/api-docs/**").permitAll();
                    http.requestMatchers("/actuator/**").permitAll();
                    http.anyRequest().authenticated();
                })
                .oauth2ResourceServer(oauth -> {
                    oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter));
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
