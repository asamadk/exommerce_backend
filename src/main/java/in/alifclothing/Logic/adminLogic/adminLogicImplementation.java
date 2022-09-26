package in.alifclothing.Logic.adminLogic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.alifclothing.Dto.Response;
import in.alifclothing.FileHandlerService.fileStorageService;
import in.alifclothing.Helper.Contants;
import in.alifclothing.PersistanceRepository.*;
import in.alifclothing.model.*;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.*;

@Service
public class adminLogicImplementation implements adminLogic{

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private in.alifclothing.FileHandlerService.fileStorageService fileStorageService;
    @Autowired
    private SizeOptionRepository sizeOptionRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private BannerRepository bannerRepository;
    @Autowired
    private OrderStatusRepository orderStatusRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private DeleteProductImages dpi;
    @Autowired
    private DeleteBannerImages dbi;
    @Autowired
    private UserProductInfoRepository userProductInfoRepository;

    public adminLogicImplementation(fileStorageService fileStorageService){
        this.fileStorageService = fileStorageService;
    }

    @Override
    public Response<ProductModel> getAllProducts() {

        Response<ProductModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();
        List<ProductModel> productModelList =  productRepository.findAll();
        if(productModelList.size() == 0){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No product found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }else{
            response.setResponseWrapper(productModelList);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        }

        return response;
    }

    @Override
    public Response<ProductModel> getSingleProduct(Integer pid) {
        Response<ProductModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();
        List<ProductModel> productModelList = new ArrayList<>();
        Optional<ProductModel> productModel = productRepository.findById(pid);
        productModel.ifPresent(product -> {
            productModelList.add(product);
            response.setResponseWrapper(productModelList);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        });

        if(!productModel.isPresent()){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No product found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }
        return response;
    }

    @Override
    public Response<ProductModel> updateProduct(ProductModel productModel,Integer pid) {

        Optional<ProductModel> productModelOptional = productRepository.findById(pid);
        Response<ProductModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        productModelOptional.ifPresent(product -> {
            List<ProductModel> productModelList = new ArrayList<>();
            long time = System.currentTimeMillis();
            product.setUpdateDate(new Date(time));
            product.setProduct_price(productModel.getProduct_price());
            product.setProduct_real_price(productModel.getProduct_real_price());
            product.setAvaialable(productModel.isAvaialable());
            product.setProduct_long_Desc(productModel.getProduct_long_Desc());
            product.setProduct_small_Desc(productModel.getProduct_small_Desc());
            productRepository.save(product);
            productModelList.add(product);
            response.setResponseWrapper(productModelList);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        });
        if(!productModelOptional.isPresent()){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No product found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }
        return response;
    }

    @Override
    public Response<ProductModel> getproductJSON(ProductModel productModel, String categoryID) throws JsonProcessingException {
        Response<ProductModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        long millis = System.currentTimeMillis();
        productModel.setUpdateDate(new Date(millis));
        productModel.setAvaialable(true);

        Optional<CategoryModel> categoryModel = categoriesRepository.findById(Integer.parseInt(categoryID));

        if(categoryModel.isPresent()){
            CategoryModel category = categoryModel.get();
            productModel.setCategoryModel(category);
        }else{
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No category found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }

        productRepository.save(productModel);
        List<ProductModel> productModelList = new ArrayList<>();
        productModelList.add(productModel);
        response.setResponseWrapper(productModelList);
        response.setResponseDesc(Contants.SUCCESS);
        response.setResponseCode(Contants.OK_200);
        return response;
    }

    //delete product by ID
    @Override
    public Response<String> deleteproduct(Integer pid) {
        Response<String> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        Optional<ProductModel> productModelOptional = productRepository.findById(pid);
        productModelOptional.ifPresent(product -> {
            dpi.deleteProductImages(product.getProduct_img1(),product.getProduct_img2(),product.getProduct_img3(),product.getProduct_img4());
            response.setResponseWrapper(Collections.singletonList("Product Deleted Successfully"));
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        });

        if(!productModelOptional.isPresent()){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No product found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }else {
            productRepository.deleteById(pid);
        }

        return response;
    }

    @Override
    public Response<OptionModel> addSize(OptionModel optionModel) {

        Response<OptionModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        List<OptionModel> optionModelList = new ArrayList<>();
        optionModelList.add(optionModel);
        sizeOptionRepository.save(optionModel);
        response.setResponseWrapper(optionModelList);
        response.setResponseDesc(Contants.SUCCESS);
        response.setResponseCode(Contants.OK_200);

        return response;
    }

