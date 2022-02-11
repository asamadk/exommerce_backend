package in.alifclothing.Controllers.Admin;

import in.alifclothing.Dto.Response;
import in.alifclothing.Logic.adminLogic.adminLogic;
import in.alifclothing.model.BannerModel;
import in.alifclothing.model.CouponsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/admin")
public class CouponsController {

    @Autowired
    private adminLogic adminLogic;

    @PostMapping("/coupon")
    public ResponseEntity<Response<CouponsModel>> addcoupon(@RequestBody CouponsModel couponsModel){

        Response<CouponsModel> response = adminLogic.addCoupon(couponsModel);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<CouponsModel>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<CouponsModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/coupons")
    public ResponseEntity<Response<CouponsModel>> getallcoupons(){

        Response<CouponsModel> response = adminLogic.getAllCoupons();
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<CouponsModel>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<CouponsModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/coupon/{coupon_id}")
    public ResponseEntity<Response<CouponsModel>> getcoupon(@PathVariable("coupon_id") Integer cid){

        Response<CouponsModel> response = adminLogic.getCoupon(cid);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<CouponsModel>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<CouponsModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/coupon/{coupon_id}")
    public ResponseEntity<Response<String>> deletecoupon(@PathVariable("coupon_id") Integer cid){

        Response<String> response = adminLogic.deleteCoupon(cid);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<String >>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
