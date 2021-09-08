package in.alifclothing.Logic.adminLogic;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.alifclothing.FileHandlerService.fileStorageService;
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

    public adminLogicImplementation(fileStorageService fileStorageService){
        this.fileStorageService = fileStorageService;
    }

    @Override
    public List<ProductModel> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<ProductModel> getSingleProduct(Integer pid) {
        return productRepository.findById(pid);
    }

    @Override
    public Optional<ProductModel> updateProduct(ProductModel productModel,Integer pid) {
        Optional<ProductModel> product = productRepository.findById(pid);
        if(product.isPresent()){
            ProductModel productModel1 = product.get();
            long time = System.currentTimeMillis();
            productModel1.setUpdateDate(new Date(time));
            productModel1.setProduct_price(productModel.getProduct_price());
            productModel1.setProduct_real_price(productModel.getProduct_real_price());
            productModel1.setAvaialable(productModel.isAvaialable());
            productModel1.setProduct_long_Desc(productModel.getProduct_long_Desc());
            productModel1.setProduct_small_Desc(productModel.getProduct_small_Desc());
            productRepository.save(productModel1);
        }

        return product;
    }


    //convert product to JSON and save it to database
    @Override
    public ProductModel getproductJSON(String ProductModel,MultipartFile[] files,String catid){
        ProductModel productJSON = new ProductModel();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            productJSON = objectMapper.readValue(ProductModel,ProductModel.class);
        }catch (IOException e){
            e.printStackTrace();
        }

        List<String> uploadURL = new ArrayList<>();
        Arrays.stream(files).forEach(file -> {
            String filename = fileStorageService.storeFile(file);
            String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(filename).toUriString();
            uploadURL.add(url);
        });
        long millis = System.currentTimeMillis();
        productJSON.setUpdateDate(new Date(millis));
        try {
            productJSON.setProduct_img1(uploadURL.get(0));
            productJSON.setProduct_img2(uploadURL.get(1));
            productJSON.setProduct_img3(uploadURL.get(2));
            productJSON.setProduct_img4(uploadURL.get(3));
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        productJSON.setAvaialable(true);
        Optional<CategoryModel> categoryModel = categoriesRepository.findById(Integer.parseInt(catid));
        if(categoryModel.isPresent()){
            CategoryModel c = categoryModel.get();
            productJSON.setCategoryModel(c);
        }
        productRepository.save(productJSON);
        return productJSON;
    }

    //delete product by ID
    @Override
    public boolean deleteproduct(Integer pid) {
        boolean result =false;
        try{
            Optional<ProductModel> productModelOptional = productRepository.findById(pid);
            productModelOptional.ifPresent(product -> {
                dpi.deleteProductImages(product.getProduct_img1(),product.getProduct_img2(),product.getProduct_img3(),product.getProduct_img4());
            });
            productRepository.deleteById(pid);
            result = true;
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return result;
    }

    @Override
    public OptionModel addSize(OptionModel optionModel) {
        return sizeOptionRepository.save(optionModel);
    }

    @Override
    public List<OptionModel> getAllSizes() {
        return sizeOptionRepository.findAll();
    }

    @Override
    public boolean addSizeToProduct(Integer product_id, Integer sizeoption_id) {

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
                productRepository.save(product);
            });
        });

        return productModelOptional.isPresent()&&optionModelOptional.isPresent();
    }

    @Override
    public boolean deleteSizeFromProduct(Integer product_id, Integer sizeoption_id) {
        Optional<ProductModel> productModelOptional = productRepository.findById(product_id);
        Optional<OptionModel> optionModelOptional = sizeOptionRepository.findById(sizeoption_id);

        productModelOptional.ifPresent(product -> {
            optionModelOptional.ifPresent(size -> {
                if(!product.getOptionModel().isEmpty()) {
                    product.getOptionModel().remove(size);
                    productRepository.save(product);
                }
            });
        });

        return productModelOptional.isPresent()&&optionModelOptional.isPresent();
    }

    @Override
    public CategoryModel addCategory(String categoryModel, MultipartFile file) {

        CategoryModel categoryModelJSON = new CategoryModel();
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            categoryModelJSON = objectMapper.readValue(categoryModel,CategoryModel.class);
        }catch (IOException e){
            e.printStackTrace();
        }
        String filename = fileStorageService.storeFile(file);
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(filename).toUriString();
        categoryModelJSON.setCategory_image(url);
        categoriesRepository.save(categoryModelJSON);
        return categoryModelJSON;
    }

    @Override
    public List<CategoryModel> getAllCategories() {
        return categoriesRepository.findAll();
    }

    @Override
    public boolean deleteCategory(Integer cat_id) {
        boolean result = false;
        try {
            categoriesRepository.findById(cat_id).ifPresent(category ->{
                int i = category.getCategory_image().length()-1;
                while(i >= 0){
                    if(category.getCategory_image().charAt(i) == '/')break;
                    i--;
                }
                fileStorageService.deleteFile(category.getCategory_image().substring(i+1));
                categoriesRepository.delete(category);
            });
            result = true;
        }catch (NoSuchElementException exception){
            exception.printStackTrace();
        }
        return result;
    }

    @Override
    public List<UserModel> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public CouponsModel addCoupon(CouponsModel couponsModel) {
        return couponRepository.save(couponsModel);
    }

    @Override
    public List<CouponsModel> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public Optional<CouponsModel> getCoupon(Integer cid) {
        return couponRepository.findById(cid);
    }

    @Override
    public boolean deleteCoupon(Integer cid) {
        try {
            couponRepository.deleteById(cid);
            return true;
        }catch (NoSuchElementException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addBanner(MultipartFile[] files,MultipartFile file5,MultipartFile file6, MultipartFile file7) {
        boolean result = false;
        if(!bannerRepository.findAll().isEmpty())return result;
        List<String> bannerModelList = new ArrayList<>();

        Arrays.stream(files).forEach(file -> {
            String filename = fileStorageService.storeFile(file);
            String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(filename).toUriString();
            bannerModelList.add(url);
        });
        String fileName5 = fileStorageService.storeFile(file5);
        String url5 = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(fileName5).toUriString();
        String fileName6 = fileStorageService.storeFile(file6);
        String url6 = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(fileName6).toUriString();
        String fileName7 = fileStorageService.storeFile(file7);
        String url7 = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(fileName7).toUriString();

        BannerModel bannerModel = new BannerModel();

            bannerModel.setBannerimg1(bannerModelList.get(0));
            bannerModel.setBannerimg2(bannerModelList.get(1));
            bannerModel.setBannerimg3(bannerModelList.get(2));
            bannerModel.setBannerimg4(bannerModelList.get(3));
            bannerModel.setBannerimg5(url5);
            bannerModel.setBannerimg6(url6);
            bannerModel.setBannerimg7(url7);
            bannerRepository.save(bannerModel);
            result =  true;

        return result;
    }

    @Override
    public List<BannerModel> getBanners() {
        return bannerRepository.findAll();
    }

    @Override
    public boolean deleteAllBanners() {
        List<BannerModel> bannerModels = bannerRepository.findAll();
        bannerModels.forEach(banner -> {
            List<String> banners = new ArrayList<>();
            banners.add(banner.getBannerimg1());
            banners.add(banner.getBannerimg2());
            banners.add(banner.getBannerimg3());
            banners.add(banner.getBannerimg4());
            banners.add(banner.getBannerimg5());
            banners.add(banner.getBannerimg6());
            banners.add(banner.getBannerimg7());

            if(dbi.deleteAllBanners(banners))
                bannerRepository.delete(banner);
        });
        return !bannerModels.isEmpty();
    }

    @Override
    public boolean createOrderStatus(OrderStatus orderStatus) {
         orderStatusRepository.save(orderStatus);
         return true;
    }

    @Override
    public List<OrderStatus> findAllOrderStatus() {
        return orderStatusRepository.findAll();
    }

    @Override
    public boolean deleteOrderStatusById(Integer order_status_id) {
        orderStatusRepository.deleteById(order_status_id);
        return true;
    }

    @Override
    public ShoppingCartModel getUsersCartByUserId(Integer user_id) {
        return shoppingCartRepository.findByUserId(user_id).orElse(null);
    }

    @Override
    public List<OrderModel> getUsersOrdersByUserId(Integer user_id) {
        return orderRepository.findByUserId(user_id);
    }

    @Override
    public boolean deleteUsersOrderByOrderId(Integer order_id) {
        boolean b = orderRepository.existsById(order_id);
        orderRepository.deleteById(order_id);
        return b;
    }

    @Override
    public boolean updateOrdersStatusOfOneOrder(Integer order_id, Integer orderstatus_id) {
        Optional<OrderStatus> orderStatusOptional = orderStatusRepository.findById(orderstatus_id);
        Optional<OrderModel> orderModelOptional = orderRepository.findById(order_id);

        orderModelOptional.ifPresent(order -> {
            orderStatusOptional.ifPresent(orderStatus -> {
                order.setOrderStatus(orderStatus);
                orderRepository.save(order);
            });
        });

        return orderModelOptional.isPresent()&&orderStatusOptional.isPresent();
    }

    @Override
    public List<OrderModel> getAllOrdersofAllUsers() {
        return orderRepository.findAll();
    }

    @Override
    public OrderModel getSingleOrderByOrderId(Integer order_id) {
        return orderRepository.findById(order_id).orElse(null);
    }


}