    @Override
    public Response<OptionModel> getAllSizes() {

        Response<OptionModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();
        List<OptionModel> optionModelList =  sizeOptionRepository.findAll();
        if(optionModelList.size() == 0){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No size option found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }else{
            response.setResponseWrapper(optionModelList);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        }
        return response;
    }

    @Override
    public Response<String> addSizeToProduct(Integer product_id, Integer sizeoption_id) {

        Response<String> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        Optional<ProductModel> productModelOptional = productRepository.findById(product_id);
        Optional<OptionModel> optionModelOptional = sizeOptionRepository.findById(sizeoption_id);

        productModelOptional.ifPresent(product -> {
            optionModelOptional.ifPresent(size -> {
                if(product.getOptionModel().isEmpty()){
                    List<OptionModel> optionModelList = new ArrayList<>();
                    optionModelList.add(size);
                    product.setOptionModel(optionModelList);
                }else{
                    product.getOptionModel().add(size);
                }
                response.setResponseWrapper(Collections.singletonList("Size added successfully"));
                response.setResponseDesc(Contants.SUCCESS);
                response.setResponseCode(Contants.OK_200);
                productRepository.save(product);
            });
        });
        if(!productModelOptional.isPresent()){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No product found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }

        if(!optionModelOptional.isPresent()){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No size option found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }

        return response;
    }

    @Override
    public Response<String> deleteSizeFromProduct(Integer product_id, Integer sizeoption_id) {
        Response<String> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        Optional<ProductModel> productModelOptional = productRepository.findById(product_id);
        Optional<OptionModel> optionModelOptional = sizeOptionRepository.findById(sizeoption_id);

        productModelOptional.ifPresent(product -> {
            optionModelOptional.ifPresent(size -> {
                if(!product.getOptionModel().isEmpty()) {
                    product.getOptionModel().remove(size);
                    productRepository.save(product);
                    response.setResponseWrapper(Collections.singletonList("Size removed successfully"));
                    response.setResponseDesc(Contants.SUCCESS);
                    response.setResponseCode(Contants.OK_200);
                }else{
                    response.setResponseCode(Contants.NOT_FOUND_404);
                    errorMap.put(Contants.ERROR,"Product has no size");
                    response.setErrorMap(errorMap);
                    response.setResponseDesc(Contants.FALIURE);
                }
            });
        });
        if(!productModelOptional.isPresent()){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No product found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }
        if(!optionModelOptional.isPresent()){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No size option found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }
        return response;
    }

    @Override
    public Response<CategoryModel> addCategory(CategoryModel categoryModel) {
        Response<CategoryModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        categoriesRepository.save(categoryModel);
        List<CategoryModel> categoryModelList = new ArrayList<>();
        categoryModelList.add(categoryModel);
        response.setResponseWrapper(categoryModelList);
        response.setResponseDesc(Contants.SUCCESS);
        response.setResponseCode(Contants.OK_200);
        return response;
    }


    @Override
    public Response<String> deleteCategory(Integer cat_id) {

        Response<String> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

            Optional<CategoryModel> categoryModel =  categoriesRepository.findById(cat_id);
            if(categoryModel.isPresent()){
                categoriesRepository.delete(categoryModel.get());
                response.setResponseWrapper(Collections.singletonList("Category Deleted"));
                response.setResponseDesc(Contants.SUCCESS);
                response.setResponseCode(Contants.OK_200);
            }else{
                response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
                errorMap.put(Contants.ERROR,"No category found");
                response.setErrorMap(errorMap);
                response.setResponseDesc(Contants.FALIURE);
            }

        return response;
    }

    @Override
    public Response<UserModel> getUsers() {
        Response<UserModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();
        List<UserModel> userModelList =  userRepository.findAll();
        if(userModelList.size() == 0){
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No user found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }else{
            response.setResponseWrapper(userModelList);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        }
        return response;
    }

    @Override
    public Response<CouponsModel> addCoupon(CouponsModel couponsModel) {
        Response<CouponsModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        List<CouponsModel> couponsModelList = new ArrayList<>();
        couponsModelList.add(couponsModel);
        response.setResponseWrapper(couponsModelList);
        response.setResponseDesc(Contants.SUCCESS);
        response.setResponseCode(Contants.OK_200);

        couponRepository.save(couponsModel);
        return response;
    }

