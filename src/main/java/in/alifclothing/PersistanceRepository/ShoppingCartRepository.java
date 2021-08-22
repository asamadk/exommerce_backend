package in.alifclothing.PersistanceRepository;

import in.alifclothing.model.ShoppingCartModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCartModel,Integer> {

    @Query("select s from ShoppingCartModel s where s.userModel.user_id= :id")
    Optional<ShoppingCartModel> findByUserId(@Param("id") Integer id);

}
