package cu.havanaclub.userservice.model;


import lombok.Builder;

import java.util.List;

@Builder
public record UserDTO(String username, String email, String firstName, String lastName, String password,
                      List<String> roles, boolean enabled) {

}
