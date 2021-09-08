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
    private String category_image;
    @JsonIgnore
   @OneToMany(cascade = CascadeType.ALL,mappedBy = "categoryModel")
    private List<ProductModel> productModel;

    public CategoryModel(String category_Name) {
        this.category_Name = category_Name;
    }

    public CategoryModel(String category_Name, List<ProductModel> productModel, String category_image) {
        this.category_Name = category_Name;
        this.productModel = productModel;
        this.category_image = category_image;
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

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }
}
