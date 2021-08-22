package in.alifclothing.Logic.homeLogic;

import in.alifclothing.Logic.homeLogic.homeLogic;
import in.alifclothing.PersistanceRepository.UserRepository;
import in.alifclothing.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


@Service
public class homeLogicImplementation implements homeLogic {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserModel persistUser(UserModel userModel) {
        Optional<UserModel> checkforEmailPresence = Optional.ofNullable(userRepository.findByEmail(userModel.getEmail()));

        if(checkforEmailPresence.isPresent())
            return null;
        else {
            userModel.setRole("ROLE_SUPERUSER");
            userModel.setUser_Password(bCryptPasswordEncoder.encode(userModel.getUser_Password()));
            userModel.setUser_block(false);
            userRepository.save(userModel);
        }
        return userModel;
    }

    @Override
    public List<UserModel> get() {
        return userRepository.findAll();
    }

}
