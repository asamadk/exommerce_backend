package in.alifclothing.Controllers.Admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import in.alifclothing.Dto.Response;
import in.alifclothing.FileHandlerService.fileStorageService;
import in.alifclothing.Logic.adminLogic.adminLogic;
import in.alifclothing.model.CategoryModel;
import in.alifclothing.model.OptionModel;
import in.alifclothing.model.OrderModel;
import in.alifclothing.model.ProductModel;
import org.aspectj.lang.annotation.DeclareWarning;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/admin")
public class ProductController {

    @Autowired
    private adminLogic adminLogic;
    @Autowired
    private final in.alifclothing.FileHandlerService.fileStorageService fileStorageService;

    public ProductController(fileStorageService fileStorageService){
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/products")
    public ResponseEntity<Response<ProductModel>> allproducts(){

        Response<ProductModel> response = adminLogic.getAllProducts();
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<ProductModel>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<ProductModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);

    }


    @GetMapping("/product/{product_id}")
    public ResponseEntity<Response<ProductModel>> singleproduct(@PathVariable("product_id") Integer pid){

        Response<ProductModel> response = adminLogic.getSingleProduct(pid);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<ProductModel>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<ProductModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/updateproduct/{product_id}")
    public ResponseEntity<Response<ProductModel>> updateProduct(@RequestBody ProductModel productModel,@PathVariable("product_id") Integer pid){

        Response<ProductModel> response = adminLogic.updateProduct(productModel,pid);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<ProductModel>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<ProductModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/product")
    public ResponseEntity<Response<ProductModel>> addproduct(@RequestBody ProductModel productModel) throws JsonProcessingException {

        Response<ProductModel> response = adminLogic.getproductJSON(productModel);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<ProductModel>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<ProductModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @DeleteMapping("/product")
    public ResponseEntity<Response<String>> deleteproduct(@RequestParam("product_id") String pid){

        Response<String> response = adminLogic.deleteproduct(Integer.parseInt(pid));
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<String>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/sizeoption")
    public ResponseEntity<Response<OptionModel>> addsize(@RequestBody OptionModel optionModel){

        Response<OptionModel> response = adminLogic.addSize(optionModel);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<OptionModel>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<OptionModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/sizeoption")
    public ResponseEntity<Response<OptionModel>> getAllSizeOptions(){

        Response<OptionModel> response = adminLogic.getAllSizes();
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<OptionModel>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<OptionModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/product/size")
    public ResponseEntity<Response<String>> addSizetoProduct(@RequestParam("product_id") String product_id, @RequestParam("sizeoption_id") String sizeoption_id){

        Response<String> response = adminLogic.addSizeToProduct(Integer.parseInt(product_id),Integer.parseInt(sizeoption_id));
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<String>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/product/size")
    public ResponseEntity<Response<String>> deleteSizeFromProduct(@RequestParam("product_id") String product_id,@RequestParam("sizeoption_id") String sizeoption_id){

        Response<String> response = adminLogic.deleteSizeFromProduct(Integer.parseInt(product_id),Integer.parseInt(sizeoption_id));
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<String>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //extract file name from front end to delete specific file
    @DeleteMapping("/product/{file_name}")
    public void deleteProductImages(@PathVariable("file_name") String file_name){
        fileStorageService.deleteFile(file_name);
    }

}
