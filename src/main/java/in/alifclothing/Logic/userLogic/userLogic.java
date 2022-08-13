package in.alifclothing.Logic.userLogic;

import in.alifclothing.Dto.ChangePasswordRequest;
import in.alifclothing.Dto.Response;
import in.alifclothing.model.*;
import org.json.JSONObject;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public interface userLogic {
    Response<ShoppingCartModel> addProductToCart(Integer product_id, String username, String sizeObject);

    Response<Object> getUserCart(String email);

    Response<String> deleteProductFromCart(Integer product_id, Integer cart_id, Integer userProductInfoId);

    Response<String > addCouponToCart(String couponName,Integer cartId);

    Response<CouponsModel> getAllCoupons();

    Response<String> deleteCouponFromCart(Integer cartId);

    Response<UserModel> getUser(String email);

    Response<UserModel> updateCurrentUser(UserModel userModel,Integer uid);

    Response<OrderModel> createOrderFromCart(String email, String couponName);

    Response<OrderModel> getAllOrdersOfUser(String email,String page,String size);

    Response<OrderModel> getSingleOrderOfUser(Integer order_id);

    Response<ProductModel> getAllProducts(String orderBy,String page,String limit);

    Response<ProductModel> getSingleProduct(Integer product_id);

    void deleteAllOrdersNonExposedMethod(String email);

    Response<ProductModel> getAllProductsByCategory(Integer category_id,String page,String size);

//    Response<ProductModel> getAllProductsByCategory(Integer category_id);

    Response<String> addProductToWishlist(Integer product_id, String email);

    Response<WishlistModel> getUserWishlist(String email,String page,String size);

    Response<String> deleteProductFromWishlist(Integer product_id,Integer wishlist_id);

    Response<CategoryModel> getAllCategories();

    Response<ProductModel> searchProducts(String productName);

    Response<String> returnOrder(Integer orderId);

    Response<String> changePassword(ChangePasswordRequest changePasswordModel,String userEmail);

    Response<String> confirmOrder(OrderModel orderModel);
}
