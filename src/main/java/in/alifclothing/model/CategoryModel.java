package in.alifclothing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.engine.internal.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
public class CategoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int category_Id;
    private String category_Name;
    @JsonIgnore
   @OneToMany(cascade = CascadeType.ALL,mappedBy = "categoryModel")
    private List<ProductModel> productModel;

    public CategoryModel(String category_Name) {
        this.category_Name = category_Name;
    }

    public CategoryModel(String category_Name, List<ProductModel> productModel) {
        this.category_Name = category_Name;
        this.productModel = productModel;
    }

    public List<ProductModel> getProductModel() {
        return productModel;
    }

    public void setProductModel(List<ProductModel> productModel) {
        this.productModel = productModel;
    }

    public CategoryModel(){}

    public int getCategory_Id() {
        return category_Id;
    }

    public void setCategory_Id(int category_Id) {
        this.category_Id = category_Id;
    }

    public String getCategory_Name() {
        return category_Name;
    }

    public void setCategory_Name(String category_Name) {
        this.category_Name = category_Name;
    }
}
