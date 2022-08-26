package in.alifclothing.Logic.userLogic;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.alifclothing.Dto.ChangePasswordRequest;
import in.alifclothing.Dto.Response;
import in.alifclothing.Helper.Contants;
import in.alifclothing.PersistanceRepository.*;
import in.alifclothing.model.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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
    @Autowired
    private OrderStatusRepository orderStatusRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private Environment evn;
    @Autowired
    private UserProductInfoRepository userProductInfoRepository;

    @Override
    public Response<ShoppingCartModel> addProductToCart(Integer product_id, String email, String sizeObject) {

        Response<ShoppingCartModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        UserModel user = userRepository.findByEmail(email);
        if(user == null){
            response.setResponseCode(Contants.NOT_FOUND_404);
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
                deleteCouponFromCart(cart.get().getShoppingCartId());
                shoppingCartModel = cart.get();
                shoppingCartModel.setUserModel(user);
                shoppingCartModel.getProductModelList().add(productModel);
                shoppingCartModel.setCouponsModel(null);
                shoppingCartModel.setCouponUsed(false);
                float totalBeforeDescount = shoppingCartModel.getTotalAmountBeforeDiscount();
                totalBeforeDescount = totalBeforeDescount + productModel.getProduct_price();
                float total = shoppingCartModel.getTotal();
                total = total + productModel.getProduct_real_price();
                shoppingCartModel.setTotalAmountBeforeDiscount(totalBeforeDescount);
                shoppingCartModel.setTotal(total);
                shoppingCartRepository.save(shoppingCartModel);
                shoppingCartModel.setShoppingCartDate(new java.sql.Date(time));
            }else{
                shoppingCartModel = new ShoppingCartModel();
                shoppingCartModel.setUserModel(user);
                List<ProductModel> productModelList = new ArrayList<>();
                productModelList.add(productModel);
                shoppingCartModel.setProductModelList(productModelList);
                shoppingCartModel.setTotalAmountBeforeDiscount(productModel.getProduct_price());
                shoppingCartModel.setTotal(productModel.getProduct_real_price());
                shoppingCartModel.setShoppingCartDate(new java.sql.Date(time));
                shoppingCartRepository.save(shoppingCartModel);
            }
            UserProductInformation userProductInformation = new UserProductInformation();
            userProductInformation.setUserModel(user);
            userProductInformation.setProductModel(productModel);
            userProductInformation.setSizeJSON(sizeObject);
            JSONObject jsonObject = new JSONObject(sizeObject);
            Boolean isCustom = (Boolean) jsonObject.get("custom");
            userProductInformation.setCustom(isCustom);
            userProductInfoRepository.save(userProductInformation);
            System.out.println("userProductInfoRepository : "+userProductInformation.toString());
            List<ShoppingCartModel> shoppingCartModels = new ArrayList<>();
            shoppingCartModels.add(shoppingCartModel);
            response.setResponseWrapper(shoppingCartModels);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        }else{
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No product found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }

        return response;
    }

    @Override
    public Response<Object> getUserCart(String email) {

        Response<Object> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        UserModel user = userRepository.findByEmail(email);
        if(user == null){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No user found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }
        Optional<ShoppingCartModel> shoppingCartModel = shoppingCartRepository.findByUserId(user.getUser_id());
        ShoppingCartModel shoppingCartModel1 ;
        List<Object> responseObject = new ArrayList<>();
        List<Integer> productIDs = new ArrayList<>();
        if(shoppingCartModel.isPresent()){
            responseObject.add(shoppingCartModel.get());
            response.setResponseWrapper(responseObject);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
            shoppingCartModel.get().getProductModelList().stream().forEach(product -> {
                productIDs.add(product.getProduct_id());
            });
        }else{
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No cart found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }

        List<UserProductInformation> productInformationList =
                userProductInfoRepository.
                        getProductInformationByUserNameAndProductIds(productIDs,user.getUser_id());
        responseObject.addAll(productInformationList);
        response.setResponseWrapper(responseObject);
        return response;
    }

    @Override
    public Response<String> deleteProductFromCart(Integer product_id, Integer cart_id, Integer userProductInfoId) {

        Response<String> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        userProductInfoRepository.deleteById(userProductInfoId);
        Optional<ShoppingCartModel> shoppingcartoptional = shoppingCartRepository.findById(cart_id);
        shoppingcartoptional.ifPresent(shoppingCartModel -> {
            deleteCouponFromCart(shoppingCartModel.getShoppingCartId());
            productRepository.findById(product_id).ifPresent(productModel -> {
                shoppingCartModel.getProductModelList().remove(productModel);
                shoppingCartModel.setCouponsModel(null);
                shoppingCartModel.setCouponUsed(false);
                shoppingCartModel.setTotalAmountBeforeDiscount(shoppingCartModel.getTotalAmountBeforeDiscount() - productModel.getProduct_price());
                shoppingCartModel.setTotal(shoppingCartModel.getTotal() - productModel.getProduct_real_price());
                if(shoppingCartModel.getProductModelList().isEmpty()){
                    shoppingCartModel.setTotalAmountBeforeDiscount(0F);
                    shoppingCartModel.setTotal(0F);
                }
                shoppingCartRepository.save(shoppingCartModel);
                response.setResponseWrapper(Arrays.asList("Product Deleted"));
                response.setResponseDesc(Contants.SUCCESS);
                response.setResponseCode(Contants.OK_200);
            });
        });
        if(!shoppingcartoptional.isPresent()){
            response.setResponseCode(Contants.NOT_FOUND_404);
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
//        orderRepository.f
        Integer userID = shoppingCartModel.get().getUserModel().getUser_id();
        List<OrderModel> optionalOrderModel = orderRepository.findByUserId(userID);
        AtomicReference<Boolean> toReturn = new AtomicReference<>(false);
        if(optionalOrderModel.size() > 0){
            optionalOrderModel.stream().forEach(orderModel -> {
                if( orderModel.getCouponName() != null && orderModel.getCouponName().equalsIgnoreCase(couponName)){
                    toReturn.set(true);
                    response.setResponseWrapper(Arrays.asList("Coupon already used"));
                    response.setResponseDesc("You have already used this coupon");
                    response.setResponseCode(Contants.OK_200);
                }
            });
        }

        if(toReturn.get()){
            return response;
        }

        if(couponsModel.isPresent()) {
            shoppingCartModel.ifPresent(cart -> {
                if (!Objects.equals(couponsModel.get().getCouponName(), "")) {
                    couponsModel.ifPresent(coupon -> {
                        if(coupon.getMinimumPurchasePrice() > cart.getTotalAmountBeforeDiscount()){
                            errorMap.put(Contants.ERROR,"Minimum total needs to be "+coupon.getMinimumPurchasePrice()+" to apply this coupon");
                            response.setErrorMap(errorMap);
                            response.setResponseDesc(Contants.FALIURE);
                            response.setResponseCode(Contants.CLIENT_400);
                            return;
                        }
                        cart.setCouponsModel(coupon);
                        cart.setCouponUsed(true);

                        List<ProductModel> productModelList = cart.getProductModelList();
                        float totalbeforediscount = cart.getTotalAmountBeforeDiscount();
                        float total = cart.getTotalAmountBeforeDiscount();
                        if (cart.isCouponUsed()) {
                            float removeAmount = total * (cart.getCouponsModel().getCouponDiscount() / 100F);
                            if(removeAmount > coupon.getMaximumDiscount()){
                                removeAmount = (float) coupon.getMaximumDiscount();
                            }
                            total = total - removeAmount;
                        }
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
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No coupon found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }

        if(!shoppingCartModel.isPresent()){
            response.setResponseCode(Contants.NOT_FOUND_404);
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
            response.setResponseCode(Contants.NOT_FOUND_404);
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
            cart.setTotal(0);
            cart.getProductModelList().forEach(product -> {
                float total = cart.getTotal();
                total = total + product.getProduct_real_price();
                cart.setTotal(total);

            });
//            cart.setTotal(cart.getTotalAmountBeforeDiscount());
            cart.setCouponsModel(null);
            cart.setCouponUsed(false);
            shoppingCartRepository.save(cart);
            response.setResponseWrapper(Arrays.asList("Deleted Successfully"));
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        });

        if(!cartById.isPresent()){
            response.setResponseCode(Contants.NOT_FOUND_404);
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
            response.setResponseCode(Contants.NOT_FOUND_404);
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
//                if(Objects.equals(user.getEmail(), userModel.getEmail())){
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
//                }
//                response.setResponseCode(Contants.NOT_FOUND_404);
//                errorMap.put(Contants.ERROR,"Email do not match");
//                response.setErrorMap(errorMap);
//                response.setResponseDesc(Contants.FALIURE);
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

    private void addOrderIdToProductSizeInfo(UserModel user, OrderModel orderModel){
        List<UserProductInformation> userProductInformationList = userProductInfoRepository.getProductInformationByUserId(user.getUser_id());
        userProductInformationList.stream().forEach(userProdInfo -> {
            userProdInfo.setOrderModel(orderModel);
        });
        userProductInfoRepository.saveAll(userProductInformationList);
    }

    @Override
    public Response<OrderModel> createOrderFromCart(String email, String couponName) {

        Response<OrderModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();
        UserModel user = userRepository.findByEmail(email);

        if(user == null){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No user found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }

        Optional<ShoppingCartModel> shoppingCartModel = shoppingCartRepository.findByUserId(user.getUser_id());
        long time = System.currentTimeMillis();
        boolean flag =  shoppingCartModel.isPresent();

        shoppingCartModel.ifPresent(cart -> {
            OrderModel order = new OrderModel();
            order.setCouponName(couponName);
            order.setOrderDate(new Date(time));
            order.setPrice(cart.getTotal());
            order.setUserModel(user);
            if(order.getProductModelList() == null)order.setProductModelList(new ArrayList<>());
            cart.getProductModelList().forEach(product -> {
                order.getProductModelList().add(product);
            });
            //TODO : add coupon name in order field
            order.setRazorpay_order_id(cart.getRazorpay_order_id());
            orderRepository.save(order);
            addOrderIdToProductSizeInfo(user,order);
            shoppingCartRepository.deleteById(cart.getShoppingCartId());
            List<OrderModel> orderModelList = new ArrayList<>();
            orderModelList.add(order);
            response.setResponseWrapper(orderModelList);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
            //TODO tracking number and order status
        });

        if(!shoppingCartModel.isPresent()){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"Shopping cart is empty");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }
        return response;
    }

    @Override
    public Response<OrderModel> getAllOrdersOfUser(String email,String page,String size) {

        Response<OrderModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        UserModel user = userRepository.findByEmail(email);
        if(user == null){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No user found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }

        List<OrderModel> orderModelList =  new ArrayList<>();
        Pageable paging = null;
        if(page != null && size != null){
             paging = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size));
        }else{
            paging = PageRequest.of(0, 10);
        }
        Page<OrderModel> orderModelPage = orderRepository.findByUserIdPagination(user.getUser_id(),paging);
        if(!orderModelPage.isEmpty()){
            orderModelList = orderModelPage.toList();
        }
        response.setResponseWrapper(orderModelList);
        response.setResponseDesc(Contants.SUCCESS);
        response.setResponseCode(Contants.OK_200);
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
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No order found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
        }
        return response;
    }

    @Override
    public Response<ProductModel> getAllProducts(String orderBy,String page,String limit) {
        List<ProductModel> productModelList = null;
        if(Integer.parseInt(limit) == 0) {
            if (orderBy == null) {
                productModelList = productRepository.findAll();
            } else {
                if (orderBy.equals(Contants.DESCENDING)) {
                    productModelList = productRepository.findInOrderDESC();
                } else if (orderBy.equals(Contants.ASCENDING)) {
                    //make for ascending in future
                } else {
                    productModelList = productRepository.findAll();
                }
            }
        }else{
            if(orderBy == null) {
                Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(limit));
                Slice<ProductModel> productModelSlice = productRepository.findProductsByLimit(pageable);
                productModelList = productModelSlice.getContent();
            }else if(orderBy.equals(Contants.DESCENDING)){
                Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(limit),Sort.by("updateDate").descending());
                Slice<ProductModel> productModelSlice = productRepository.findProductsByLimit(pageable);
                productModelList = productModelSlice.getContent();
            }else if(orderBy.equals(Contants.ASCENDING)){
                Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(limit),Sort.by("updateDate").ascending());
                Slice<ProductModel> productModelSlice = productRepository.findProductsByLimit(pageable);
                productModelList = productModelSlice.getContent();
            }else{
                Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(limit));
                Slice<ProductModel> productModelSlice = productRepository.findProductsByLimit(pageable);
                productModelList = productModelSlice.getContent();
            }
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
            response.setResponseCode(Contants.NOT_FOUND_404);
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
    public void deleteAllOrdersNonExposedMethod(String email) {
        UserModel user = userRepository.findByEmail(email);
        orderRepository.deleteOrders(user);
    }

    @Override
    public Response<ProductModel> getAllProductsByCategory(Integer category_id,String page,String size) {
        Response<ProductModel> response = new Response<ProductModel>();
        Map<String,String> errorMap = new HashMap<>();
        Pageable pageable = null;
        if(page != null && size != null){
            pageable = PageRequest.of(Integer.parseInt(page),Integer.parseInt(size));
        }else{
            pageable = PageRequest.of(0,10);
        }
        List<ProductModel> productModelList = productRepository.findByCategoryIdPagable(category_id,pageable);
        if(productModelList == null){
            response.setResponseCode(Contants.NOT_FOUND_404);
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
            response.setResponseCode(Contants.NOT_FOUND_404);
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
                List<ProductModel> productModelList = wishlist.getProductModelList();
                if(productModelList.contains(product)){
                    response.setResponseCode(Contants.OK_200);
                    response.setErrorMap(null);
                    response.setResponseDesc(Contants.FALIURE);
                    response.setResponseWrapper(Collections.singletonList("Product already exists in your wishlist"));
                    return;
                }
                wishlist.getProductModelList().add(product);
                wishlistRepository.save(wishlist);
                response.setResponseWrapper(Collections.singletonList("Product Added"));
            });
            if(!wishlistModelOptional.isPresent()){
                WishlistModel wishlist = new WishlistModel();
                wishlist.setUserModel(user);
                List<ProductModel> productModelList = new ArrayList<>();
                productModelList.add(product);
                wishlist.setProductModelList(productModelList);
                wishlistRepository.save(wishlist);
                response.setResponseWrapper(Collections.singletonList("Wishlist Created"));
            }
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
    public Response<WishlistModel> getUserWishlist(String email,String page,String size) {

        Response<WishlistModel> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        Pageable pageable = null;
        if(page != null && size != null){
            pageable = PageRequest.of(Integer.parseInt(page),Integer.parseInt(size));
        }else{
            pageable = PageRequest.of(0,10);
        }
        UserModel user = userRepository.findByEmail(email);
        if(user == null){
            response.setResponseCode(Contants.NOT_FOUND_404);
            errorMap.put(Contants.ERROR,"No user found");
            response.setErrorMap(errorMap);
            response.setResponseDesc(Contants.FALIURE);
            return response;
        }

        List<WishlistModel> wishList = new ArrayList<>();
         Page<WishlistModel> wishlistModelPage = wishlistRepository.findByUserId(user.getUser_id(),pageable);
         if(!wishlistModelPage.isEmpty()){
             wishList = wishlistModelPage.toList();
         }
        if(wishList.size() > 0){
            response.setResponseWrapper(wishList);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseCode(Contants.OK_200);
        }else{
            response.setResponseCode(Contants.NOT_FOUND_404);
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
            response.setResponseCode(Contants.NOT_FOUND_404);
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
            response.setResponseCode(Contants.NOT_FOUND_404);
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

    @Override
    public Response<ProductModel> searchProducts(String productName) {
        Response<ProductModel> response = new Response<ProductModel>();
        Map<String,String> errorMap = new HashMap<>();

        System.out.println("PRODUCT NAME : "+ productName);
        List<ProductModel> productModelList = productRepository.findProductsBySearch("%"+productName+"%");
        if(productModelList.size() > 0){
            response.setResponseWrapper(productModelList);
            response.setResponseCode(Contants.OK_200);
            response.setResponseDesc(Contants.SUCCESS);
        }else{
            errorMap.put(Contants.ERROR,Contants.NOT_FOUND_404);
            response.setErrorMap(errorMap);
            response.setResponseWrapper(null);
            response.setResponseCode(Contants.NOT_FOUND_404);
            response.setResponseDesc(Contants.FALIURE);
        }
        return response;
    }

    @Override
    public Response<String> returnOrder(Integer orderId) {
        Response<String> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        Optional<OrderModel> orderModelOptional = orderRepository.findById(orderId);
        Optional<OrderStatus> orderStatusOptional = orderStatusRepository.findByStatusName(Contants.ORDER_STATUS_RETURN);
        orderModelOptional.ifPresent(order -> {
           orderStatusOptional.ifPresent(status -> {
               order.setOrderStatus(status);
               response.setResponseWrapper(Arrays.asList(Contants.ORDER_RETURN));
               response.setResponseCode(Contants.OK_200);
               response.setResponseDesc(Contants.SUCCESS);
           });
        });

        if(!orderModelOptional.isPresent() || !orderStatusOptional.isPresent()){
            errorMap.put(Contants.ERROR,"Not found");
            response.setErrorMap(errorMap);
            response.setResponseCode(Contants.NOT_FOUND_404);
            response.setResponseDesc("Not found");
        }
        return response;
    }

    @Override
    public Response<String> changePassword(ChangePasswordRequest changePasswordRequest,String userEmail) {
        Response<String> response = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

        UserModel user =  userRepository.findByEmail(userEmail);
        if(user != null) {
            if (changePasswordRequest != null) {
                if (changePasswordRequest.getCurrentPassword() != null && changePasswordRequest.getNewPassword() != null) {
                    if(bCryptPasswordEncoder.matches(changePasswordRequest.getCurrentPassword(),user.getUser_Password())){
                        String encodedPassword = bCryptPasswordEncoder.encode(changePasswordRequest.getNewPassword());
                        user.setUser_Password(encodedPassword);
                        userRepository.save(user);
                        response.setResponseDesc(Contants.SUCCESS);
                        response.setResponseCode(Contants.OK_200);
                        response.setResponseWrapper(Arrays.asList("Password changed succesfully, Please login again"));
                    }else{
                        errorMap.put(Contants.ERROR, "Old password does not match");
                        response.setErrorMap(errorMap);
                        response.setResponseCode(Contants.NOT_FOUND_404);
                        response.setResponseDesc(Contants.FALIURE);
                    }
                }
            } else {
                errorMap.put(Contants.ERROR, "Empty DTO");
                response.setErrorMap(errorMap);
                response.setResponseCode(Contants.NOT_FOUND_404);
                response.setResponseDesc(Contants.FALIURE);
            }
        }else{
            errorMap.put(Contants.ERROR, "User not found");
            response.setErrorMap(errorMap);
            response.setResponseCode(Contants.NOT_FOUND_404);
            response.setResponseDesc(Contants.FALIURE);
        }

        return response;
    }

    @Override
    public Response<String> confirmOrder(OrderModel orderModel) {
        Map<String,String> errorMap = new HashMap<>();
        Response<String> response = new Response<>();
        try{
            if(!orderModel.getPaymentMode().isEmpty()){
                orderModel.setOrderStatusString(Contants.ORDER_STATUS_NEW);
                Date today = new Date();
                Date after7Days = new Date(today.getTime()+ ((1000 * 60 * 60 * 24)*7));
                orderModel.setExpectedArrivalDate(after7Days);
                orderRepository.save(orderModel);
            }
            sendOrderSuccessMailToUser(orderModel.getUserModel().getEmail(),orderModel);
//            sendOrderSuccessMailToUser("vekoco5655@mahazai.com",orderModel);
            sendOrderSuccessfullMailToAlif(orderModel);
            response.setResponseCode(Contants.OK_200);
            response.setResponseDesc(Contants.SUCCESS);
            response.setResponseWrapper(Collections.singletonList("Order placed successfully"));
        }catch (Exception exception){
            response.setResponseWrapper(null);
            response.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            response.setResponseDesc(Contants.FALIURE);
            errorMap.put(Contants.ERROR,exception.getMessage());
            response.setErrorMap(errorMap);
            exception.printStackTrace();
        }
        return response;
    }

    private void sendOrderSuccessfullMailToAlif(OrderModel orderModel) throws MessagingException, IOException {
        String email = evn.getProperty("alif.mail.for.order");
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(Objects.requireNonNull(evn.getProperty("spring.mail.username")),"Alif Support");
        helper.setTo(email);
        String alifURL = "https://www.alifclothing.in/admin/order/details/"+orderModel.getOrderId();
        String subject = "AN ORDER HAS BEEN PLACED";
        ObjectMapper objectMapper = new ObjectMapper();
//        String contentJSON = objectMapper.writeValueAsString(orderModel);
        String contentJSON = "<p>An order is placed by "+orderModel.getUserModel().getUser_Fname() + orderModel.getUserModel().getUser_Lname()
                            +"<p><b><a href=\""+ alifURL +"\">For more information click here</a></b><p>";
        helper.setSubject(subject);
        helper.setText(contentJSON,true);
        javaMailSender.send(message);
    }

    private void sendOrderSuccessMailToUser(String email,OrderModel orderModel) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(Objects.requireNonNull(evn.getProperty("spring.mail.username")),"Alif Support");
        helper.setTo(email);
        String alifURL = "https://www.alifclothing.in/order/detail/"+orderModel.getOrderId();
        String subject = "ORDER PLACED SUCCESSFULLY";
        String content = "<p>Hello "+orderModel.getUserModel().getUser_Fname()+"</p>"
                +"<p>Your order has been placed</p>"
                +"<p><b>Order ID </b> # "+orderModel.getOrderId()+"</p>"
                +"<p><b>Estimated delivery date</b> : "+orderModel.getExpectedArrivalDate()+"<p>"
                +"<p>If it was not you contact Alif support immediately</p>"
                +"<p><b><a href=\""+ alifURL +"\">For more information of your order click here</a></b><p>";
        helper.setSubject(subject);
        helper.setText(content,true);
        javaMailSender.send(message);
    }

}
