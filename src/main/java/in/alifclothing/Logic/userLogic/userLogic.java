package in.alifclothing.Logic.userLogic;

import in.alifclothing.model.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public interface userLogic {
    ShoppingCartModel addProductToCart(Integer product_id,String username);

    ShoppingCartModel getUserCart(String email);

    AtomicBoolean deleteProductFromCart(Integer product_id, Integer cart_id);

    boolean addCouponToCart(CouponsModel couponsModel,Integer cartId);

    List<CouponsModel> getAllCoupons();

    boolean deleteCouponFromCart(Integer cartId);

    UserModel getUser(String email);

    boolean updateCurrentUser(UserModel userModel,Integer uid);

    boolean createOrderFromCart(String email);

    List<OrderModel> getAllOrdersOfUser(String email);

    OrderModel getSingleOrderOfUser(Integer order_id);

    List<ProductModel> getAllProducts();

    ProductModel getSingleProduct(Integer product_id);

    List<ProductModel> getAllProductsByCategory(Integer category_id);

    boolean addProductToWishlist(Integer product_id, String email);

    WishlistModel getUserWishlist(String email);

    boolean deleteProductFromWishlist(Integer product_id,Integer wishlist_id);

    List<CategoryModel> getAllCategories();
}
