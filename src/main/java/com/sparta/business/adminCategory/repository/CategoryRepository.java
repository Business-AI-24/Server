package com.sparta.business.adminCategory.repository;

import com.sparta.business.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Optional<Category> findByType(String categoryType);


//    List<Category> findByTypeIn(String type);

}
