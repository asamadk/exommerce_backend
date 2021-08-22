package in.alifclothing.security;

import in.alifclothing.PersistanceRepository.UserRepository;
import in.alifclothing.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class userDetailsServiceImplementation implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserModel user = userRepository.findByEmail(s);

        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }

        userDetailsImplementation userDetailsImplementation;
        userDetailsImplementation = new userDetailsImplementation(user);
        return userDetailsImplementation;
    }
}
