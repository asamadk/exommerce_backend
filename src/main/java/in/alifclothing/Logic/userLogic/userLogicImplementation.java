package in.alifclothing.Logic.userLogic;

import in.alifclothing.PersistanceRepository.*;
import in.alifclothing.model.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ShoppingCartModel addProductToCart(Integer product_id, String email) {

        UserModel user = userRepository.findByEmail(email);
        Optional<ProductModel> product = productRepository.findById(product_id);
        ProductModel productModel;
        ShoppingCartModel shoppingCartModel;
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
                total += productModel.getProduct_price();
                shoppingCartModel.setTotalAmountBeforeDiscount(total);
                shoppingCartModel.setTotal(total);
                shoppingCartRepository.save(shoppingCartModel);
                shoppingCartModel.setShoppingCartDate(new java.sql.Date(time));
                return shoppingCartModel;
            }else{
                shoppingCartModel = new ShoppingCartModel();
                shoppingCartModel.setUserModel(user);
                List<ProductModel> productModelList = new ArrayList<>();
                productModelList.add(productModel);
                shoppingCartModel.setProductModelList(productModelList);
                shoppingCartModel.setTotalAmountBeforeDiscount(productModel.getProduct_price());
                shoppingCartModel.setTotal(productModel.getProduct_price());
                shoppingCartModel.setShoppingCartDate(new java.sql.Date(time));
                shoppingCartRepository.save(shoppingCartModel);
                return shoppingCartModel;
            }
        }

        return new ShoppingCartModel();
    }

    @Override
    public ShoppingCartModel getUserCart(String email) {
        UserModel user = userRepository.findByEmail(email);
        Optional<ShoppingCartModel> shoppingCartModel = shoppingCartRepository.findByUserId(user.getUser_id());
        ShoppingCartModel shoppingCartModel1 ;
        return shoppingCartModel.orElse(null);
    }

    @Override
    public AtomicBoolean deleteProductFromCart(Integer product_id, Integer cart_id) {
        AtomicBoolean flag = new AtomicBoolean(false);
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
                flag.set(true);
            });
        });
        return flag;
    }

    @Override
    public boolean addCouponToCart(CouponsModel couponsModel, Integer cartId) {
        Optional<ShoppingCartModel> shoppingCartModel = shoppingCartRepository.findById(cartId);
        Optional<CouponsModel> couponName = Optional.ofNullable(couponRepository.findByCouponName(couponsModel.getCouponName()));
        shoppingCartModel.ifPresent(cart -> {
            if(!Objects.equals(couponsModel.getCouponName(), "")){
                couponName.ifPresent(coupon -> {
                    cart.setCouponsModel(coupon);
                    cart.setCouponUsed(true);

                    List<ProductModel> productModelList = cart.getProductModelList();
                    float totalbeforediscount = 0;
                    for(ProductModel productModel: productModelList){
                        totalbeforediscount += productModel.getProduct_price();
                    }
                    float total = totalbeforediscount;
                    if(cart.isCouponUsed())
                        total = total * (cart.getCouponsModel().getCouponDiscount()/100F);
                    cart.setTotal(total);
                    cart.setTotalAmountBeforeDiscount(totalbeforediscount);

                    shoppingCartRepository.save(cart);
                });
            }
        });
        return shoppingCartModel.isPresent()&&couponName.isPresent();
    }

    @Override
    public List<CouponsModel> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public boolean deleteCouponFromCart(Integer cartId) {
        //total amount is same as amount befoee discount
        Optional<ShoppingCartModel> cartById = shoppingCartRepository.findById(cartId);
        cartById.ifPresent(cart -> {
            cart.setTotal(cart.getTotalAmountBeforeDiscount());
            cart.setCouponsModel(null);
            cart.setCouponUsed(false);
            shoppingCartRepository.save(cart);
        });
        return cartById.isPresent();
    }

    @Override
    public UserModel getUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean updateCurrentUser(UserModel userModel,Integer uid) {
        Optional<UserModel> checkUser = userRepository.findById(uid);
        checkUser.ifPresent(user -> {
            try {
                if(user.getEmail().equals(userModel.getEmail())){
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
                }
            }catch (Exception exception){
                exception.printStackTrace();
            }

        });
        return userModel.getUser_id() == uid;
    }

    @Override
    public boolean createOrderFromCart(String email) {
        UserModel user = userRepository.findByEmail(email);
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
            //TODO tracking number and order status
        });
        return flag;
    }

    @Override
    public List<OrderModel> getAllOrdersOfUser(String email) {
        UserModel user = userRepository.findByEmail(email);
        return orderRepository.findByUserId(user.getUser_id());
    }

    @Override
    public OrderModel getSingleOrderOfUser(Integer order_id) {
        return orderRepository.findById(order_id).orElse(null);
    }

    @Override
    public List<ProductModel> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public ProductModel getSingleProduct(Integer product_id) {
        return productRepository.findById(product_id).orElse(null);
    }

    @Override
    public List<ProductModel> getAllProductsByCategory(Integer category_id) {
        return productRepository.findByCategoryId(category_id);
    }

    @Override
    public boolean addProductToWishlist(Integer product_id, String email) {

        UserModel user = userRepository.findByEmail(email);
        Optional<ProductModel> productModelOptional = productRepository.findById(product_id);
        productModelOptional.ifPresent(product -> {
            Optional<WishlistModel> wishlistModelOptional = wishlistRepository.findByUserId(user.getUser_id());
            wishlistModelOptional.ifPresent(wishlist -> {
                wishlist.setUserModel(user);
                wishlist.getProductModelList().add(product);
                wishlistRepository.save(wishlist);
            });
            if(wishlistModelOptional.isEmpty()){
                WishlistModel wishlist = new WishlistModel();
                wishlist.setUserModel(user);
                List<ProductModel> productModelList = new ArrayList<>();
                productModelList.add(product);
                wishlist.setProductModelList(productModelList);
                wishlistRepository.save(wishlist);
            }
        });

        return productModelOptional.isPresent();
    }

    @Override
    public WishlistModel getUserWishlist(String email) {
        UserModel user = userRepository.findByEmail(email);
        return wishlistRepository.findByUserId(user.getUser_id()).orElse(null);
    }

    @Override
    public boolean deleteProductFromWishlist(Integer product_id, Integer wishlist_id) {
        Optional<WishlistModel> wishlistModelOptional = wishlistRepository.findById(wishlist_id);
        wishlistModelOptional.ifPresent(wishlist -> {
            productRepository.findById(product_id).ifPresent(product -> {
                wishlist.getProductModelList().remove(product);
                wishlistRepository.save(wishlist);
            });
        });
        return wishlistModelOptional.isPresent();
    }

    @Override
    public List<CategoryModel> getAllCategories() {
        return categoriesRepository.findAll();
    }


}
