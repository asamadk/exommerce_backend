package in.alifclothing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wishlist")
public class WishlistModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int wishlistId;
    @OneToOne
    @JsonIgnore
    private UserModel userModel;
    @ManyToMany
    private List<ProductModel> productModelList;
    private Date creation_Date;

    public WishlistModel(UserModel userModel, List<ProductModel> productModelList, Date creation_Date) {
        this.userModel = userModel;
        this.productModelList = productModelList;
        this.creation_Date = creation_Date;
    }

    public WishlistModel(UserModel userModel, List<ProductModel> productModelList) {
        this.userModel = userModel;
        this.productModelList = productModelList;
    }

    public WishlistModel() {

    }

    public int getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public Date getCreation_Date() {
        return creation_Date;
    }

    public void setCreation_Date(Date creation_Date) {
        this.creation_Date = creation_Date;
    }

    public List<ProductModel> getProductModelList() {
        return productModelList;
    }

    public void setProductModelList(List<ProductModel> productModelList) {
        this.productModelList = productModelList;
    }
}
