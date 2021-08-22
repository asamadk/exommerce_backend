package in.alifclothing.Controllers.Admin;

import in.alifclothing.Logic.adminLogic.adminLogic;
import in.alifclothing.model.OrderModel;
import in.alifclothing.model.OrderStatus;
import in.alifclothing.model.ShoppingCartModel;
import in.alifclothing.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class UserAdminController {

    @Autowired
    private adminLogic adminLogic;

    @GetMapping("/users")
    public List<UserModel> getallusers(){
        return adminLogic.getUsers();
    }

    @PostMapping("/orderstatus")
    public ResponseEntity<String> addOrderStatus(@RequestBody OrderStatus orderStatus){
        if(adminLogic.createOrderStatus(orderStatus))return ResponseEntity.ok("Added");
        return ResponseEntity.unprocessableEntity().body("Cannot add orderStaus");
    }

    @GetMapping("/orderstatus")
    public List<OrderStatus> getAllOrderStatus(){
        return adminLogic.findAllOrderStatus();
    }

    @DeleteMapping("/orderstatus/{order_status_id}")
    public ResponseEntity<String> deleteStatus(@PathVariable("order_status_id") Integer order_status_id){
        if(adminLogic.deleteOrderStatusById(order_status_id))return ResponseEntity.ok("Success");
        return ResponseEntity.internalServerError().body("Something went wrong");
    }

    @GetMapping("/cart/{user_id}")
    public ShoppingCartModel getUsersCart(@PathVariable("user_id") Integer user_id){
        return adminLogic.getUsersCartByUserId(user_id);
    }





}
