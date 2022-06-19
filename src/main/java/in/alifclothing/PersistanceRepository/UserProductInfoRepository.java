package in.alifclothing.PersistanceRepository;

import in.alifclothing.model.UserProductInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserProductInfoRepository extends JpaRepository<UserProductInformation,Integer> {

    @Query("select o from UserProductInformation o where o.userModel.user_id =:uid and o.productModel.product_id in :pid")
    List<UserProductInformation> getProductInformationByUserNameAndProductIds(
            @Param("pid") List<Integer> productId,@Param("uid") Integer userID);

}
