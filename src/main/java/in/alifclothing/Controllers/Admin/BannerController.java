package in.alifclothing.Controllers.Admin;

import in.alifclothing.Logic.adminLogic.adminLogic;
import in.alifclothing.model.BannerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class BannerController {

    @Autowired
    private adminLogic adminLogic;

    @PostMapping("/banner")
    public ResponseEntity<String> addbanner(@RequestPart("MainBanner") MultipartFile[] files,
                                                @RequestPart("Banner1") MultipartFile file1,
                                                @RequestPart("Banner2") MultipartFile file2,
                                                @RequestPart("Banner3") MultipartFile file3){
        if(adminLogic.addBanner(files,file1,file2,file3))return ResponseEntity.ok("Images uploaded succesfully");
        return ResponseEntity.unprocessableEntity().body("Image not uploaded");
    }

    @GetMapping("/banner")
    public List<BannerModel> getBanners(){
        return adminLogic.getBanners();
    }


    @DeleteMapping("/banner")
    public ResponseEntity<String> deleteBanner(){
        if(adminLogic.deleteAllBanners())return ResponseEntity.ok("Deleted");
        return ResponseEntity.unprocessableEntity().body("Could not be deleted");
    }

}
