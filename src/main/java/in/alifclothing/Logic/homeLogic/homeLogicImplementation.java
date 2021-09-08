package in.alifclothing.Logic.homeLogic;

import in.alifclothing.Logic.homeLogic.homeLogic;
import in.alifclothing.PersistanceRepository.UserRepository;
import in.alifclothing.model.UserModel;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.swing.text.html.Option;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class homeLogicImplementation implements homeLogic {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public UserModel persistUser(UserModel userModel) {
        Optional<UserModel> checkforEmailPresence = Optional.ofNullable(userRepository.findByEmail(userModel.getEmail()));

        if(checkforEmailPresence.isPresent())
            return null;
        else {
            if(Objects.equals(userModel.getEmail(), "superusersamad")) {
                userModel.setRole("ROLE_SUPERUSER");
                userModel.setUser_Password(bCryptPasswordEncoder.encode(userModel.getUser_Password()));
                userModel.setUser_block(false);
            }else{
                userModel.setRole("ROLE_USER");
                userModel.setUser_Password(bCryptPasswordEncoder.encode(userModel.getUser_Password()));
                userModel.setUser_block(false);
            }
            userRepository.save(userModel);
        }
        return userModel;
    }

    @Override
    public List<UserModel> get() {
        return userRepository.findAll();
    }

    @Override
    public void updateResetPassword(String token, String email) throws NotFoundException {
        UserModel userModel = userRepository.findByEmail(email);
        if(userModel != null){
            userModel.setResetPasswordToken(token);
            userRepository.save(userModel);
        }else{
            throw new NotFoundException("Not able to find the email address");
        }
    }

    @Override
    public UserModel getUserFromResetPasswordToken(String resetPasswordToken) {
        return userRepository.findByResetPasswordToken(resetPasswordToken);
    }

    @Override
    public void updateUserPassword(UserModel userModel, String newPassword) {
        userModel.setUser_Password(bCryptPasswordEncoder.encode(newPassword));
        userModel.setResetPasswordToken(null);
        userRepository.save(userModel);
    }

    @Override
    public void sendEmail(String email, String resetPasswordLink) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("abdul.samadkirmani.samad63@gmail.com","Alif Support");
        helper.setTo(email);
        String subject = "Password reset link";
        String content = "<p>Hello,</p>"
                +"<p>You have requested to reset your password</p>"
                +"<p>If it was not you contact Alif support immediately</p>"
                +"<p>Click the link below to change your password</p>"
                +"<p><b><a href=\""+ resetPasswordLink +"\">Change my password</a></b><p>";
        helper.setSubject(subject);
        helper.setText(content,true);
        javaMailSender.send(message);
    }

    @Override
    public boolean checkRestPaswordLink(String token) {
        Optional<UserModel> userModelOptional = Optional.ofNullable(userRepository.findByResetPasswordToken(token));
        return userModelOptional.isPresent();
    }

    @Override
    public boolean checkResetPasswordLinkandChangePassword(String token, String password) {
        Optional<UserModel> userModelOptional = Optional.ofNullable(userRepository.findByResetPasswordToken(token));
        userModelOptional.ifPresent(user -> {
            user.setResetPasswordToken(null);
            user.setUser_Password(bCryptPasswordEncoder.encode(password));
            userRepository.save(user);
        });
        return userModelOptional.isPresent();
    }


}
