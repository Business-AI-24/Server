package com.sparta.business.domain.common.repository;

import com.sparta.business.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    // N+1 문제 해결
    @EntityGraph(attributePaths = {"user"})
    Page<Review> findAllByStore_Id(UUID storeId, Pageable pageable);
}
