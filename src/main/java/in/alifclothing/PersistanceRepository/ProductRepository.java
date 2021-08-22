package in.alifclothing.PersistanceRepository;

import in.alifclothing.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductModel,Integer> {
    @Query("select p from ProductModel p where p.categoryModel.category_Id=:id")
    List<ProductModel> findByCategoryId(@Param("id") Integer id);
}
