package in.alifclothing.Controllers.User;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayException;
import in.alifclothing.PersistanceRepository.OrderRepository;
import in.alifclothing.PersistanceRepository.ShoppingCartRepository;
import in.alifclothing.PersistanceRepository.UserRepository;
import in.alifclothing.model.OrderModel;
import in.alifclothing.model.ShoppingCartModel;
import in.alifclothing.model.UserModel;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.razorpay.RazorpayClient;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class PaymentController {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/payment/{cart_id}")
    public ResponseEntity<String> createOrder(@PathVariable("cart_id") Integer cart_id, Principal principal) throws RazorpayException {


        shoppingCartRepository.findById(cart_id).ifPresent(cart -> {
            try {
                RazorpayClient razorpayClient = new RazorpayClient("rzp_test_porKXLxk1XNW5B","jZxL44I1zsYXgI1reqdhPt87");

                JSONObject object = new JSONObject();
                object.put("amount",cart.getTotal()*100);
                object.put("currency","INR");
                object.put("receipt","txn_02071999");
                //creating new order
                Order order = razorpayClient.Orders.create(object);
                cart.setRazorpay_order_id(order.get("id").toString());
                shoppingCartRepository.save(cart);
                //TODO : save payment id in database
            } catch (RazorpayException e) {
                e.printStackTrace();
            }

        });

        return ResponseEntity.ok("");
    }

}
