package in.alifclothing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int OrderId;
    @ManyToOne
    private UserModel userModel;
    @ManyToMany
    private List<ProductModel> productModelList;
    private float price;
    private Date orderDate;
    private String orderTrackingNumber;
    @ManyToOne
    private OrderStatus orderStatus;
    private String razorpay_order_id;
    private String couponName;

    public OrderModel(UserModel userModel, List<ProductModel> productModelList,
                      float price, Date orderDate, String orderTrackingNumber,
                      OrderStatus orderStatus, String razorpay_order_id) {
        this.userModel = userModel;
        this.productModelList = productModelList;
        this.price = price;
        this.orderDate = orderDate;
        this.orderTrackingNumber = orderTrackingNumber;
        this.orderStatus = orderStatus;
        this.razorpay_order_id = razorpay_order_id;
    }

    public OrderModel(int orderId, UserModel userModel, List<ProductModel> productModelList, float price, Date orderDate, String orderTrackingNumber, OrderStatus orderStatus, String razorpay_order_id, String couponName) {
        OrderId = orderId;
        this.userModel = userModel;
        this.productModelList = productModelList;
        this.price = price;
        this.orderDate = orderDate;
        this.orderTrackingNumber = orderTrackingNumber;
        this.orderStatus = orderStatus;
        this.razorpay_order_id = razorpay_order_id;
        this.couponName = couponName;
    }

    public OrderModel() {

    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public List<ProductModel> getProductModelList() {
        return productModelList;
    }

    public void setProductModelList(List<ProductModel> productModelList) {
        this.productModelList = productModelList;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTrackingNumber() {
        return orderTrackingNumber;
    }

    public void setOrderTrackingNumber(String orderTrackingNumber) {
        this.orderTrackingNumber = orderTrackingNumber;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getRazorpay_order_id() {
        return razorpay_order_id;
    }

    public void setRazorpay_order_id(String razorpay_order_id) {
        this.razorpay_order_id = razorpay_order_id;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }
}
