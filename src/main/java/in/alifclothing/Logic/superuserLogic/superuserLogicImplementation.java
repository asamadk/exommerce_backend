package in.alifclothing.Logic.superuserLogic;

import in.alifclothing.PersistanceRepository.UserRepository;
import in.alifclothing.model.UserModel;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class superuserLogicImplementation implements superuserLogic{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public UserModel getuserownprofile(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserModel updateProfile(UserModel userModel, String email) {
        UserModel user = userRepository.findByEmail(email);
        userModel.setUser_Password(user.getUser_Password());
        userModel.setUser_registration_Date(user.getUser_registration_Date());
        userModel.setRole("ROLE_SUPERUSER");
        userModel.setUser_id(user.getUser_id());
        userModel.setUser_block(user.isUser_block());
        userModel.setUser_email_verified(user.isUser_email_verified());

        return userRepository.save(userModel);
    }


    @Override
    public UserModel createUser(UserModel userModel) {
        Optional<UserModel> checkforemailpresence = Optional.ofNullable(userRepository.findByEmail(userModel.getEmail()));
        if(checkforemailpresence.isPresent())return null;
        else {
            userModel.setRole("ROLE_USER");
            userModel.setUser_block(false);
            userModel.setUser_Password(passwordEncoder.encode(userModel.getUser_Password()));
            long millis = System.currentTimeMillis();
            userModel.setUser_registration_Date(new Date(millis));
            userRepository.save(userModel);
        }
        return userModel;
    }

    @Override
    public UserModel createAdmin(UserModel userModel) {
        Optional<UserModel> checkforemailpresence = Optional.ofNullable(userRepository.findByEmail(userModel.getEmail()));
        if(checkforemailpresence.isPresent())return null;
        else {
            userModel.setRole("ROLE_ADMIN");
            userModel.setUser_block(false);
            userModel.setUser_Password(passwordEncoder.encode(userModel.getUser_Password()));
            long millis = System.currentTimeMillis();
            userModel.setUser_registration_Date(new Date(millis));
            userRepository.save(userModel);
        }
        return userModel;
    }


    @Override
    public List<UserModel> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserModel getSingleUserByUserId(Integer user_id) {
        return userRepository.findById(user_id).orElse(null);
    }

    @Override
    public boolean deleteUser(Integer uid) {

        boolean result = userRepository.existsById(uid);
        if(result)
            userRepository.deleteById(uid);
        return result;
    }

    @Override
    public boolean blockandunblockUser(Integer uid) {
        Optional<UserModel> userModelOptional = userRepository.findById(uid);
        List<UserModel> userModelList = userModelOptional.stream().collect(Collectors.toList());
        UserModel user = userModelList.get(0);
        user.setUser_block(!user.isUser_block());
        userRepository.save(user);
        return userModelOptional.isPresent();
    }
}
