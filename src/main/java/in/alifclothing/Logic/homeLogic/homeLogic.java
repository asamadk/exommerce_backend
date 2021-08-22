package in.alifclothing.Logic.homeLogic;

import in.alifclothing.model.UserModel;

import java.util.List;
import java.util.Optional;

public interface homeLogic {

    public UserModel persistUser(UserModel userModel);

    public List<UserModel> get();

}
