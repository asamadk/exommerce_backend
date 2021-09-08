package in.alifclothing.Controllers.Admin;

import in.alifclothing.model.CategoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class CategoryController {

    @Autowired
    private in.alifclothing.Logic.adminLogic.adminLogic adminLogic;

    @PostMapping("/category")
    public CategoryModel addcategory(@RequestPart("category") String categoryModel, @RequestPart("category_image") MultipartFile file){
        return adminLogic.addCategory(categoryModel,file);
    }

    @GetMapping("/categories")
    public List<CategoryModel> getallcategories(){
        return adminLogic.getAllCategories();
    }

    @DeleteMapping("/delete-category/{category_id}")
    public ResponseEntity<String> deletecategory(@PathVariable("category_id") Integer cat_id){
        if(adminLogic.deleteCategory(cat_id))return ResponseEntity.ok().body("Delete Successful");
        return ResponseEntity.badRequest().body("Delete not successful");
    }

}
