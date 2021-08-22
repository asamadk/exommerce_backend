package in.alifclothing.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int OrderStatusId;
    private String StatusName;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "orderStatus")
    @JsonIgnore
    private List<OrderModel> orderModel;

    public OrderStatus(String statusName, List<OrderModel> orderModel) {
        StatusName = statusName;
        this.orderModel = orderModel;
    }

    public OrderStatus(String statusName) {
        StatusName = statusName;
    }

    public OrderStatus() {

    }

    public int getOrderStatusId() {
        return OrderStatusId;
    }

    public void setOrderStatusId(int orderStatusId) {
        OrderStatusId = orderStatusId;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String statusName) {
        StatusName = statusName;
    }

    public List<OrderModel> getOrderModel() {
        return orderModel;
    }

    public void setOrderModel(List<OrderModel> orderModel) {
        this.orderModel = orderModel;
    }
}