    @Override
    public Response<CouponsModel> getAllCoupons() {
        Response<CouponsModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        List<CouponsModel> couponsModelList = couponRepository.findAll();
        if(couponsModelList.size() != 0){
           response.setResponseWrapper(couponsModelList);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        }else{
            response.setResponseCode(Contants.NOT_FOUND_404);
            response.setResponseDesc(Contants.FALIURE);
            errorMap.put(Contants.ERROR,"No copuon found");
            response.setErrorMap(errorMap);
        }

        return response;
    }

    @Override
    public Response<CouponsModel> getCoupon(Integer cid) {

        Response<CouponsModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();
        Optional<CouponsModel> couponsModelOptional = couponRepository.findById(cid);
        couponsModelOptional.ifPresent(couponsModel -> {
            List<CouponsModel> couponsModelList = new ArrayList<>();
            couponsModelList.add(couponsModel);
            response.setResponseCode(Contants.OK_200);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseWrapper(couponsModelList);
        });

        if(!couponsModelOptional.isPresent()){
            response.setResponseCode(Contants.NOT_FOUND_404);
            response.setResponseDesc(Contants.FALIURE);
            errorMap.put(Contants.ERROR,"No coupon found");
            response.setErrorMap(errorMap);
        }
         return response;
    }

    @Override
    public Response<String> deleteCoupon(Integer cid) {
        Response<String> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        Optional<CouponsModel> couponsModel = couponRepository.findById(cid);
        couponsModel.ifPresent(coupon -> {
            List<String> couponsModelList = new ArrayList<>();
            couponsModelList.add("Deleted Successfully");
            response.setResponseCode(Contants.OK_200);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseWrapper(couponsModelList);
            couponRepository.delete(coupon);
        });
        if(!couponsModel.isPresent()){
            response.setResponseCode(Contants.NOT_FOUND_404);
            response.setResponseDesc(Contants.FALIURE);
            errorMap.put(Contants.ERROR,"No coupon found");
            response.setErrorMap(errorMap);
        }
//        couponRepository.deleteById(cid);

        return response;
    }

    @Override
    public Response<String > addBanner(BannerModel bannerModel) {

        deleteAllBanners();
        Response<String> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        bannerRepository.save(bannerModel);
        response.setResponseWrapper(Arrays.asList("Banners added"));
        response.setResponseDesc(Contants.SUCCESS);
        response.setResponseCode(Contants.OK_200);

        return response;
    }

    @Override
    public Response<BannerModel> getBanners() {
        Response<BannerModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        List<BannerModel> bannerModelList = bannerRepository.findAll();
        if(bannerModelList.size() == 0){
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No banners found in wishlist");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }else {
            response.setResponseWrapper(bannerModelList);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        }
        return response;
    }

    @Override
    public Response<String> deleteAllBanners() {

        Response<String> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        List<BannerModel> bannerModels = bannerRepository.findAll();
        if(bannerModels.size() == 0){
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No banner found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }
        bannerModels.forEach(banner -> {
            List<String> banners = new ArrayList<>();
            banners.add(banner.getBannerimg1());
            banners.add(banner.getBannerimg2());
            banners.add(banner.getBannerimg3());
            banners.add(banner.getBannerimg4());
            banners.add(banner.getBannerimg5());
            banners.add(banner.getBannerimg6());
            banners.add(banner.getBannerimg7());

            if(dbi.deleteAllBanners(banners)) {
                bannerRepository.delete(banner);
                response.setResponseWrapper(Arrays.asList("Banner Deleted"));
                response.setResponseDesc(Contants.SUCCESS);
                response.setResponseCode(Contants.OK_200);
            }else{
                response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
                errorMap.put(Contants.ERROR,"Something went wrong");
                response.setErrorMap(errorMap);
                response.setResponseDesc(Contants.FALIURE);
            }

        });
        return response;
    }

    @Override
    public Response<String> createOrderStatus(OrderStatus orderStatus) {
         orderStatusRepository.save(orderStatus);
        Response<String> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();
        response.setResponseWrapper(Collections.singletonList("Order Status Created"));
        response.setResponseDesc(Contants.SUCCESS);
        response.setResponseCode(Contants.OK_200);
        return response;
    }

