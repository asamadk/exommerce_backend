package in.alifclothing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int user_id;
    private String user_Fname;
    private String user_Lname;
    private String email;
    @JsonIgnore
    private String user_Password;
    @JsonIgnore
    private String Role;
    private String user_City;
    private String user_State;
    private int user_zip;
    private boolean user_email_verified;
    private Date user_registration_Date;
    private int user_verification_code;
    private String user_phone_number;
    private String user_country;
    private String user_address1;
    private String user_address2;
    private String resetPasswordToken;
    private boolean user_block;
    @JsonIgnore
    @OneToOne(mappedBy = "userModel")
    private ShoppingCartModel shoppingCartModel;
    @JsonIgnore
    @OneToMany(mappedBy = "userModel")
    private List<OrderModel> orderModels;
    @JsonIgnore
    @OneToMany(mappedBy = "userModel")
    private List<UserProductInformation> userProductInformations;
    @JsonIgnore
    @OneToOne(mappedBy = "userModel")
    private WishlistModel wishlistModel;
    public UserModel(){}

    //constructor without userid
    public UserModel(String user_Fname, String user_Lname, String email, String user_Password,
                     String Role, String user_City, String user_State, int user_zip, boolean user_email_verified,
                     Date user_registration_Date, int user_verification_code, String user_phone_number, String user_country,
                     String user_address1, String user_address2, ShoppingCartModel shoppingCartModel,
                     List<OrderModel> orderModels,WishlistModel wishlistModel,boolean user_block,String resetPasswordToken) {
        this.user_Fname = user_Fname;
        this.user_Lname = user_Lname;
        this.email = email;
        this.user_Password = user_Password;
        this.Role = Role;
        this.user_City = user_City;
        this.user_State = user_State;
        this.user_zip = user_zip;
        this.user_email_verified = user_email_verified;
        this.user_registration_Date = user_registration_Date;
        this.user_verification_code = user_verification_code;
        this.user_phone_number = user_phone_number;
        this.user_country = user_country;
        this.user_address1 = user_address1;
        this.user_address2 = user_address2;
        this.shoppingCartModel = shoppingCartModel;
        this.orderModels = orderModels;
        this.wishlistModel = wishlistModel;
        this.user_block = user_block;
        this.resetPasswordToken = resetPasswordToken;
    }

    //Getters and setters


    public List<UserProductInformation> getUserProductInformations() {
        return userProductInformations;
    }

    public void setUserProductInformations(List<UserProductInformation> userProductInformations) {
        this.userProductInformations = userProductInformations;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_Fname() {
        return user_Fname;
    }

    public void setUser_Fname(String user_Fname) {
        this.user_Fname = user_Fname;
    }

    public String getUser_Lname() {
        return user_Lname;
    }

    public void setUser_Lname(String user_Lname) {
        this.user_Lname = user_Lname;
    }

    public String getUser_Password() {
        return user_Password;
    }

    public void setUser_Password(String user_Password) {
        this.user_Password = user_Password;
    }

    public String getUser_City() {
        return user_City;
    }

    public void setUser_City(String user_City) {
        this.user_City = user_City;
    }

    public String getUser_State() {
        return user_State;
    }

    public void setUser_State(String user_State) {
        this.user_State = user_State;
    }

    public int getUser_zip() {
        return user_zip;
    }

    public void setUser_zip(int user_zip) {
        this.user_zip = user_zip;
    }

    public boolean isUser_email_verified() {
        return user_email_verified;
    }

    public void setUser_email_verified(boolean user_email_verified) {
        this.user_email_verified = user_email_verified;
    }

    public Date getUser_registration_Date() {
        return user_registration_Date;
    }

    public void setUser_registration_Date(Date user_registration_Date) {
        this.user_registration_Date = user_registration_Date;
    }

    public int getUser_verification_code() {
        return user_verification_code;
    }

    public void setUser_verification_code(int user_verification_code) {
        this.user_verification_code = user_verification_code;
    }

    public String getUser_phone_number() {
        return user_phone_number;
    }

    public void setUser_phone_number(String user_phone_number) {
        this.user_phone_number = user_phone_number;
    }

    public String getUser_country() {
        return user_country;
    }

    public void setUser_country(String user_country) {
        this.user_country = user_country;
    }

    public String getUser_address1() {
        return user_address1;
    }

    public void setUser_address1(String user_address1) {
        this.user_address1 = user_address1;
    }

    public String getUser_address2() {
        return user_address2;
    }

    public void setUser_address2(String user_address2) {
        this.user_address2 = user_address2;
    }

    public ShoppingCartModel getShoppingCartModel() {
        return shoppingCartModel;
    }

    public void setShoppingCartModel(ShoppingCartModel shoppingCartModel) {
        this.shoppingCartModel = shoppingCartModel;
    }

    public List<OrderModel> getOrderModels() {
        return orderModels;
    }

    public void setOrderModels(List<OrderModel> orderModels) {
        this.orderModels = orderModels;
    }

    public WishlistModel getWishlistModel() {
        return wishlistModel;
    }

    public void setWishlistModel(WishlistModel wishlistModel) {
        this.wishlistModel = wishlistModel;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public boolean isUser_block() {
        return user_block;
    }

    public void setUser_block(boolean user_block) {
        this.user_block = user_block;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }
}
