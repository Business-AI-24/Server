package com.sparta.business.adminCategory.service;

import com.sparta.business.adminCategory.dto.CategoryEditRequestDto;
import com.sparta.business.adminCategory.repository.CategoryRepository;
import com.sparta.business.domain.common.repository.UserRepository;
import com.sparta.business.entity.Category;
import com.sparta.business.entity.User;
import com.sparta.business.entity.UserRoleEnum;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    //카테고리 저장
    @Transactional

    public ResponseEntity<String> addCategory(String categoryType, User user) {

        //권한 확인
        if (!UserRoleEnum.MASTER.equals(user.getRole())) {
            System.out.println("CategoryService->" + user.getRole());
            return ResponseEntity.status(403).body("카테고리 등록 권한이 없습니다.");
        }

        //중복 확인
        Optional<Category> existingCategory = categoryRepository.findByType(categoryType);
        if (existingCategory.isPresent()) {
            return ResponseEntity.badRequest().body("이미 존재하는 카테고리 타입입니다.");
        }
            //카테고리 저장
            Category category = new Category();
            category.setType(categoryType);
            categoryRepository.save(category);
            return ResponseEntity.ok("카테고리가 성공적으로 등록되었습니다. ");


        }

    //카테고리 수정
    @Transactional
    public ResponseEntity<String> updateCategory(UUID categoryId, CategoryEditRequestDto categoryEditRequestDto, String username) {

        System.out.println("CategoryType"+categoryEditRequestDto.getCategoryType());
        System.out.println("CategoryService :"+categoryId);
        System.out.println("categorry username :"+username);

        //사용자 정보를 가져옴
        User user= userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("로그인 후 수정하세요."));

        // 권한 확인
        if (!UserRoleEnum.MASTER.equals(user.getRole())) {
            return ResponseEntity.status(403).body("권한이 없습니다.");
        }

        // 카테고리 수정 로직
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다."));

        //카테고리 수정
        category.setType(categoryEditRequestDto.getCategoryType());
        categoryRepository.save(category);


        return ResponseEntity.ok("카테고리가 성공적으로 수정되었습니다.");

    }
}



