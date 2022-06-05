package in.alifclothing.PersistanceRepository;

import in.alifclothing.model.OrderModel;
import in.alifclothing.model.UserModel;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderModel,Integer> {
    @Query("select o from  OrderModel o where o.userModel.user_id= :id")
    List<OrderModel> findByUserId(@Param("id") Integer id);

    @Query("select o from OrderModel o where o.userModel.email = :email")
    List<OrderModel> findByEmailId(@Param("email") String email);

    @Query("delete from OrderModel o where o.userModel = :userId")
    void deleteOrders(@Param("userId") UserModel userModel);

//    Pageable paging = PageRequest.of(pageNo,pageSize);

    @Query("select o from  OrderModel o where o.userModel.user_id= :id")
    Page<OrderModel> findByUserIdPagination(@Param("id") Integer id, Pageable pageable);

}
