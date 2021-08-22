package in.alifclothing.Controllers.User;

import in.alifclothing.Logic.userLogic.userLogic;
import in.alifclothing.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private userLogic userLogic;

    @PostMapping("/cart/{product_id}")
    public ShoppingCartModel addtocart(@PathVariable("product_id") Integer product_id, Principal principal){
        return userLogic.addProductToCart(product_id,principal.getName());
    }

    @GetMapping("/cart")
    public ShoppingCartModel getcart(Principal principal){
        return userLogic.getUserCart(principal.getName());
    }

    @DeleteMapping("/cart/{cart_id}/{product_id}")
    public ResponseEntity<String> deletefromcart(@PathVariable("product_id") Integer product_id,@PathVariable("cart_id") Integer cart_id){
        if(userLogic.deleteProductFromCart(product_id,cart_id).get()) {
            return ResponseEntity.ok().body("Delete sucessfull");
        }
        return ResponseEntity.internalServerError().body("Something went wrong");
    }

    //coupon model as body
    @PostMapping("/cart/coupon/{cart_id}")
    public ResponseEntity<String> addcoupon(@RequestBody CouponsModel couponsModel,@PathVariable("cart_id") Integer cartId){
        if(userLogic.addCouponToCart(couponsModel,cartId))return ResponseEntity.ok().body("Coupon added succesfully");
        return ResponseEntity.internalServerError().body("Coupon could not be added");
    }


    @DeleteMapping("/cart/coupon/{cart_id}")
    public ResponseEntity<String> deletecoupon(@PathVariable("cart_id") Integer cartId){
        if (userLogic.deleteCouponFromCart(cartId))return ResponseEntity.ok("Deleted successfully");
        return ResponseEntity.status(404).body("Not Deleted");
    }

    @GetMapping("/products")
    public List<ProductModel> getProducts(){
        return userLogic.getAllProducts();
    }

    @GetMapping("/product/{product_id}")
    public ProductModel getProduct(@PathVariable("product_id") Integer product_id){
        return userLogic.getSingleProduct(product_id);
    }

    @GetMapping("/product/category/{category_id}")
    public List<ProductModel> getProductsByCategory(@PathVariable("category_id") Integer category_id){
        return userLogic.getAllProductsByCategory(category_id);
    }

    @GetMapping("/find")
    public UserModel showUserDetails(Principal principal){
        return userLogic.getUser(principal.getName());
    }

    @PutMapping("/update/{user_id}")
    public ResponseEntity<String> updateUser(@RequestBody UserModel userModel,@PathVariable("user_id") Integer uid){
        if(userLogic.updateCurrentUser(userModel,uid))return ResponseEntity.ok("User updated");
        else return ResponseEntity.status(404).body("User not updated");
    }

    @GetMapping("/coupons")
    public List<CouponsModel> getcoupons(){
        return userLogic.getAllCoupons();
    }


    @PostMapping("/order")
    public ResponseEntity<String > createOrder(Principal principal){
        if(userLogic.createOrderFromCart(principal.getName()))return ResponseEntity.ok("Order created successfully");
        return ResponseEntity.unprocessableEntity().body("Something went wrong");
    }

    @GetMapping("/orders")
    public List<OrderModel> getOrders(Principal principal){
        return userLogic.getAllOrdersOfUser(principal.getName());
    }

    @GetMapping("/order/{order_id}")
    public OrderModel getOrder(@PathVariable("order_id") Integer order_id){
        return userLogic.getSingleOrderOfUser(order_id);
    }

    @PostMapping("/wishlist/{product_id}")
    public ResponseEntity<String> addtowishlist(@PathVariable("product_id") Integer product_id, Principal principal){
         if(userLogic.addProductToWishlist(product_id,principal.getName()))
             return ResponseEntity.ok("Added to wishlist");
         return ResponseEntity.internalServerError().body("Something went wwrong");
    }

    @GetMapping("/wishlist")
    public WishlistModel getWishlist(Principal principal){
       return userLogic.getUserWishlist(principal.getName());
    }

    @DeleteMapping("/wishlist/{product_id}/{wishlist_id}")
    public ResponseEntity<String> deletefromwushlist(@PathVariable("product_id") Integer product_id,@PathVariable("wishlist_id") Integer wishlist_id){
        return userLogic.deleteProductFromWishlist(product_id,wishlist_id)?ResponseEntity.ok("product deleted"):ResponseEntity.internalServerError().body("Something went wrong");
    }
    //TODO Payments


}
