package in.alifclothing.PersistanceRepository;

import in.alifclothing.model.ProductModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductModel,Integer> {
    @Query("select p from ProductModel p where p.categoryModel.category_Id=:id")
    List<ProductModel> findByCategoryId(@Param("id") Integer id);

    @Query("select p from ProductModel p where p.categoryModel.category_Id=:id")
    List<ProductModel> findByCategoryIdPagable(@Param("id") Integer id, Pageable pageable);

    @Query("select p from ProductModel p order by p.updateDate DESC ")
    List<ProductModel> findInOrderDESC();

    @Query("select p from ProductModel p")
    Slice<ProductModel> findProductsByLimit(Pageable pageable);


    @Query("select p from ProductModel p where UPPER(p.product_name) like UPPER(:name)")
    List<ProductModel> findProductsBySearch(@Param("name") String name);

}
