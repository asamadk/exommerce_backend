package in.alifclothing.Logic.adminLogic;

import in.alifclothing.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface adminLogic {

     List<ProductModel> getAllProducts();

     Optional<ProductModel> getSingleProduct(Integer pid);

     Optional<ProductModel> updateProduct(ProductModel productModel,Integer pid);

     ProductModel getproductJSON(String ProductModel,MultipartFile[] files,String catid);

     boolean deleteproduct(Integer pid);

     OptionModel addSize(OptionModel optionModel);

     List<OptionModel> getAllSizes();

     boolean addSizeToProduct(Integer product_id, Integer sizeoption_id);

     boolean deleteSizeFromProduct(Integer product_id, Integer sizeoption_id);

     CategoryModel addCategory(CategoryModel categoryModel);

     List<CategoryModel> getAllCategories();

     boolean deleteCategory(Integer cat_id);

     List<UserModel> getUsers();

     CouponsModel addCoupon(CouponsModel couponsModel);

    List<CouponsModel> getAllCoupons();

     Optional<CouponsModel> getCoupon(Integer cid);

     boolean deleteCoupon(Integer cid);

     boolean addBanner(MultipartFile[] files,MultipartFile file1,MultipartFile file2,MultipartFile file3);

     List<BannerModel> getBanners();

     boolean deleteAllBanners();

     boolean createOrderStatus(OrderStatus orderStatus);

     List<OrderStatus> findAllOrderStatus();

     boolean deleteOrderStatusById(Integer order_status_id);

     ShoppingCartModel getUsersCartByUserId(Integer user_id);

     List<OrderModel> getUsersOrdersByUserId(Integer user_id);

     boolean deleteUsersOrderByOrderId(Integer order_id);

     boolean updateOrdersStatusOfOneOrder(Integer order_id, Integer orderstatus_id);

     List<OrderModel> getAllOrdersofAllUsers();

     OrderModel getSingleOrderByOrderId(Integer order_id);
}
