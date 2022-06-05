package in.alifclothing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name = "shoppingcartuser")
public class ShoppingCartModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ShoppingCartId;
    @OneToOne()
    @JsonIgnore
    private UserModel userModel;
    private Date shoppingCartDate;
    @ManyToMany
    private List<ProductModel> productModelList;
    private float totalAmountBeforeDiscount;
    private float total;
    @ManyToOne
    private CouponsModel couponsModel;
    private boolean couponUsed;
    private String razorpay_order_id;
    private Boolean isCustom;
    private String customSizeJSON;

    public ShoppingCartModel(UserModel userModel, Date shoppingCartDate,List<ProductModel> productModelList,
                             float total,CouponsModel couponsModel,boolean couponUsed, float totalAmountBeforeDiscount) {
        this.userModel = userModel;
        this.shoppingCartDate = shoppingCartDate;
        this.productModelList = productModelList;
        this.total = total;
        this.couponsModel = couponsModel;
        this.couponUsed = couponUsed;
        this.totalAmountBeforeDiscount = totalAmountBeforeDiscount;
    }

    public ShoppingCartModel(int shoppingCartId, UserModel userModel, Date shoppingCartDate, List<ProductModel> productModelList, float totalAmountBeforeDiscount, float total, CouponsModel couponsModel, boolean couponUsed, String razorpay_order_id, Boolean isCustom, String customSizeJSON) {
        ShoppingCartId = shoppingCartId;
        this.userModel = userModel;
        this.shoppingCartDate = shoppingCartDate;
        this.productModelList = productModelList;
        this.totalAmountBeforeDiscount = totalAmountBeforeDiscount;
        this.total = total;
        this.couponsModel = couponsModel;
        this.couponUsed = couponUsed;
        this.razorpay_order_id = razorpay_order_id;
        this.isCustom = isCustom;
        this.customSizeJSON = customSizeJSON;
    }

    public ShoppingCartModel(UserModel userModel, List<ProductModel> productModelList) {
        this.userModel = userModel;
        this.productModelList = productModelList;
    }

    public ShoppingCartModel() {

    }

    public Boolean getCustom() {
        return isCustom;
    }

    public void setCustom(Boolean custom) {
        isCustom = custom;
    }

    public String getCustomSizeJSON() {
        return customSizeJSON;
    }

    public void setCustomSizeJSON(String customSizeJSON) {
        this.customSizeJSON = customSizeJSON;
    }

    public int getShoppingCartId() {
        return ShoppingCartId;
    }

    public void setShoppingCartId(int shoppingCartId) {
        ShoppingCartId = shoppingCartId;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public Date getShoppingCartDate() {
        return shoppingCartDate;
    }

    public void setShoppingCartDate(Date shoppingCartDate) {
        this.shoppingCartDate = shoppingCartDate;
    }

    public List<ProductModel> getProductModelList() {
        return productModelList;
    }

    public void setProductModelList(List<ProductModel> productModelList) {
        this.productModelList = productModelList;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public CouponsModel getCouponsModel() {
        return couponsModel;
    }

    public void setCouponsModel(CouponsModel couponsModel) {
        this.couponsModel = couponsModel;
    }

    public boolean isCouponUsed() {
        return couponUsed;
    }

    public void setCouponUsed(boolean couponUsed) {
        this.couponUsed = couponUsed;
    }

    public float getTotalAmountBeforeDiscount() {
        return totalAmountBeforeDiscount;
    }

    public void setTotalAmountBeforeDiscount(float totalAmountBeforeDiscount) {
        this.totalAmountBeforeDiscount = totalAmountBeforeDiscount;
    }

    public String getRazorpay_order_id() {
        return razorpay_order_id;
    }

    public void setRazorpay_order_id(String razorpay_order_id) {
        this.razorpay_order_id = razorpay_order_id;
    }
}
