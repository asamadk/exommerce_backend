package in.alifclothing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
public class ProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int product_id;
    private String product_name;
    private int product_weight;
    private float product_price;
    private float product_real_price;
    private String product_small_Desc;
    private String product_long_Desc;
    private String product_img1;
    private String product_img2;
    private String product_img3;
    private String product_img4;
    private boolean avaialable;
    private Date updateDate;
    @ManyToOne
    @JoinColumn
    private CategoryModel categoryModel;
    @JsonIgnore
    @ManyToMany(mappedBy = "productModelList")
    private List<ShoppingCartModel> shoppingCartModels;
    @JsonIgnore
    @ManyToMany(mappedBy = "productModelList")
    private List<WishlistModel> wishlistModel;
    @ManyToMany()
    private List<OptionModel> optionModel;
    @JsonIgnore
    @ManyToMany(mappedBy = "productModelList")
    private List<OrderModel> orderModel;

    public ProductModel(String product_name, int product_weight, float product_price,float product_real_price,
                        String product_small_Desc, String product_long_Desc, String product_img1,
                        String product_img2, String product_img3, String product_img4, CategoryModel categoryModel,List<ShoppingCartModel> shoppingCartModel,
                        boolean avaialable,Date updateDate, List<WishlistModel> wishlistModel
                        ,List<OrderModel> orderModel) {
        this.product_name = product_name;
        this.product_weight = product_weight;
        this.product_price = product_price;
        this.product_real_price = product_real_price;
        this.product_small_Desc = product_small_Desc;
        this.product_long_Desc = product_long_Desc;
        this.product_img1 = product_img1;
        this.product_img2 = product_img2;
        this.product_img3 = product_img3;
        this.product_img4 = product_img4;
        this.categoryModel = categoryModel;
        this.shoppingCartModels = shoppingCartModel;
        this.avaialable = avaialable;
        this.updateDate = updateDate;
        this.wishlistModel = wishlistModel;
        this.orderModel = orderModel;
    }

    public ProductModel() {

    }


    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_weight() {
        return product_weight;
    }

    public void setProduct_weight(int product_weight) {
        this.product_weight = product_weight;
    }

    public float getProduct_price() {
        return product_price;
    }

    public void setProduct_price(float product_price) {
        this.product_price = product_price;
    }

    public String getProduct_small_Desc() {
        return product_small_Desc;
    }

    public void setProduct_small_Desc(String product_small_Desc) {
        this.product_small_Desc = product_small_Desc;
    }

    public String getProduct_long_Desc() {
        return product_long_Desc;
    }

    public void setProduct_long_Desc(String product_long_Desc) {
        this.product_long_Desc = product_long_Desc;
    }

    public String getProduct_img1() {
        return product_img1;
    }

    public void setProduct_img1(String product_img1) {
        this.product_img1 = product_img1;
    }

    public String getProduct_img2() {
        return product_img2;
    }

    public void setProduct_img2(String product_img2) {
        this.product_img2 = product_img2;
    }

    public String getProduct_img3() {
        return product_img3;
    }

    public void setProduct_img3(String product_img3) {
        this.product_img3 = product_img3;
    }

    public String getProduct_img4() {
        return product_img4;
    }

    public void setProduct_img4(String product_img4) {
        this.product_img4 = product_img4;
    }

    public CategoryModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(CategoryModel categoryModel) {
        this.categoryModel = categoryModel;
    }

    public boolean isAvaialable() {
        return avaialable;
    }

    public void setAvaialable(boolean avaialable) {
        this.avaialable = avaialable;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public List<ShoppingCartModel> getShoppingCartModels() {
        return shoppingCartModels;
    }

    public void setShoppingCartModels(List<ShoppingCartModel> shoppingCartModels) {
        this.shoppingCartModels = shoppingCartModels;
    }

    public List<WishlistModel> getWishlistModel() {
        return wishlistModel;
    }

    public void setWishlistModel(List<WishlistModel> wishlistModel) {
        this.wishlistModel = wishlistModel;
    }

    public List<OptionModel> getOptionModel() {
        return optionModel;
    }

    public void setOptionModel(List<OptionModel> optionModel) {
        this.optionModel = optionModel;
    }

    public List<OrderModel> getOrderModel() {
        return orderModel;
    }

    public void setOrderModel(List<OrderModel> orderModel) {
        this.orderModel = orderModel;
    }

    public float getProduct_real_price() {
        return product_real_price;
    }

    public void setProduct_real_price(float product_real_price) {
        this.product_real_price = product_real_price;
    }

}
