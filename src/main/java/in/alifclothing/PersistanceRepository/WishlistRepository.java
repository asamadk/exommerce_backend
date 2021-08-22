package in.alifclothing.PersistanceRepository;

import in.alifclothing.model.WishlistModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<WishlistModel,Integer> {

    @Query("select w from WishlistModel w where w.userModel.user_id=:id")
    Optional<WishlistModel> findByUserId(@Param("id") Integer id);
}
