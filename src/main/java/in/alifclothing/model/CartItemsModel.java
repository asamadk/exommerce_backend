//package in.alifclothing.model;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//import javax.persistence.*;
//import java.util.List;
//
//@Entity
//@Table(name = "cartItems")
//public class CartItemsModel {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private int cart_id;
//    @ManyToMany//many to many
//    private List<ProductModel> productModelList;
//    @JsonIgnore
//    @ManyToMany
//    private List<ShoppingCartModel> shoppingCartModel;
//
//    public CartItemsModel(List<ProductModel> productModelList, List<ShoppingCartModel> shoppingCartModel) {
//        this.productModelList = productModelList;
//        this.shoppingCartModel = shoppingCartModel;
//    }
//
//    public CartItemsModel() {
//
//    }
//
//    public int getCart_id() {
//        return cart_id;
//    }
//
//    public void setCart_id(int cart_id) {
//        this.cart_id = cart_id;
//    }
//
//    public List<ProductModel> getProductModelList() {
//        return productModelList;
//    }
//
//    public void setProductModelList(List<ProductModel> productModelList) {
//        this.productModelList = productModelList;
//    }
//
//
//    public List<ShoppingCartModel> getShoppingCartModel() {
//        return shoppingCartModel;
//    }
//
//    public void setShoppingCartModel(List<ShoppingCartModel> shoppingCartModel) {
//        this.shoppingCartModel = shoppingCartModel;
//    }
//
//}
