package in.alifclothing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "coupons")
public class CouponsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int couponsId;
    private String couponName;
    private int couponDiscount;
    @JsonIgnore
    @OneToMany(mappedBy = "couponsModel")
    private List<ShoppingCartModel> shoppingCartModel;

    public CouponsModel(String couponName, int couponDiscount) {
        this.couponName = couponName;
        this.couponDiscount = couponDiscount;
    }

    public CouponsModel() {

    }

    public int getCouponsId() {
        return couponsId;
    }

    public void setCouponsId(int couponsId) {
        this.couponsId = couponsId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public int getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(int couponDiscount) {
        this.couponDiscount = couponDiscount;
    }


}
