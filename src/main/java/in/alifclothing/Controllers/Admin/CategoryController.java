package in.alifclothing.Controllers.Admin;

import in.alifclothing.Dto.Response;
import in.alifclothing.model.CategoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/admin")
public class CategoryController {

    @Autowired
    private in.alifclothing.Logic.adminLogic.adminLogic adminLogic;

    @PostMapping("/category")
    public ResponseEntity<Response<CategoryModel>> addcategory(@RequestBody CategoryModel categoryModel){

        Response<CategoryModel> response = adminLogic.addCategory(categoryModel);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<CategoryModel>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<CategoryModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @DeleteMapping("/category/{category_id}")
    public ResponseEntity<Response<String>> deletecategory(@PathVariable("category_id") Integer cat_id){

        Response<String> response = adminLogic.deleteCategory(cat_id);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<String>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
