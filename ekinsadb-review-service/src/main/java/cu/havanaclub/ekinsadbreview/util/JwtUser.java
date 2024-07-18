package cu.havanaclub.ekinsadbreview.util;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@Builder
public class JwtUser implements UserDetails {

    private final long id;
    private final String email;
    private final String phoneNo;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final boolean credentialsNonExpired;
    private final boolean accountNonLocked;
    private final boolean accountNonExpired;

    private final Collection<? extends GrantedAuthority> authorities;
}
