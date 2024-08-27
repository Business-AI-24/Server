package com.sparta.business.domain.master_customer.repository;

import com.sparta.business.entity.OrderProduct;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, UUID> {

}
