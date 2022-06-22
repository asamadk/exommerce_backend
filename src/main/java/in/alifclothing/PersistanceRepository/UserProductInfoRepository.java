package in.alifclothing.PersistanceRepository;

import in.alifclothing.model.UserProductInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserProductInfoRepository extends JpaRepository<UserProductInformation,Integer> {

    @Query("select o from UserProductInformation o where o.userModel.user_id =:uid and o.productModel.product_id in :pid and o.orderModel IS NULL")
    List<UserProductInformation> getProductInformationByUserNameAndProductIds(@Param("pid") List<Integer> productId,@Param("uid") Integer userID);

    @Query("select o from UserProductInformation o where o.userModel.user_id =:uid and o.orderModel IS NULL")
    List<UserProductInformation> getProductInformationByUserId(@Param("uid") Integer userID);

    @Query("select o from UserProductInformation o where o.userModel.user_id =:uid")
    List<UserProductInformation> temp(@Param("uid") Integer userID);

}
