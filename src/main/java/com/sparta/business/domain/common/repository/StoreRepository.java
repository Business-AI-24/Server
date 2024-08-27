package com.sparta.business.domain.common.repository;

import com.sparta.business.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store,UUID> {
    Page<Store> findAllByCategory_Id(UUID categoryId,Pageable pageable);
}
