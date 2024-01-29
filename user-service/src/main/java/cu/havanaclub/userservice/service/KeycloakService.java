package cu.havanaclub.userservice.service;

import cu.havanaclub.userservice.model.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface KeycloakService {

    List<UserRepresentation> findAllUsers();
    List<UserRepresentation> searchUserByUsername(String username);
    List<UserRepresentation> createUser(UserDTO userDTO);
    UserRepresentation deleteUser(String username);
    UserRepresentation updateUser(UserDTO userDTO);

}
