package cu.havanaclub.userservice.service.impl;

import cu.havanaclub.userservice.model.UserConfigurations;
import cu.havanaclub.userservice.model.UserDTO;
import cu.havanaclub.userservice.service.KeycloakService;
import cu.havanaclub.userservice.util.KeycloakProvider;
import jakarta.ws.rs.core.Response;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class KeycloakServiceImpl implements KeycloakService {

    @Autowired
    KeycloakProvider keycloakProvider;

    /**
     * Method to list all Keycloak Users
     * @return The list of users.
     */
    @Override
    public List<UserRepresentation> findAllUsers() {
        return keycloakProvider.getRealmResource()
                .users()
                .list();
    }

    /**
     * Find a User for the username.
     * @param username The username.
     * @return List<UserRepresentation>
     */
    @Override
    public List<UserRepresentation> searchUserByUsername(String username) {
        return keycloakProvider.getRealmResource()
                .users()
                .searchByUsername(username, true);
    }

    /**
     * Method for create a user in Keycloak
     * @param userDTO User data.
     * @return UserRepresentation
     */
    @Override
    public List<UserRepresentation> createUser(@NonNull UserDTO userDTO) {
        int status = 0;
        UsersResource usersResource = keycloakProvider.getUserResource();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userDTO.firstName());
        userRepresentation.setLastName(userDTO.lastName());
        userRepresentation.setEmail(userDTO.email());
        userRepresentation.setUsername(userDTO.username());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

        Response response = usersResource.create(userRepresentation);

        if(response.getStatus() == 201) {
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/")+1);
            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(OAuth2Constants.PASSWORD);
            credentialRepresentation.setValue(userDTO.password());
            usersResource.get(userId).resetPassword(credentialRepresentation);

            RealmResource realmResource = keycloakProvider.getRealmResource();

            List<RoleRepresentation> roleRepresentations = null;
            if(userDTO.roles()==null || userDTO.roles().isEmpty()){
                roleRepresentations = List.of(realmResource.roles().get("user").toRepresentation());
            }
            else {
                roleRepresentations = realmResource.roles()
                        .list()
                        .stream()
                        .filter(role->userDTO.roles()
                                .stream()
                                .anyMatch(roleName->roleName.equalsIgnoreCase(role.getName())))
                        .toList();
            }
            realmResource.users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(roleRepresentations);

            return searchUserByUsername(userDTO.username());
        }
        else if (response.getStatus()==409) {
            log.error("User already exists!");
            return null;
        }
        log.error("Error creating user!");
        return null;
    }

    /**
     * Method to delete one user form Keycloak
     * @param username The username of the user
     * @return This method returns a UserRepresentation if the user is deleted successfully or null is not possible to delete.
     */
    @Override
    public UserRepresentation deleteUser(String username) {
        List<UserRepresentation> userRepresentations= searchUserByUsername(username);
        if(userRepresentations!=null && !userRepresentations.isEmpty()){
            keycloakProvider.getUserResource()
                    .get(userRepresentations.get(0).getId())
                    .remove();
            if(searchUserByUsername(username)!=null || searchUserByUsername(username).isEmpty())
                return userRepresentations.get(0);
        }
        return null;
    }

    /**
     * Method to update a user
     * @param userDTO User information
     * @return return a userRepresentation with the user data.
     */
    @Override
    public UserRepresentation updateUser(UserDTO userDTO) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        if(!userDTO.password().isEmpty()) {
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(OAuth2Constants.PASSWORD);
            credentialRepresentation.setValue(userDTO.password());
        }

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userDTO.firstName());
        userRepresentation.setLastName(userDTO.lastName());
        userRepresentation.setEmail(userDTO.email());
        userRepresentation.setUsername(userDTO.username());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

        if(!userDTO.password().isEmpty())
            userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

        List<UserRepresentation> userRepresentations = searchUserByUsername(userDTO.username());

        if(userRepresentations==null || userRepresentations.isEmpty())
            return null;

        UserResource usersResource = keycloakProvider.getUserResource().get(userRepresentations.get(0).getId());
        usersResource.update(userRepresentation);
        List<UserRepresentation> userRepresentations1 = searchUserByUsername(userDTO.username());

        if(userRepresentations1==null || userRepresentations1.isEmpty())
            return null;
        return userRepresentations1.get(0);
    }

    @Override
    public UserDTO getAuthenticatedUserData() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        Jwt principal = (Jwt) auth.getPrincipal();

        UserRepresentation userRepresentations = searchUserByUsername(auth.getName()).get(0);

        if(userRepresentations == null)
            log.error("An error has been occurred");

        return UserDTO.builder()
                .username(auth.getName())
                .email(userRepresentations.getEmail())
                .firstName(userRepresentations.getFirstName())
                .lastName(userRepresentations.getLastName())
                .enabled(userRepresentations.isEmailVerified())
                .password("RESTRICTED")
                .roles(auth.getAuthorities().stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority().endsWith("_ROLE")?
                                grantedAuthority.getAuthority().substring(0, grantedAuthority.getAuthority().lastIndexOf("_")):
                                grantedAuthority.getAuthority())
                        .toList())
                .build();
    }

    @Override
    public UserConfigurations getUserLang() {
        UserRepresentation userRepresentation = searchUserByUsername(getAuthenticatedUserData().username()).get(0);
        if(userRepresentation.getAttributes().get("lang")==null || userRepresentation.getAttributes().get("lang").isEmpty())
            setUserLang("es");
        UserConfigurations uc = UserConfigurations.builder()
                .lang(userRepresentation.getAttributes().get("lang").get(0))
                .build();
        return uc;
    }

    @Override
    public void setUserLang(String newLang) {
        UserRepresentation userRepresentation = searchUserByUsername(getAuthenticatedUserData().username()).get(0);
        userRepresentation.singleAttribute("lang", newLang);
        UserResource usersResource = keycloakProvider.getUserResource().get(userRepresentation.getId());
        usersResource.update(userRepresentation);
    }


}
