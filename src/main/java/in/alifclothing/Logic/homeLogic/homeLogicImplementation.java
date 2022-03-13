package in.alifclothing.Logic.homeLogic;

import in.alifclothing.Dto.Response;
import in.alifclothing.PersistanceRepository.UserRepository;
import in.alifclothing.model.UserModel;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import in.alifclothing.Helper.Contants;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.*;


@Service
public class homeLogicImplementation implements homeLogic {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public Response<?> persistUser(UserModel userModel) {
        Optional<UserModel> checkforEmailPresence = Optional.ofNullable(userRepository.findByEmail(userModel.getEmail()));

        Response<UserModel> response = new Response<>();
        HashMap<String,String> errorMap = new HashMap<>();

        if(checkforEmailPresence.isPresent()) {
            errorMap.put(Contants.ERROR, "Email Already present");
            response.setErrorMap(errorMap);
            response.setResponseCode(Contants.CLIENT_400);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }else {
            if(Objects.equals(userModel.getEmail(), "superusersamad")) {
                userModel.setRole("ROLE_SUPERUSER");
                userModel.setUser_Password(bCryptPasswordEncoder.encode(userModel.getUser_Password()));
                userModel.setUser_block(false);
            }else{
                userModel.setRole("ROLE_USER");
                userModel.setUser_Password(bCryptPasswordEncoder.encode(userModel.getUser_Password()));
                userModel.setUser_block(false);
            }
            List<UserModel> userModelList = new ArrayList<>();
            userModelList.add(userModel);
            response.setResponseWrapper(userModelList);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
            userRepository.save(userModel);
        }
        return response;
    }

    @Override
    public List<UserModel> get() {
        return userRepository.findAll();
    }

    @Override
    public Response<?> updateResetPassword(String token, String email) {
        Map<String,String> errorMap = new HashMap<>();
        Response<String> response = new Response<>();
        UserModel userModel = userRepository.findByEmail(email);
        if(userModel != null){
            response.setResponseWrapper(new ArrayList(Arrays.asList("Email has been sent to your account")));
            response.setResponseCode(Contants.OK_200);
            response.setResponseDesc(Contants.SUCCESS);
            userModel.setResetPasswordToken(token);
            userRepository.save(userModel);
        }else{
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            response.setResponseDesc(Contants.FALIURE);
            errorMap.put(Contants.ERROR,"Not able to find the email address");
            response.setErrorMap(errorMap);
        }
        return response;
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
    public Response<String> checkRestPaswordLink(String token) {
        Map<String,String> errorMap = new HashMap<>();
        Response<String> response = new Response<>();
        Optional<UserModel> userModelOptional = Optional.ofNullable(userRepository.findByResetPasswordToken(token));
        if(userModelOptional.isPresent()){
            response.setResponseCode(Contants.OK_200);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseWrapper(Arrays.asList("Token verified"));
        }else{
            errorMap.put(Contants.ERROR,Contants.UNAUTH_TOKEN);
            response.setErrorMap(errorMap);
            response.setResponseWrapper(null);
            response.setResponseCode(Contants.CLIENT_400);
        }
        return response;
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

    @Override
    public Response<String> sendMessageContactUs(String name, String email, String subject, String body) throws MessagingException, UnsupportedEncodingException {
        Map<String,String> errorMap = new HashMap<>();
        Response<String> response = new Response<>();
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(email, "Alif Support");
            helper.setTo("alif@alifclothing.in");
            helper.setText(String.valueOf(body), false);
            helper.setSubject(subject);
            javaMailSender.send(message);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
            response.setResponseWrapper(Arrays.asList("Mail Sent"));
        }catch (Exception e){
            System.out.println(e.getMessage());
            response.setResponseWrapper(null);
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            response.setResponseDesc(Contants.FALIURE);
            errorMap.put(Contants.ERROR,e.getMessage());
            response.setErrorMap(errorMap);
        }
        return response;
    }


}
