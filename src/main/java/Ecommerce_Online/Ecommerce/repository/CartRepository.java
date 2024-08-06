package Ecommerce_Online.Ecommerce.repository;

import Ecommerce_Online.Ecommerce.model.Cart;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer>{
    
    public Cart findByProductIdAndUserId(Integer productId, Integer userId);

    public Integer countByUserId(Integer userId);
    
    public List<Cart> findByUserId(Integer userId);
}
