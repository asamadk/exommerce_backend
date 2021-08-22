package in.alifclothing.Controllers.Admin;

import in.alifclothing.Logic.adminLogic.adminLogic;
import in.alifclothing.model.OrderModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class OrderController {

    @Autowired
    private adminLogic adminLogic;


    @GetMapping("/orders")
    public List<OrderModel> getAllOrders(){
        return adminLogic.getAllOrdersofAllUsers();
    }

    @GetMapping("/orders/{user_id}")
    public List<OrderModel> getUsersOrders(@PathVariable("user_id") Integer user_id){
        return adminLogic.getUsersOrdersByUserId(user_id);
    }

    @DeleteMapping("/order/{order_id}")
    public ResponseEntity<String> deleteUsersOrder(@PathVariable("order_id") Integer order_id){
        if(adminLogic.deleteUsersOrderByOrderId(order_id))return ResponseEntity.ok("Deleted");
        return ResponseEntity.internalServerError().body("Something went wrong");
    }

    @PutMapping("order/{order_id}/{orderstatus_id}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable("order_id") Integer order_id,@PathVariable("orderstatus_id") Integer orderstatus_id){
        if(adminLogic.updateOrdersStatusOfOneOrder(order_id,orderstatus_id))return ResponseEntity.ok("Updated");
        return ResponseEntity.internalServerError().body("Cannot update order status");
    }

    @GetMapping("order/{order_id}")
    public OrderModel getSingleOrder(@PathVariable("order_id") Integer order_id){
        return adminLogic.getSingleOrderByOrderId(order_id);
    }
    //TODO : list of payments from order id

}
