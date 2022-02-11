package in.alifclothing.Controllers.Admin;

import in.alifclothing.Dto.Response;
import in.alifclothing.Logic.adminLogic.adminLogic;
import in.alifclothing.model.OrderModel;
import in.alifclothing.model.OrderStatus;
import in.alifclothing.model.ShoppingCartModel;
import in.alifclothing.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/admin")
public class UserAdminController {

    @Autowired
    private adminLogic adminLogic;

    @GetMapping("/users")
    public ResponseEntity<Response<UserModel>> getallusers(){

        Response<UserModel> response = adminLogic.getUsers();
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<UserModel>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<UserModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/orderstatus")
    public ResponseEntity<Response<String>> addOrderStatus(@RequestBody OrderStatus orderStatus){

        Response<String> response = adminLogic.createOrderStatus(orderStatus);
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<String>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/orderstatus")
    public ResponseEntity<Response<OrderStatus>> getAllOrderStatus(){

        Response<OrderStatus> response = adminLogic.findAllOrderStatus();
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<OrderStatus>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<OrderStatus>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/orderstatus")
    public ResponseEntity<Response<String>> deleteStatus(@RequestParam("order_status_id") String order_status_id){

        Response<String> response = adminLogic.deleteOrderStatusById(Integer.parseInt(order_status_id));
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<String>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<String>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/cart")
    public ResponseEntity<Response<ShoppingCartModel>> getUsersCart(@RequestParam("user_id") String user_id){

        Response<ShoppingCartModel> response = adminLogic.getUsersCartByUserId(Integer.parseInt(user_id));
        if(response.getErrorMap() == null){
            return new ResponseEntity<Response<ShoppingCartModel>>(response, HttpStatus.OK);
        }

        return new ResponseEntity<Response<ShoppingCartModel>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }





}
