package in.alifclothing.Controllers.Admin;

import in.alifclothing.FileHandlerService.fileStorageService;
import in.alifclothing.Logic.adminLogic.adminLogic;
import in.alifclothing.model.CategoryModel;
import in.alifclothing.model.OptionModel;
import in.alifclothing.model.ProductModel;
import org.aspectj.lang.annotation.DeclareWarning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class ProductController {

    @Autowired
    private adminLogic adminLogic;
    @Autowired
    private in.alifclothing.FileHandlerService.fileStorageService fileStorageService;

    public ProductController(fileStorageService fileStorageService){
        this.fileStorageService = fileStorageService;
    }


    @GetMapping("/products")
    public List<ProductModel> allproducts(){
        return adminLogic.getAllProducts();

    }

    //edit product can be used by single product details
    @GetMapping("/product/{product_id}")
    public Optional<ProductModel> singleproduct(@PathVariable("product_id") Integer pid){
        return adminLogic.getSingleProduct(pid);
    }

    @PutMapping("/updateproduct/{product_id}")
    public Optional<ProductModel> updateProduct(@RequestBody ProductModel productModel,@PathVariable("product_id") Integer pid){
        return adminLogic.updateProduct(productModel,pid);
    }

    @PostMapping("/addproduct")
    public ProductModel addproduct(@RequestPart("product") String ProductModel,@RequestPart("files") MultipartFile[] files,@RequestPart("category") String catid){
        return adminLogic.getproductJSON(ProductModel,files,catid);
    }


    @DeleteMapping("/deleteproduct/{product_id}")
    public ResponseEntity<String> deleteproduct(@PathVariable("product_id") Integer pid){
        if(adminLogic.deleteproduct(pid))return ResponseEntity.ok("Success");
        return ResponseEntity.internalServerError().body("Product not deleted");
    }

    @PostMapping("/addsizeoption")
    public OptionModel addsize(@RequestBody OptionModel optionModel){
        return adminLogic.addSize(optionModel);
    }

    @GetMapping("/sizeoption")
    public List<OptionModel> getAllSizeOptions(){
        return adminLogic.getAllSizes();
    }

    @PostMapping("/product/size/{product_id}/{sizeoption_id}")
    public ResponseEntity<String> addSizetoProduct(@PathVariable("product_id") Integer product_id, @PathVariable("sizeoption_id") Integer sizeoption_id){
        if(adminLogic.addSizeToProduct(product_id,sizeoption_id))return ResponseEntity.ok("Size added to product");
        return ResponseEntity.internalServerError().body("Unable to add product");
    }

    @DeleteMapping("/product/size/{product_id}/{sizeoption_id}")
    public ResponseEntity<String> deleteSizeFromProduct(@PathVariable("product_id") Integer product_id,@PathVariable("sizeoption_id") Integer sizeoption_id){
        if(adminLogic.deleteSizeFromProduct(product_id,sizeoption_id))return ResponseEntity.ok("Size deleted");
        return ResponseEntity.internalServerError().body("Cannot delete size");
    }

    //extract file name from front end to delete specific file
    @DeleteMapping("/product/{file_name}")
    public void deleteProductImages(@PathVariable("file_name") String file_name){
        fileStorageService.deleteFile(file_name);
    }

}
