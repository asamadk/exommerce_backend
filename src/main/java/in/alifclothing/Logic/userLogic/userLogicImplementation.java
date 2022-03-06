package in.alifclothing.Logic.userLogic;

import in.alifclothing.Dto.Response;
import in.alifclothing.Helper.Contants;
import in.alifclothing.PersistanceRepository.*;
import in.alifclothing.model.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class userLogicImplementation implements userLogic{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;

    @Override
    public Response<ShoppingCartModel> addProductToCart(Integer product_id, String email) {

        Response<ShoppingCartModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        UserModel user = userRepository.findByEmail(email);
        if(user == null){
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No user found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }
        Optional<ProductModel> product = productRepository.findById(product_id);
        ProductModel productModel;
        ShoppingCartModel shoppingCartModel = null;
        long time = System.currentTimeMillis();
        if(product.isPresent()) {
            productModel = product.get();
            Optional<ShoppingCartModel> cart = shoppingCartRepository.findByUserId(user.getUser_id());
            if(cart.isPresent()){
                shoppingCartModel = cart.get();
                shoppingCartModel.setUserModel(user);
                shoppingCartModel.getProductModelList().add(productModel);
                shoppingCartModel.setCouponsModel(null);
                shoppingCartModel.setCouponUsed(false);
                float total = shoppingCartModel.getTotalAmountBeforeDiscount();
                total += productModel.getProduct_real_price();
                shoppingCartModel.setTotalAmountBeforeDiscount(total);
                shoppingCartModel.setTotal(total);
                shoppingCartRepository.save(shoppingCartModel);
                shoppingCartModel.setShoppingCartDate(new java.sql.Date(time));
            }else{
                shoppingCartModel = new ShoppingCartModel();
                shoppingCartModel.setUserModel(user);
                List<ProductModel> productModelList = new ArrayList<>();
                productModelList.add(productModel);
                shoppingCartModel.setProductModelList(productModelList);
                shoppingCartModel.setTotalAmountBeforeDiscount(productModel.getProduct_real_price());
                shoppingCartModel.setTotal(productModel.getProduct_real_price());
                shoppingCartModel.setShoppingCartDate(new java.sql.Date(time));
                shoppingCartRepository.save(shoppingCartModel);

            }
            List<ShoppingCartModel> shoppingCartModels = new ArrayList<>();
            shoppingCartModels.add(shoppingCartModel);
            response.setResponseWrapper(shoppingCartModels);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        }else{
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No product found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }

        return response;
    }

    @Override
    public Response<ShoppingCartModel> getUserCart(String email) {

        Response<ShoppingCartModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        UserModel user = userRepository.findByEmail(email);
        if(user == null){
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No user found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }
        Optional<ShoppingCartModel> shoppingCartModel = shoppingCartRepository.findByUserId(user.getUser_id());
        ShoppingCartModel shoppingCartModel1 ;
        if(shoppingCartModel.isPresent()){
            List<ShoppingCartModel> shoppingCartModels = new ArrayList<>();
            shoppingCartModels.add(shoppingCartModel.get());
            response.setResponseWrapper(shoppingCartModels);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        }else{
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No cart found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }
        return response;
    }

    @Override
    public Response<String> deleteProductFromCart(Integer product_id, Integer cart_id) {

        Response<String> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        Optional<ShoppingCartModel> shoppingcartoptional = shoppingCartRepository.findById(cart_id);
        shoppingcartoptional.ifPresent(shoppingCartModel -> {
            productRepository.findById(product_id).ifPresent(productModel -> {
                shoppingCartModel.getProductModelList().remove(productModel);
                shoppingCartModel.setCouponsModel(null);
                shoppingCartModel.setCouponUsed(false);
                shoppingCartModel.setTotalAmountBeforeDiscount(shoppingCartModel.getTotalAmountBeforeDiscount() - productModel.getProduct_price());
                shoppingCartModel.setTotal(0F);
                if(shoppingCartModel.getProductModelList().isEmpty())shoppingCartModel.setTotalAmountBeforeDiscount(0F);
                shoppingCartRepository.save(shoppingCartModel);
                response.setResponseWrapper(Arrays.asList("Product Deleted"));
                response.setResponseDesc(Contants.SUCCESS);
                response.setResponseCode(Contants.OK_200);
            });
        });
        if(!shoppingcartoptional.isPresent()){
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No product in cart");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }

        return response;
    }

    @Override
    public Response<String> addCouponToCart(String couponName, Integer cartId) {

        Response<String> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        Optional<ShoppingCartModel> shoppingCartModel = shoppingCartRepository.findById(cartId);
        Optional<CouponsModel> couponsModel = Optional.ofNullable(couponRepository.findByCouponName(couponName));
        if(couponsModel.isPresent()) {
            shoppingCartModel.ifPresent(cart -> {
                if (!Objects.equals(couponsModel.get().getCouponName(), "")) {
                    couponsModel.ifPresent(coupon -> {
                        cart.setCouponsModel(coupon);
                        cart.setCouponUsed(true);

                        List<ProductModel> productModelList = cart.getProductModelList();
                        float totalbeforediscount = 0;
                        for (ProductModel productModel : productModelList) {
                            totalbeforediscount += productModel.getProduct_price();
                        }
                        float total = totalbeforediscount;
                        if (cart.isCouponUsed())
                            total = total * (cart.getCouponsModel().getCouponDiscount() / 100F);
                        cart.setTotal(total);
                        cart.setTotalAmountBeforeDiscount(totalbeforediscount);

                        response.setResponseWrapper(Arrays.asList("Coupon added successfully"));
                        response.setResponseDesc(Contants.SUCCESS);
                        response.setResponseCode(Contants.OK_200);
                        shoppingCartRepository.save(cart);
                    });
                }
            });
        }else{
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No coupon found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }

        if(!shoppingCartModel.isPresent()){
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No cart found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }

        return response;
    }

    @Override
    public Response<CouponsModel> getAllCoupons() {
        List<CouponsModel> couponsModelList = couponRepository.findAll();
        Response<CouponsModel> response = new Response<CouponsModel>();
        Map<String,String> errorMap = new HashMap<>();
        if(couponsModelList == null){
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No product found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }
        response.setResponseWrapper(couponsModelList);
        response.setResponseDesc(Contants.SUCCESS);
        response.setResponseCode(Contants.OK_200);
        return response;
    }

    @Override
    public Response<String > deleteCouponFromCart(Integer cartId) {
        //total amount is same as amount before discount

        Response<String> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        Optional<ShoppingCartModel> cartById = shoppingCartRepository.findById(cartId);
        cartById.ifPresent(cart -> {
            cart.setTotal(cart.getTotalAmountBeforeDiscount());
            cart.setCouponsModel(null);
            cart.setCouponUsed(false);
            shoppingCartRepository.save(cart);
            response.setResponseWrapper(Arrays.asList("Deleted Successfully"));
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        });

        if(!cartById.isPresent()){
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No cart found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }
        return response;
    }

    @Override
    public Response<UserModel> getUser(String email) {

        Response<UserModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();
        List<UserModel> userModelList = new ArrayList<>();
        UserModel user =  userRepository.findByEmail(email);
        if(user == null){
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No user found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }else{
            userModelList.add(user);
            response.setResponseWrapper(userModelList);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        }
        return response;
    }

    @Override
    public Response<UserModel> updateCurrentUser(UserModel userModel,Integer uid) {

        Response<UserModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();
        List<UserModel> userModelList = new ArrayList<>();

        Optional<UserModel> checkUser = userRepository.findById(uid);
        checkUser.ifPresent(user -> {
            try {
                if(Objects.equals(user.getEmail(), userModel.getEmail())){
                    userModel.setUser_id(uid);
                    userModel.setRole(user.getRole());
                    userModel.setUser_email_verified(user.isUser_email_verified());
                    userModel.setUser_registration_Date(user.getUser_registration_Date());
                    userModel.setUser_block(user.isUser_block());
                    userModel.setShoppingCartModel(user.getShoppingCartModel());
                    userModel.setUser_Password(user.getUser_Password());
                    userModel.setOrderModels(user.getOrderModels());
                    userModel.setWishlistModel(user.getWishlistModel());
                    userRepository.save(userModel);
                    userModelList.add(userModel);
                    response.setResponseWrapper(userModelList);
                    response.setResponseDesc(Contants.SUCCESS);
                    response.setResponseCode(Contants.OK_200);
                }
                response.setResponseCode(Contants.NOT_FOUND_404);
                errorMap.put(Contants.ERROR,"Email do not match");
                response.setErrorMap(errorMap);
                response.setResponseDesc(Contants.FALIURE);
            }catch (Exception exception){
                exception.printStackTrace();
            }

        });

        if(!checkUser.isPresent()){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No user found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }
        return response;
    }

    @Override
    public Response<String> createOrderFromCart(String email) {

        Response<String> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();
        UserModel user = userRepository.findByEmail(email);

        if(user == null){
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No user found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }
        Optional<ShoppingCartModel> shoppingCartModel = shoppingCartRepository.findByUserId(user.getUser_id());
        long time = System.currentTimeMillis();
        boolean flag =  shoppingCartModel.isPresent();

        shoppingCartModel.ifPresent(cart -> {
            OrderModel order = new OrderModel();
            order.setOrderDate(new Date(time));
            order.setPrice(cart.getTotal());
            order.setUserModel(user);
            if(order.getProductModelList() == null)order.setProductModelList(new ArrayList<>());
            cart.getProductModelList().forEach(product -> {
                order.getProductModelList().add(product);
            });
            order.setRazorpay_order_id(cart.getRazorpay_order_id());
            orderRepository.save(order);
            shoppingCartRepository.deleteById(cart.getShoppingCartId());
            response.setResponseWrapper(Arrays.asList("Order Created"));
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
            //TODO tracking number and order status
        });

        if(!shoppingCartModel.isPresent()){
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"Shopping cart is empty");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }
        return response;
    }

    @Override
    public Response<OrderModel> getAllOrdersOfUser(String email) {

        Response<OrderModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        UserModel user = userRepository.findByEmail(email);
        if(user == null){
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No user found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }
        List<OrderModel> orderModelList =  orderRepository.findByUserId(user.getUser_id());
        if(orderModelList != null){
            response.setResponseWrapper(orderModelList);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        }
        return response;
    }

    @Override
    public Response<OrderModel> getSingleOrderOfUser(Integer order_id) {

        Response<OrderModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();
        List<OrderModel> orderModelList = new ArrayList<>();

        OrderModel order =  orderRepository.findById(order_id).orElse(null);
        if(order != null){
            orderModelList.add(order);
            response.setResponseWrapper(orderModelList);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        }else{
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No order found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }
        return response;
    }

    @Override
    public Response<ProductModel> getAllProducts(String orderBy,String limit) {

        List<ProductModel> productModelList = null;
//        if(limit != Contants.NO_LIMIT){
//            productModelList = productRepository.findByLimit(Integer.valueOf(limit));
//        }
        if(orderBy == null) {
             productModelList = productRepository.findAll();
        }else{
            productModelList = productRepository.findInOrder();
        }
        Response<ProductModel> response = new Response<ProductModel>();
        Map<String,String> errorMap = new HashMap<>();

        if(productModelList == null){
            errorMap.put(Contants.ERROR,"No products found");
            response.setErrorMap(errorMap);
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }

        response.setResponseWrapper(productModelList);
        response.setResponseDesc(Contants.SUCCESS);
        response.setResponseCode(Contants.OK_200);
        return response;
    }

    @Override
    public Response<ProductModel> getSingleProduct(Integer product_id) {
        Response<ProductModel> response = new Response<ProductModel>();
        Map<String,String> errorMap = new HashMap<>();
        List<ProductModel> productModelList = new ArrayList<>();
        ProductModel product = productRepository.findById(product_id).orElse(null);
        if(product == null){
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No product found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }
        productModelList.add(product);
        response.setResponseWrapper(productModelList);
        response.setResponseDesc(Contants.SUCCESS);
        response.setResponseCode(Contants.OK_200);
        return response;
    }

    @Override
    public Response<ProductModel> getAllProductsByCategory(Integer category_id) {
        Response<ProductModel> response = new Response<ProductModel>();
        Map<String,String> errorMap = new HashMap<>();
        List<ProductModel> productModelList = productRepository.findByCategoryId(category_id);
        if(productModelList == null){
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No product found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }
        response.setResponseWrapper(productModelList);
        response.setResponseDesc(Contants.SUCCESS);
        response.setResponseCode(Contants.OK_200);
        return response;
    }

    @Override
    public Response<String> addProductToWishlist(Integer product_id, String email) {

        Response<String> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        UserModel user = userRepository.findByEmail(email);
        if(user == null){
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No user found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }
        Optional<ProductModel> productModelOptional = productRepository.findById(product_id);
        productModelOptional.ifPresent(product -> {
            Optional<WishlistModel> wishlistModelOptional = wishlistRepository.findByUserId(user.getUser_id());
            wishlistModelOptional.ifPresent(wishlist -> {
                wishlist.setUserModel(user);
                wishlist.getProductModelList().add(product);
                wishlistRepository.save(wishlist);
                response.setResponseWrapper(Arrays.asList("Product Added"));
            });
            if(!wishlistModelOptional.isPresent()){
                WishlistModel wishlist = new WishlistModel();
                wishlist.setUserModel(user);
                List<ProductModel> productModelList = new ArrayList<>();
                productModelList.add(product);
                wishlist.setProductModelList(productModelList);
                wishlistRepository.save(wishlist);
                response.setResponseWrapper(Arrays.asList("Wishlist Created"));
            }
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        });
        if(!productModelOptional.isPresent()){
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No product found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }
        return response;
    }

    @Override
    public Response<WishlistModel> getUserWishlist(String email) {

        Response<WishlistModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();
        UserModel user = userRepository.findByEmail(email);
        if(user == null){
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No user found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }

        List<WishlistModel> wishlistModelList = new ArrayList<>();
        Optional<WishlistModel> wishList = wishlistRepository.findByUserId(user.getUser_id());
        if(wishList.isPresent()){
            wishlistModelList.add(wishList.get());
            response.setResponseWrapper(wishlistModelList);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        }else{
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No wishlist found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }
        return response;
    }

    @Override
    public Response<String> deleteProductFromWishlist(Integer product_id, Integer wishlist_id) {

        Response<String> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        Optional<WishlistModel> wishlistModelOptional = wishlistRepository.findById(wishlist_id);
        wishlistModelOptional.ifPresent(wishlist -> {
            productRepository.findById(product_id).ifPresent(product -> {
                wishlist.getProductModelList().remove(product);
                wishlistRepository.save(wishlist);
                response.setResponseWrapper(Arrays.asList("Product Deleted"));
                response.setResponseDesc(Contants.SUCCESS);
                response.setResponseCode(Contants.OK_200);
            });
        });

        if(!wishlistModelOptional.isPresent()){
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No product found in wishlist");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }
        return response;
    }

    @Override
    public Response<CategoryModel> getAllCategories() {
        Response<CategoryModel> response = new Response<CategoryModel>();
        Map<String,String> errorMap = new HashMap<>();
        List<CategoryModel> categoryModelList = categoriesRepository.findAll();
        if(categoryModelList == null){
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            errorMap.put(Contants.ERROR,"No Category found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }
        response.setResponseWrapper(categoryModelList);
        response.setResponseDesc(Contants.SUCCESS);
        response.setResponseCode(Contants.OK_200);
        return response;
    }


}
