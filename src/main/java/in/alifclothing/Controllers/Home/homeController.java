package in.alifclothing.Controllers.Home;

import in.alifclothing.Logic.homeLogic.homeLogic;
import in.alifclothing.Logic.userLogic.userLogic;
import in.alifclothing.model.CategoryModel;
import in.alifclothing.model.CouponsModel;
import in.alifclothing.model.ProductModel;
import in.alifclothing.model.UserModel;
import javassist.NotFoundException;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

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

    public homeController(in.alifclothing.FileHandlerService.fileStorageService fileStorageService){
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/test")
    public String testing(){
        return "Sucesfull";
    }

    @PostMapping("/register")
    public UserModel addUser(@RequestBody UserModel user){
        return homeLogic.persistUser(user);
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

    @GetMapping("/products")
    public List<ProductModel> fetchproducts(){
        return userLogic.getAllProducts();
    }

    @GetMapping("/product/{product_id}")
    public ProductModel fetchSingleProduct(@PathVariable("product_id") Integer product_id){
        return userLogic.getSingleProduct(product_id);
    }

    @GetMapping("/product/category/{category_id}")
    public List<ProductModel> fetchProductsByCategories(@PathVariable("category_id") Integer category_id){
        return userLogic.getAllProductsByCategory(category_id);
    }

    @GetMapping("/coupons")
    public List<CouponsModel> fetchCoupons(){
        return userLogic.getAllCoupons();
    }

    @GetMapping("/categories")
    public List<CategoryModel> fetchCategories(){
        return userLogic.getAllCategories();
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> processForgotPassword(@RequestPart("email") String email,HttpServletRequest request) throws NotFoundException {
        try{
            String token = RandomString.make(45);
            homeLogic.updateResetPassword(token,email);
            //generate reset password link
            String siteURL = request.getRequestURL().toString().replace(request.getServletPath(),"");
            String passwordResetLink = siteURL+"/reset_password?token="+token;
            System.out.println(passwordResetLink);
            //send mail
            homeLogic.sendEmail(email,passwordResetLink);

        }catch (NotFoundException | MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("We have send email to change your password");
    }

    @GetMapping("/reset_password")
    public ResponseEntity<String> showResetPasswordForm(@Param(value = "token") String token){
        if(homeLogic.checkRestPaswordLink(token))return ResponseEntity.ok("Token verified");
        return ResponseEntity.internalServerError().body("Token not verified");
    }
    //previous link when clicked a form with change password will appear and the the below post mapping will do its job
    @PostMapping("/reset_password")
    public ResponseEntity<String> changePasswordUsingLink(@Param(value = "token") String token,@RequestPart("password") String password,HttpServletRequest request){
        if(homeLogic.checkResetPasswordLinkandChangePassword(token,password))return ResponseEntity.ok("Password changed succesfully");
        return ResponseEntity.internalServerError().body("Password not changed");
    }

}
