package in.alifclothing.Controllers.Admin;

import com.razorpay.Order;
import in.alifclothing.Dto.Response;
import in.alifclothing.Logic.adminLogic.adminLogic;
import in.alifclothing.model.OrderModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/admin")
public class OrderController {

    @Autowired
    private adminLogic adminLogic;

    @GetMapping("/orders")
    public ResponseEntity<Response<OrderModel>> getAllOrders(){

        Response<OrderModel> response = adminLogic.getAllOrdersofAllUsers();
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<OrderModel>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<OrderModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/order")
    public ResponseEntity<Response<OrderModel>> addOrder(
            @RequestBody OrderModel orderModel,
            @RequestParam("userId") String userId
    ){
        Response<OrderModel> response = adminLogic.addOrder(orderModel, userId);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<OrderModel>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<OrderModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/order")
    public ResponseEntity<Response<OrderModel>> getUsersOrders(){

        String email =  SecurityContextHolder.getContext().getAuthentication().getName();
        Response<OrderModel> response = adminLogic.getUsersOrdersByUserId(email);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<OrderModel>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<OrderModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/order/{order_id}")
    public ResponseEntity<Response<String>> deleteUsersOrder(@PathVariable("order_id") Integer order_id){

        Response<String> response = adminLogic.deleteUsersOrderByOrderId(order_id);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<String>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/order/update")
    public ResponseEntity<Response<String>> updateOrderStatus(@RequestParam("order") String order_id,@RequestParam("status") String orderstatus_id){

        Response<String> response = adminLogic.updateOrdersStatusOfOneOrder(Integer.parseInt(order_id),Integer.parseInt(orderstatus_id));
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<String>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/order/{order_id}")
    public ResponseEntity<Response<OrderModel>> getSingleOrder(@PathVariable("order_id") Integer order_id){

        Response<OrderModel> response = adminLogic.getSingleOrderByOrderId(order_id);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<OrderModel>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<OrderModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    //TODO : list of payments from order id

}
