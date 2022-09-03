package in.alifclothing.Controllers.User;

import com.paytm.pg.merchant.PaytmChecksum;

import in.alifclothing.Dto.PaytmCustom;
import in.alifclothing.Dto.Response;
import in.alifclothing.Helper.Contants;
import in.alifclothing.PersistanceRepository.OrderRepository;
import in.alifclothing.PersistanceRepository.ShoppingCartRepository;
import in.alifclothing.PersistanceRepository.UserRepository;
import in.alifclothing.model.OrderModel;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


//@CrossOrigin(origins = "http://localhost:3000", originPatterns = "https://alif-frontend.herokuapp.com/")
@CrossOrigin(origins = "http://43.204.244.109:3000/")
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
    public ResponseEntity<Response<String>> initiateTransaction(@RequestBody() OrderModel orderModel, Principal principal, HttpServletResponse response) throws Exception {

        Response<String> customResponse = new Response<>();
        Map<String,String> errorMap = new HashMap<>();

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
                customResponse.setResponseCode(Contants.OK_200);
                customResponse.setResponseDesc(Contants.SUCCESS);
                customResponse.setResponseWrapper(Arrays.asList(responseData));
//                PrintWriter out = response.getWriter();
//                response.setContentType("application/json");
//                response.setCharacterEncoding("UTF-8");
//                out.write(responseData);
//                out.flush();
            }
            responseReader.close();
        }catch (Exception e){
            errorMap.put(Contants.ERROR,e.getMessage());
            customResponse.setResponseWrapper(null);
            customResponse.setErrorMap(errorMap);
            customResponse.setResponseCode(Contants.INTERNAL_SERVER_ERROR);
            customResponse.setResponseDesc(e.getLocalizedMessage());
            e.printStackTrace();
        }
        if(customResponse.getResponseCode().equals(Contants.OK_200)) {
            return new ResponseEntity<Response<String>>(customResponse, HttpStatus.OK);
        }
        return new ResponseEntity<Response<String>>(customResponse, HttpStatus.INTERNAL_SERVER_ERROR);
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
                               @RequestParam("mode") String mode,
                               HttpServletResponse response
                               ) throws MalformedURLException {
        JSONObject paytmParams = new JSONObject();
        //TODO : encrypt info afterwords
        JSONObject body = new JSONObject();
        body.put("requestType", "NATIVE");
        body.put("mid", paytmCustom.getMerchantId());
        body.put("orderId", "ORDERID_"+orderModel.getOrderId());
        body.put("paymentMode", mode);
//        if(orderModel.getPaymentMode().equals(Contants.CREDIT_CARD_PAYMENT) || orderModel.getPaymentMode().equals(Contants.DEBIT_CARD_PAYMENT)){
//            body.put("cardInfo", info);
//        }else if(orderModel.getPaymentMode().equals(Contants.UPI_PAYMENT)){
//            body.put("payerAccount",info);
//        }
        if(mode  != null && (mode.equals(Contants.CREDIT_CARD_PAYMENT) || mode.equals(Contants.DEBIT_CARD_PAYMENT))){
            String[] cardDetails = info.split("x");
            body.put("cardInfo", "|" + cardDetails[0] + "|" + cardDetails[1] + "|" + cardDetails[2]);
//            body.put("cardInfo","|4111111111111111|123|092017");
//            System.out.println("HERE -> "+ "|" + cardDetails[0] + "|" + cardDetails[1] + "|" + cardDetails[2]);
        }else if(mode != null && mode.equals(Contants.UPI_PAYMENT)){
            body.put("payerAccount",info);
        }
        body.put("authMode", "otp");
//        body.put("preferredOtpPage","merchant");

        JSONObject head = new JSONObject();
        head.put("txnToken", token);

        paytmParams.put("body", body);
        paytmParams.put("head", head);

        String post_data = paytmParams.toString();

        /* for Staging */
        URL url = new URL("https://securegw-stage.paytm.in/theia/api/v1/processTransaction?mid="+paytmCustom.getMerchantId()+"&orderId=ORDERID_"+orderModel.getOrderId());
//        System.out.println("URL : "+url.toString());

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
