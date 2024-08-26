package com.sparta.business.domain.common.repository;

import com.sparta.business.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findAllByStore_Id(UUID storeId, Pageable pageable);
}