    @Override
    public Response<OrderStatus> findAllOrderStatus() {
        Response<OrderStatus> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();
        List<OrderStatus> orderStatusList = orderStatusRepository.findAll();

        if(orderStatusList.size() == 0){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No order found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }else{
            response.setResponseWrapper(orderStatusList);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        }

        return response;
    }

    @Override
    public Response<String> deleteOrderStatusById(Integer order_status_id) {

        Response<String> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        boolean doesExists = orderStatusRepository.existsById(order_status_id);
        if(doesExists){
            orderStatusRepository.deleteById(order_status_id);
            response.setResponseWrapper(Collections.singletonList("Order status deleted"));
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        }else{
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No order status found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }

        return response;
    }

    @Override
    public Response<ShoppingCartModel> getUsersCartByUserId(Integer user_id) {
        Response<ShoppingCartModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        ShoppingCartModel cart =  shoppingCartRepository.findByUserId(user_id).orElse(null);
        if(cart == null){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No cart found of user");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }else{
            List<ShoppingCartModel> shoppingCartModelList = new ArrayList<>();
            shoppingCartModelList.add(cart);
            response.setResponseWrapper(shoppingCartModelList);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        }
        return response;
    }

    @Override
    public Response<OrderModel> getUsersOrdersByUserId(String email) {
        Response<OrderModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();
        if(email == null){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No email found of user");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }
        List<OrderModel> orderModelList = orderRepository.findByEmailId(email);
        if(orderModelList.size() == 0){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No order found of user");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }else{
            response.setResponseWrapper(orderModelList);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        }

        return response;
    }

    @Override
    public Response<OrderModel> addOrder(OrderModel orderModel, String userId) {
        Response<OrderModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        Optional<UserModel> userModelOptional = userRepository.findById(Integer.parseInt(userId));

        if(userModelOptional.isPresent()){
            orderModel.setUserModel(userModelOptional.get());
        }else{
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No user found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }

        List<OrderModel> orderModelList = new ArrayList<>();
        orderModelList.add(orderModel);
        response.setResponseWrapper(orderModelList);
        response.setResponseDesc(Contants.SUCCESS);
        response.setResponseCode(Contants.OK_200);

        orderRepository.save(orderModel);
        return response;
    }

    @Override
    public Response<String> deleteUsersOrderByOrderId(Integer order_id) {
        boolean ifExists = orderRepository.existsById(order_id);

        Response<String> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        if(ifExists){
            orderRepository.deleteById(order_id);
            response.setResponseWrapper(Collections.singletonList("Order deleted successfully"));
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        }else{
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No Orders found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }

        return response;
    }

    @Override
    public Response<String> updateOrdersStatusOfOneOrder(Integer order_id, Integer orderstatus_id) {

        Response<String> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        Optional<OrderStatus> orderStatusOptional = orderStatusRepository.findById(orderstatus_id);
        Optional<OrderModel> orderModelOptional = orderRepository.findById(order_id);

        orderModelOptional.ifPresent(order -> {
            orderStatusOptional.ifPresent(orderStatus -> {
                order.setOrderStatus(orderStatus);
                orderRepository.save(order);
                response.setResponseWrapper(Collections.singletonList("Order updated sucessfully to "+orderStatus.getStatusName()));
                response.setResponseDesc(Contants.SUCCESS);
                response.setResponseCode(Contants.OK_200);
            });
        });

        if(!orderModelOptional.isPresent()) {
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR, "No Orders found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }
        if(!orderStatusOptional.isPresent()){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No Orders Status found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }

        return response;
    }

    @Override
    public Response<OrderModel> getAllOrdersofAllUsers() {
        Response<OrderModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        List<OrderModel> orderModelList = orderRepository.findAll();
        if(orderModelList.size() == 0){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No Orders found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }else{
            response.setResponseWrapper(orderModelList);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        }

        return response;
    }

    @Override
    public Response<OrderModel> getSingleOrderByOrderId(Integer order_id) {

        Response<OrderModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        OrderModel order = orderRepository.findById(order_id).orElse(null);
        if(order == null){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No Order found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }else{
            List<OrderModel> orderModelList = new ArrayList<>();
            orderModelList.add(order);
            response.setResponseWrapper(orderModelList);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        }
        return  response;
    }


}
