package in.alifclothing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class UserProductInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userProductInfoId;
    private Boolean isCustom;
    @ManyToOne
    @JsonIgnore
    private UserModel userModel;
    @ManyToOne
    private ProductModel productModel;
    @ManyToOne
    private OrderModel orderModel;
    private String sizeJSON;

    public UserProductInformation(Integer userProductInfoId, Boolean isCustom, UserModel userModel, ProductModel productModel, OrderModel orderModel) {
        this.userProductInfoId = userProductInfoId;
        this.isCustom = isCustom;
        this.userModel = userModel;
        this.productModel = productModel;
        this.orderModel = orderModel;
    }

    public String getSizeJSON() {
        return sizeJSON;
    }

    public void setSizeJSON(String sizeJSON) {
        this.sizeJSON = sizeJSON;
    }

    public UserProductInformation() {
    }

    public Integer getUserProductInfoId() {
        return userProductInfoId;
    }

    public void setUserProductInfoId(Integer userProductInfoId) {
        this.userProductInfoId = userProductInfoId;
    }

    public Boolean getCustom() {
        return isCustom;
    }

    public void setCustom(Boolean custom) {
        isCustom = custom;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public ProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }

    public OrderModel getOrderModel() {
        return orderModel;
    }

    public void setOrderModel(OrderModel orderModel) {
        this.orderModel = orderModel;
    }

    @Override
    public String toString() {
        return "UserProductInformation{" +
                "userProductInfoId=" + userProductInfoId +
                ", isCustom=" + isCustom +
                ", userModel=" + userModel +
                ", productModel=" + productModel +
                ", orderModel=" + orderModel +
                ", sizeJSON='" + sizeJSON + '\'' +
                '}';
    }
}
