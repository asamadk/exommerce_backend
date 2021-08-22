package in.alifclothing.Controllers.Superuser;

import in.alifclothing.Logic.superuserLogic.superuserLogic;
import in.alifclothing.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/superuser")
public class superUserController {

    @Autowired
    private superuserLogic logic;

    @GetMapping("/editProfile")
    public UserModel editprofile(Principal principal){
        return logic.getuserownprofile(principal.getName());
    }

    @PutMapping("/updateProfile")
    public UserModel updateprofile(@RequestBody UserModel user,Principal principal){
        return logic.updateProfile(user,principal.getName());
    }

    @PostMapping("/createUser")
    public UserModel adduser(@RequestBody UserModel user){
        //if email already exist empty usermodel will be returned
        //user_id 0 then email already exists
        return logic.createUser(user);
    }

    @PostMapping("/createAdmin")
    public UserModel addAdmin(@RequestBody UserModel user){
        return logic.createAdmin(user);
    }

    @GetMapping("/users")
    public List<UserModel> getuser(){
        //filter on based of admin and user by front end team
        return logic.getUsers();
    }

    @GetMapping("/user/{user_id}")
    public UserModel getSingleUser(@PathVariable("user_id") Integer user_id){
        return logic.getSingleUserByUserId(user_id);
    }

    @DeleteMapping("/deleteUser/{user_id}")
    public boolean deleteuser(@PathVariable("user_id") Integer user_id){
        return logic.deleteUser(user_id);
    }

    @PostMapping("/active/{user_id}")
    public boolean blockUnblockuser(@PathVariable("user_id") Integer user_id){
            //if user is blocked it will unblock else it will block the user
            return logic.blockandunblockUser(user_id);
    }

}
