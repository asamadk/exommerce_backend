package in.alifclothing.Logic.homeLogic;

import in.alifclothing.Dto.Response;
import in.alifclothing.model.UserModel;
import javassist.NotFoundException;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

public interface homeLogic {

    public Response<?> persistUser(UserModel userModel);

    public List<UserModel> get();

    public Response<?> updateResetPassword(String token, String email) throws NotFoundException;

    public UserModel getUserFromResetPasswordToken(String resetPasswordToken);

    public void updateUserPassword(UserModel userModel,String newPassword);

    public void sendEmail(String email, String resetPasswordLink) throws MessagingException, UnsupportedEncodingException;

    public boolean checkRestPaswordLink(String token);

    public boolean checkResetPasswordLinkandChangePassword(String token,String password);

}
