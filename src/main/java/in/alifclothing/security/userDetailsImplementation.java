package in.alifclothing.security;

import in.alifclothing.model.UserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class userDetailsImplementation implements UserDetails {

    private UserModel userModel;

    public userDetailsImplementation(UserModel userModel) {
        super();
        this.userModel = userModel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userModel.getRole());
        return List.of(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        return userModel.getUser_Password();
    }

    @Override
    public String getUsername() {
        return userModel.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !userModel.isUser_block();
    }
}
