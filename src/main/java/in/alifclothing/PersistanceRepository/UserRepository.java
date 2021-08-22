package in.alifclothing.PersistanceRepository;

import in.alifclothing.model.ShoppingCartModel;
import in.alifclothing.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel,Integer> {

    public UserModel findByEmail(String email);

}
