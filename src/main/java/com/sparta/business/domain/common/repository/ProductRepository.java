package com.sparta.business.domain.common.repository;

import com.sparta.business.domain.master_customer.dto.OrderProductDto;
import com.sparta.business.entity.Product;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findAllByStore_Id(UUID storeId, Pageable pageable);
}
