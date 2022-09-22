package in.alifclothing.Controllers.Admin;

import in.alifclothing.Dto.Response;
import in.alifclothing.Logic.adminLogic.adminLogic;
import in.alifclothing.model.BannerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/admin")
public class BannerController {

    @Autowired
    private adminLogic adminLogic;

    @PostMapping("/banner")
    public ResponseEntity<Response<String>> addbanner(@RequestBody BannerModel bannerModel){

        Response<String> response = adminLogic.addBanner(bannerModel);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<String>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/banner")
    public ResponseEntity<Response<BannerModel>> getBanners(){

        Response<BannerModel> response = adminLogic.getBanners();
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<BannerModel>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<BannerModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @DeleteMapping("/banner")
    public ResponseEntity<Response<String>> deleteBanner(){

        Response<String> response = adminLogic.deleteAllBanners();
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<String>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
