package in.alifclothing.Logic.adminLogic;

import com.fasterxml.jackson.core.JsonProcessingException;
import in.alifclothing.Dto.Response;
import in.alifclothing.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface adminLogic {

     Response<ProductModel> getAllProducts();

     Response<ProductModel> getSingleProduct(Integer pid);

     Response<ProductModel> updateProduct(ProductModel productModel,Integer pid);

     Response<ProductModel> getproductJSON(String ProductModel,MultipartFile[] files,String catid) throws JsonProcessingException;

     Response<String> deleteproduct(Integer pid);

     Response<OptionModel> addSize(OptionModel optionModel);

     Response<OptionModel> getAllSizes();

     Response<String> addSizeToProduct(Integer product_id, Integer sizeoption_id);

     Response<String> deleteSizeFromProduct(Integer product_id, Integer sizeoption_id);

     Response<CategoryModel> addCategory(String categoryModel, MultipartFile file);

     Response<String> deleteCategory(Integer cat_id);

     Response<UserModel> getUsers();

     Response<CouponsModel> addCoupon(CouponsModel couponsModel);

    Response<CouponsModel> getAllCoupons();

     Response<CouponsModel> getCoupon(Integer cid);

     Response<String> deleteCoupon(Integer cid);

     Response<String> addBanner(MultipartFile[] files,MultipartFile file1,MultipartFile file2,MultipartFile file3);

     Response<BannerModel> getBanners();

     Response<String> deleteAllBanners();

     Response<String> createOrderStatus(OrderStatus orderStatus);

     Response<OrderStatus> findAllOrderStatus();

     Response<String> deleteOrderStatusById(Integer order_status_id);

     Response<ShoppingCartModel> getUsersCartByUserId(Integer user_id);

     Response<OrderModel> getUsersOrdersByUserId(String email);

     Response<String> deleteUsersOrderByOrderId(Integer order_id);

     Response<String> updateOrdersStatusOfOneOrder(Integer order_id, Integer orderstatus_id);

     Response<OrderModel> getAllOrdersofAllUsers();

     Response<OrderModel> getSingleOrderByOrderId(Integer order_id);
}
