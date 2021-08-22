package in.alifclothing.Controllers.Admin;

import in.alifclothing.Logic.adminLogic.adminLogic;
import in.alifclothing.model.CouponsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class CouponsController {

    @Autowired
    private adminLogic adminLogic;

    @PostMapping("/addcoupon")
    public CouponsModel addcoupon(@RequestBody CouponsModel couponsModel){
        return adminLogic.addCoupon(couponsModel);
    }

    @GetMapping("/coupons")
    public List<CouponsModel> getallcoupons(){
        return adminLogic.getAllCoupons();
    }

    @GetMapping("/coupon/{coupon_id}")
    public Optional<CouponsModel> getcoupon(@PathVariable("coupon_id") Integer cid){
        return adminLogic.getCoupon(cid);
    }

    @DeleteMapping("/delete-coupon/{coupon_id}")
    public ResponseEntity<String> deletecoupon(@PathVariable("coupon_id") Integer cid){
        if(adminLogic.deleteCoupon(cid))return ResponseEntity.ok().body("Delete Success");
        return ResponseEntity.unprocessableEntity().body("Not Deleted");
    }
}
