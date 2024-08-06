package Ecommerce_Online.Ecommerce.repository;

import Ecommerce_Online.Ecommerce.model.ProductOrder;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {

    List<ProductOrder> findByUserId(Integer userId);

    ProductOrder findByOrderId(String orderId);

}
