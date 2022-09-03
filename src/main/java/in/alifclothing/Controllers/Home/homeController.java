package in.alifclothing.Controllers.Home;

import in.alifclothing.Dto.ContactUs;
import in.alifclothing.Dto.Response;
import in.alifclothing.Helper.Contants;
import in.alifclothing.Logic.homeLogic.homeLogic;
import in.alifclothing.Logic.userLogic.userLogic;
import in.alifclothing.model.*;
import javassist.NotFoundException;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "http://43.204.244.109:3000/")
@RestController
public class homeController {

    @Autowired
    private homeLogic homeLogic;
    @Autowired
    private in.alifclothing.FileHandlerService.fileStorageService fileStorageService;
    @Autowired
    private userLogic userLogic;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private in.alifclothing.Logic.adminLogic.adminLogic adminLogic;

    public homeController(in.alifclothing.FileHandlerService.fileStorageService fileStorageService){
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/test")
    public ResponseEntity<Response<String>> testing(){
        Response<String> response = new Response<>();
        response.setResponseCode(Contants.OK_200);
        response.setResponseWrapper(Arrays.asList("Succesfull"));
        response.setErrorMap(null);
        response.setResponseDesc("Working Fine");
        return new ResponseEntity<Response<String>>(response,HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Response<?>> addUser(@RequestBody UserModel user){
        Response<?> response = homeLogic.persistUser(user);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<?>>(response,HttpStatus.OK);
        }

        return new ResponseEntity<Response<?>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadimage(@PathVariable String fileName, HttpServletRequest request){
        Resource resource = fileStorageService.downloadFile(fileName);
        String mimeType;
        try {
            mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
//                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;fileName="+resource.getFilename())
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline;fileName="+resource.getFilename()) //to render in browser insetead of downloading
                .body(resource);
    }

    @GetMapping("/mail/order/details/{orderId}")
    public ResponseEntity<Response<OrderModel>> getOrderById(@PathVariable String orderId){
        Response<OrderModel> response = adminLogic.getSingleOrderByOrderId(Integer.parseInt(orderId));
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<OrderModel>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)){
                return new ResponseEntity<Response<OrderModel>>(response,HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Response<OrderModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/products")
    public ResponseEntity<Response<ProductModel>> fetchproducts(@RequestParam("order") String orderBy,
                                                                @RequestParam("page") String page,
                                                                @RequestParam("limit") String limit){
        Response<ProductModel> response = userLogic.getAllProducts(orderBy,page,limit);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<ProductModel>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)){
                return new ResponseEntity<Response<ProductModel>>(response,HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<Response<ProductModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/product/{product_id}")
    public ResponseEntity<Response<ProductModel>> fetchSingleProduct(@PathVariable("product_id") Integer product_id){

        Response<ProductModel> response = userLogic.getSingleProduct(product_id);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<ProductModel>>(response,HttpStatus.OK);
        }

        return new ResponseEntity<Response<ProductModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/product/category/{category_id}")
    public ResponseEntity<Response<ProductModel>> fetchProductsByCategories(@PathVariable("category_id") Integer category_id,
        @RequestParam("page") String page, @RequestParam("size") String size
    ){
        Response<ProductModel> response = userLogic.getAllProductsByCategory(category_id,page,size);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<ProductModel>>(response,HttpStatus.OK);
        }

        return new ResponseEntity<Response<ProductModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/coupons")
    public ResponseEntity<Response<CouponsModel>> fetchCoupons(){
        Response<CouponsModel> response = userLogic.getAllCoupons();
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<CouponsModel>>(response,HttpStatus.OK);
        }

        return new ResponseEntity<Response<CouponsModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/categories")
    public ResponseEntity<Response<CategoryModel>> fetchCategories() {

        Response<CategoryModel> response = userLogic.getAllCategories();
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<CategoryModel>>(response,HttpStatus.OK);
        }

        return new ResponseEntity<Response<CategoryModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @PostMapping("/resetPassword")
    public ResponseEntity<Response<?>> processForgotPassword(@RequestParam("email") String email, HttpServletRequest request) throws NotFoundException {

        String token = RandomString.make(45);
        Response<?> response = homeLogic.updateResetPassword(token,email);
        try{
            //generate reset password link
            if(response.getErrorMap() == null){
                String siteURL = request.getRequestURL().toString().replace(request.getServletPath(),"");
                String passwordResetLink = siteURL+"/reset_password?token="+token;
                homeLogic.sendEmail(email,passwordResetLink);
                return new ResponseEntity<Response<?>>(response, HttpStatus.OK);
            }else if(response.getResponseCode().equals(Contants.NOT_FOUND_404)){
                return new ResponseEntity<Response<?>>(response, HttpStatus.NOT_FOUND);
            }

        }catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<Response<?>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/contactUsMail")
    public ResponseEntity<Response<String>> contactUsMail(@RequestBody ContactUs contactUs) throws MessagingException, UnsupportedEncodingException {
        Response<String> response = homeLogic.sendMessageContactUs(contactUs.getName(),contactUs.getEmail(),contactUs.getSubject(),contactUs.getBody());
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<String>>(response,HttpStatus.OK);
        }
        return new ResponseEntity<Response<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/reset_password")
    public ResponseEntity<Response<String>> showResetPasswordForm(@RequestParam(value = "token") String token){
        Response<String> response = homeLogic.checkRestPaswordLink(token);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<String>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)) {
                return new ResponseEntity<Response<String>>(response, HttpStatus.NOT_FOUND);
            }else if(response.getResponseCode().equals(Contants.CLIENT_400)){
                return new ResponseEntity<Response<String>>(response,HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<Response<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //previous link when clicked a form with change password will appear and the the below post mapping will do its job
    @PostMapping("/changePassword")
    public ResponseEntity<String> changePasswordUsingLink(@RequestParam(value = "token") String token,@RequestPart("password") String password,HttpServletRequest request){
        if(homeLogic.checkResetPasswordLinkandChangePassword(token,password))return ResponseEntity.ok("Password changed succesfully");
        return ResponseEntity.internalServerError().body("Password not changed");
    }

    @GetMapping("/search")
    public ResponseEntity<Response<ProductModel>> showProductsOnSearch(@RequestParam("product_name") String productName){

        Response<ProductModel> response = userLogic.searchProducts(productName);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<ProductModel>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)) {
                return new ResponseEntity<Response<ProductModel>>(response, HttpStatus.NOT_FOUND);
            }else if(response.getResponseCode().equals(Contants.CLIENT_400)){
                return new ResponseEntity<Response<ProductModel>>(response,HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<Response<ProductModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/tailor/details/{orderId}")
    public ResponseEntity<Response<UserProductInformation>> getProductDetailsForTailor(@PathVariable String orderId){
        Response<UserProductInformation> response = homeLogic.getProductStitchDetails(Integer.parseInt(orderId));
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<UserProductInformation>>(response,HttpStatus.OK);
        }else{
            if(response.getResponseCode().equals(Contants.NOT_FOUND_404)) {
                return new ResponseEntity<Response<UserProductInformation>>(response, HttpStatus.NOT_FOUND);
            }else if(response.getResponseCode().equals(Contants.CLIENT_400)){
                return new ResponseEntity<Response<UserProductInformation>>(response,HttpStatus.UNAUTHORIZED);
            }
        }
        return new ResponseEntity<Response<UserProductInformation>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
