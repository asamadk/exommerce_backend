package in.alifclothing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "product_option")
public class ProductOptionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int productOptionId;
    @JsonIgnore
    @ManyToOne
    private ProductModel productModel;
    @ManyToOne
    private OptionModel optionModel;

    public ProductOptionModel(ProductModel productModel, OptionModel optionModel) {
        this.productModel = productModel;
        this.optionModel = optionModel;
    }

    public ProductOptionModel() {
    }

    public int getProductOptionId() {
        return productOptionId;
    }

    public void setProductOptionId(int productOptionId) {
        this.productOptionId = productOptionId;
    }

    public ProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }

    public OptionModel getOptionModel() {
        return optionModel;
    }

    public void setOptionModel(OptionModel optionModel) {
        this.optionModel = optionModel;
    }
}
