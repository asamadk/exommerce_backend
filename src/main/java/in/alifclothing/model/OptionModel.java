package in.alifclothing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "size_option")
public class OptionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int sizeOptionId;
    private String optionName;
    @JsonIgnore
    @ManyToMany(mappedBy = "optionModel")
    List<ProductModel> productModelList;

    public OptionModel(String optionName, List<ProductModel> productModelList) {
        this.optionName = optionName;
        this.productModelList = productModelList;

    }

    public OptionModel (){}

    public int getSizeOptionId() {
        return sizeOptionId;
    }

    public void setSizeOptionId(int sizeOptionId) {
        this.sizeOptionId = sizeOptionId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public List<ProductModel> getProductModelList() {
        return productModelList;
    }

    public void setProductModelList(List<ProductModel> productModelList) {
        this.productModelList = productModelList;
    }
}
