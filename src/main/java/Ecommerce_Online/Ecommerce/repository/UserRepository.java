package Ecommerce_Online.Ecommerce.repository;

import Ecommerce_Online.Ecommerce.model.UserDtls;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDtls, Integer> {
    
    public UserDtls findByEmail(String email);

    public List<UserDtls> findByRole(String role);
    
    public UserDtls findByResetToken(String token);
    
    public Boolean existsByEmail(String email);
}
