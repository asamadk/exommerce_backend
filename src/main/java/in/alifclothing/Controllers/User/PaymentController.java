package in.alifclothing.Controllers.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.paytm.pg.merchant.PaytmChecksum;

import in.alifclothing.Dto.PaytmCustom;
import in.alifclothing.Dto.Response;
import in.alifclothing.Helper.Contants;
import in.alifclothing.PersistanceRepository.OrderRepository;
import in.alifclothing.PersistanceRepository.ShoppingCartRepository;
import in.alifclothing.PersistanceRepository.UserRepository;
import in.alifclothing.model.OrderModel;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.razorpay.RazorpayClient;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/user")
public class PaymentController {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaytmCustom paytmCustom;


    //run create order controller and when successfull run this function and send response
    @PostMapping("/payment")
    public void initiateTransaction(@RequestBody() OrderModel orderModel, Principal principal, HttpServletResponse response) throws Exception {

        JSONObject paytmParams = new JSONObject();
        JSONObject body = new JSONObject();
        body.put("requestType", "Payment");
        body.put("mid", paytmCustom.getMerchantId());
        body.put("websiteName", paytmCustom.getWebsite());
        body.put("orderId", "ORDERID_"+orderModel.getOrderId());
        body.put("callbackUrl", paytmCustom.getDetails().get("CALLBACK_URL"));

        JSONObject txnAmount = new JSONObject();
        txnAmount.put("value", orderModel.getPrice());
        txnAmount.put("currency", "INR");

        JSONObject userInfo = new JSONObject();
        userInfo.put("custId", orderModel.getUserModel().getUser_id());
        body.put("txnAmount", txnAmount);
        body.put("userInfo", userInfo);

        String checkSum = PaytmChecksum.generateSignature(body.toString(),paytmCustom.getMerchantKey());

        JSONObject head = new JSONObject();
        head.put("signature", checkSum);

        paytmParams.put("body", body);
        paytmParams.put("head", head);

        String post_data = paytmParams.toString();
        URL url = new URL("https://securegw-stage.paytm.in/theia/api/v1/initiateTransaction?mid="+paytmCustom.getMerchantId()+"&orderId=ORDERID_"+orderModel.getOrderId());

        try{
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            String responseData = "";
            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                System.out.append("Response: " + responseData);
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.write(responseData);
                out.flush();
            }
            responseReader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/payment/options")
    public void fetchPaymentOptions(@RequestBody()OrderModel orderModel,@RequestParam("token") String token,HttpServletResponse response) throws Exception {

        JSONObject paytmParams = new JSONObject();

        JSONObject body = new JSONObject();
        body.put("mid", paytmCustom.getMerchantId());
        body.put("orderId", "ORDERID_"+orderModel.getOrderId());
        body.put("returnToken", "true");

        JSONObject head = new JSONObject();
        head.put("tokenType", "TXN_TOKEN");
        head.put("token", token);

        paytmParams.put("body", body);
        paytmParams.put("head", head);

        String post_data = paytmParams.toString();

        URL url = new URL("https://securegw-stage.paytm.in/theia/api/v2/fetchPaymentOptions?mid="+paytmCustom.getMerchantId()+"&orderId=ORDERID_"+orderModel.getOrderId());

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            String responseData = "";
            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                System.out.append("Response: " + responseData);
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.write(responseData);
                out.flush();
            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @PostMapping("/payment/process")
    public void processPayment(@RequestBody() OrderModel orderModel,
                               @RequestParam("token") String token,
                               @RequestParam("info") String info,
                               HttpServletResponse response
                               ) throws MalformedURLException {
        JSONObject paytmParams = new JSONObject();
        //TODO : encrypt info afterwords
        JSONObject body = new JSONObject();
        body.put("requestType", "NATIVE");
        body.put("mid", paytmCustom.getMerchantId());
        body.put("orderId", "ORDERID_"+orderModel.getOrderId());
        body.put("paymentMode", orderModel.getPaymentMode());
        if(orderModel.getPaymentMode().equals(Contants.CREDIT_CARD_PAYMENT) || orderModel.getPaymentMode().equals(Contants.DEBIT_CARD_PAYMENT)){
            body.put("cardInfo", info);
        }else if(orderModel.getPaymentMode().equals(Contants.UPI_PAYMENT)){
            body.put("payerAccount",info);
        }
        body.put("authMode", "otp");

        JSONObject head = new JSONObject();
        head.put("txnToken", token);

        paytmParams.put("body", body);
        paytmParams.put("head", head);

        String post_data = paytmParams.toString();

        /* for Staging */
        URL url = new URL("https://securegw-stage.paytm.in/theia/api/v1/processTransaction?mid=YOUR_MID_HERE&orderId=ORDERID_98765");

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
            requestWriter.writeBytes(post_data);
            requestWriter.close();
            String responseData = "";
            InputStream is = connection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
            if ((responseData = responseReader.readLine()) != null) {
                System.out.append("Response: " + responseData);
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.write(responseData);
                out.flush();
            }
            responseReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


}
