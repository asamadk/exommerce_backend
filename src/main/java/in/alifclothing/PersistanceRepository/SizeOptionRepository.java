package in.alifclothing.PersistanceRepository;

import in.alifclothing.model.OptionModel;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeOptionRepository extends JpaRepository<OptionModel,Integer> {

}
