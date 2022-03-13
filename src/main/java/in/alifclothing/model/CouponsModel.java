package in.alifclothing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "coupons")
public class CouponsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int couponsId;
    private String couponName;
    @Column(nullable = true)
    private Integer couponDiscount;
    @Column(nullable = true)
    private Integer minimumPurchasePrice = 0;
    @Column(nullable = true)
    private Integer maximumDiscount = 0;
    @Column(nullable = true)
    private Date expireDate;
    @JsonIgnore
    @OneToMany(mappedBy = "couponsModel")
    private List<ShoppingCartModel> shoppingCartModel;

    public CouponsModel(String couponName, int couponDiscount) {
        this.couponName = couponName;
        this.couponDiscount = couponDiscount;
    }

    public CouponsModel() {

    }

    public CouponsModel(int couponsId, String couponName, int couponDiscount, int minimumPurchasePrice, int maximumDiscount, Date expireDate, List<ShoppingCartModel> shoppingCartModel) {
        this.couponsId = couponsId;
        this.couponName = couponName;
        this.couponDiscount = couponDiscount;
        this.minimumPurchasePrice = minimumPurchasePrice;
        this.maximumDiscount = maximumDiscount;
        this.expireDate = expireDate;
        this.shoppingCartModel = shoppingCartModel;
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

    public int getMinimumPurchasePrice() {
        return minimumPurchasePrice;
    }

    public void setMinimumPurchasePrice(int minimumPurchasePrice) {
        this.minimumPurchasePrice = minimumPurchasePrice;
    }

    public int getMaximumDiscount() {
        return maximumDiscount;
    }

    public void setMaximumDiscount(int maximumDiscount) {
        this.maximumDiscount = maximumDiscount;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public List<ShoppingCartModel> getShoppingCartModel() {
        return shoppingCartModel;
    }

    public void setShoppingCartModel(List<ShoppingCartModel> shoppingCartModel) {
        this.shoppingCartModel = shoppingCartModel;
    }
}
