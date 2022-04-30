package in.alifclothing.Controllers.User;

import in.alifclothing.Dto.ChangePasswordRequest;
import in.alifclothing.Dto.Response;
import in.alifclothing.Helper.Contants;
import in.alifclothing.Logic.userLogic.userLogic;
import in.alifclothing.model.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "http://localhost:3000", originPatterns = "https://master.d2pzdecn8wow21.amplifyapp.com/")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private userLogic userLogic;

    @PostMapping("/cart/{product_id}")
    public ResponseEntity<Response<ShoppingCartModel>> addtocart(@PathVariable("product_id") Integer product_id, Principal principal){

        Response<ShoppingCartModel> response = userLogic.addProductToCart(product_id,principal.getName());
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<ShoppingCartModel>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)){
                return new ResponseEntity<Response<ShoppingCartModel>>(response,HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Response<ShoppingCartModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @GetMapping("/cart")
    public ResponseEntity<Response<ShoppingCartModel>> getcart(Principal principal){

        Response<ShoppingCartModel> response = userLogic.getUserCart(principal.getName());
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<ShoppingCartModel>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)){
                return new ResponseEntity<Response<ShoppingCartModel>>(response,HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Response<ShoppingCartModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/cart/{cart_id}/{product_id}")
    public ResponseEntity<Response<String>> deletefromcart(@PathVariable("product_id") Integer product_id,@PathVariable("cart_id") Integer cart_id){

        Response<String> response = userLogic.deleteProductFromCart(product_id,cart_id);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<String>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)){
                return new ResponseEntity<Response<String>>(response,HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Response<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //coupon model as body
    @PostMapping("/cart/coupon")
    public ResponseEntity<Response<String>> addcoupon(@RequestParam("couponName") String couponName,@RequestParam("cartId") String cartId){

        Response<String> response = userLogic.addCouponToCart(couponName,Integer.parseInt(cartId));
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<String>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)) {
                return new ResponseEntity<Response<String>>(response, HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Response<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @DeleteMapping("/cart/coupon/{cart_id}")
    public ResponseEntity<Response<String>> deletecoupon(@PathVariable("cart_id") Integer cartId){

        Response<String> response = userLogic.deleteCouponFromCart(cartId);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<String>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)){
                return new ResponseEntity<Response<String>>(response,HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Response<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/orders")
    public void nonExposedDeleteAllOrders(Principal principal){
        userLogic.deleteAllOrdersNonExposedMethod(principal.getName());
    }

    @GetMapping("/product/{product_id}")
    public ResponseEntity<Response<ProductModel>> getProduct(@PathVariable("product_id") Integer product_id){
        Response<ProductModel> response = userLogic.getSingleProduct(product_id);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<ProductModel>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)){
                return new ResponseEntity<Response<ProductModel>>(response,HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Response<ProductModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/product/category/{category_id}")
    public ResponseEntity<Response<ProductModel>> getProductsByCategory(@PathVariable("category_id") Integer category_id){
        Response<ProductModel> response = userLogic.getAllProductsByCategory(category_id);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<ProductModel>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)){
                return new ResponseEntity<Response<ProductModel>>(response,HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Response<ProductModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @GetMapping("/categories")
//    public ResponseEntity<Response<CategoryModel>> getCategories(){
//        Response<CategoryModel> response = userLogic.getAllCategories();
//        if(response.getErrorMap() == null){
//            return new ResponseEntity<Response<CategoryModel>>(response,HttpStatus.OK);
//        }
//
//        return new ResponseEntity<Response<CategoryModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @GetMapping("/find")
    public ResponseEntity<Response<UserModel>> showUserDetails(Principal principal){

        Response<UserModel> response = userLogic.getUser(principal.getName());
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<UserModel>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)){
                return new ResponseEntity<Response<UserModel>>(response,HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Response<UserModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update/{user_id}")
    public ResponseEntity<Response<UserModel>> updateUser(@RequestBody UserModel userModel,@PathVariable("user_id") Integer uid){

        Response<UserModel> response = userLogic.updateCurrentUser(userModel,uid);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<UserModel>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)){
                return new ResponseEntity<Response<UserModel>>(response,HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Response<UserModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/coupons")
    public ResponseEntity<Response<CouponsModel>> getcoupons(){
        Response<CouponsModel> response = userLogic.getAllCoupons();
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<CouponsModel>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)){
                return new ResponseEntity<Response<CouponsModel>>(response,HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Response<CouponsModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping("/order")
    public ResponseEntity<Response<OrderModel>> createOrder(Principal principal){

        Response<OrderModel> response = userLogic.createOrderFromCart(principal.getName());
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<OrderModel>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)){
                return new ResponseEntity<Response<OrderModel>>(response,HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Response<OrderModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/orders")
    public ResponseEntity<Response<OrderModel>> getOrders(Principal principal){

        Response<OrderModel> response = userLogic.getAllOrdersOfUser(principal.getName());
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<OrderModel>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)){
                return new ResponseEntity<Response<OrderModel>>(response,HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Response<OrderModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/order/{order_id}")
    public ResponseEntity<Response<OrderModel>> getOrder(@PathVariable("order_id") Integer order_id){

        Response<OrderModel> response = userLogic.getSingleOrderOfUser(order_id);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<OrderModel>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)){
                return new ResponseEntity<Response<OrderModel>>(response,HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Response<OrderModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/wishlist/{product_id}")
    public ResponseEntity<Response<String>> addtowishlist(@PathVariable("product_id") Integer product_id, Principal principal){

        Response<String > response = userLogic.addProductToWishlist(product_id,principal.getName());
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<String>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)){
                return new ResponseEntity<Response<String>>(response,HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Response<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/wishlist")
    public ResponseEntity<Response<WishlistModel>> getWishlist(Principal principal){

        Response<WishlistModel > response = userLogic.getUserWishlist(principal.getName());
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<WishlistModel>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)){
                return new ResponseEntity<Response<WishlistModel>>(response,HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Response<WishlistModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/wishlist/{product_id}/{wishlist_id}")
    public ResponseEntity<Response<String>> deletefromwushlist(@PathVariable("product_id") Integer product_id,@PathVariable("wishlist_id") Integer wishlist_id){

        Response<String > response = userLogic.deleteProductFromWishlist(product_id,wishlist_id);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<String>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)){
                return new ResponseEntity<Response<String>>(response,HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Response<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    //TODO Payments

    @PostMapping("/order/return")
    public ResponseEntity<Response<String>> returnOrder(@RequestParam("order_id") String orderId){
        Response<String> response = userLogic.returnOrder(Integer.parseInt(orderId));
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<String>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)){
                return new ResponseEntity<Response<String>>(response,HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Response<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/password")
    public ResponseEntity<Response<String>> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,Principal principal){
        Response<String> response = userLogic.changePassword(changePasswordRequest,principal.getName());
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<String>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)){
                return new ResponseEntity<Response<String>>(response,HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Response<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
