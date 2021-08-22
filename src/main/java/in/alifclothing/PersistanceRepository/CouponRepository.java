package in.alifclothing.PersistanceRepository;

import in.alifclothing.model.CouponsModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<CouponsModel,Integer> {
    public CouponsModel findByCouponName(String name);
}
