package cu.havanaclub.userservice.util;

import lombok.AllArgsConstructor;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakProvider {

    @Value("${keycloak.config.provider.server-url}")
    private String SERVER_URL;

    @Value("${keycloak.config.provider.realm-name}")
    private String REALM_NAME;

    @Value("${keycloak.config.provider.realm-master}")
    private String REALM_MASTER;

    @Value("${keycloak.config.provider.admin-cli}")
    private String ADMIN_CLI;

    @Value("${keycloak.config.provider.user-console}")
    private String USER_CONSOLE;

    @Value("${keycloak.config.provider.password-console}")
    private String PASSWORD_CONSOLE;

    @Value("${keycloak.config.provider.client-secret}")
    private String CLIENT_SECRET;

    private static RealmResource realmResource = null;

    public RealmResource getRealmResource(){
        if (realmResource == null)
            realmResource = KeycloakBuilder.builder()
                .serverUrl(SERVER_URL)
                .realm(REALM_MASTER)
                .clientId(ADMIN_CLI)
                .username(USER_CONSOLE)
                .password(PASSWORD_CONSOLE)
                .clientSecret(CLIENT_SECRET)
                .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(500).build())
                .build().realm(REALM_NAME);
        return realmResource;
    }

    public UsersResource getUserResource(){
        return getRealmResource().users();
    }
}
