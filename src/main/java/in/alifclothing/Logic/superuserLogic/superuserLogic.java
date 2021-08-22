package in.alifclothing.Logic.superuserLogic;

import in.alifclothing.model.UserModel;

import java.util.List;

public interface superuserLogic {

    public UserModel getuserownprofile(String email);

    public UserModel updateProfile(UserModel userModel,String email);

    public UserModel createUser(UserModel userModel);

    public UserModel createAdmin(UserModel userModel);

    public List<UserModel> getUsers();

    public UserModel getSingleUserByUserId(Integer user_id);

    public boolean deleteUser(Integer uid);

    public boolean blockandunblockUser(Integer uid);


}
