package in.alifclothing.PersistanceRepository;

import in.alifclothing.model.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<CategoryModel,Integer> {
}
