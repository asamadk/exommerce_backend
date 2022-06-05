package in.alifclothing.PersistanceRepository;

import in.alifclothing.model.WishlistModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<WishlistModel,Integer> {

    @Query("select w from WishlistModel w where w.userModel.user_id=:id")
    Optional<WishlistModel> findByUserId(@Param("id") Integer id);

    @Query("select w from WishlistModel w where w.userModel.user_id=:id")
    Page<WishlistModel> findByUserId(@Param("id") Integer id, Pageable pageable);
}
