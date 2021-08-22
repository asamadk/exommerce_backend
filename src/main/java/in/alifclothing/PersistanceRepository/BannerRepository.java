package in.alifclothing.PersistanceRepository;

import in.alifclothing.model.BannerModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<BannerModel,Integer> {
}
