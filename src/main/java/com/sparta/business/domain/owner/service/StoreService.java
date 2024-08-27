package com.sparta.business.domain.owner.service;

import com.sparta.business.domain.common.repository.StoreRepository;
import com.sparta.business.domain.common.repository.UserRepository;
import com.sparta.business.domain.master.repository.CategoryRepository;
import com.sparta.business.domain.owner.dto.StoreEditRequestDto;
import com.sparta.business.domain.owner.dto.StoreRequestDto;
import com.sparta.business.entity.Category;
import com.sparta.business.entity.Store;
import com.sparta.business.entity.User;
import com.sparta.business.entity.UserRoleEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;


    @Transactional
    public ResponseEntity<String> createStore(StoreRequestDto storeRequestDto, User user) {
        // 권한 확인
        if (!UserRoleEnum.OWNER.equals(user.getRole())) {
            return ResponseEntity.status(403).body("등록 권한이 없습니다.");
        }

        // 중복 확인
        Optional<Store> existingStore = storeRepository.findByName(storeRequestDto.getName());
        if (existingStore.isPresent()) {
            return ResponseEntity.badRequest().body("이미 존재하는 상점 이름입니다.");
        }

        // Region 및 Category 조회
//        Region region = regionRepository.findById(storeRequestDto.getRegionId())
//                .orElseThrow(() -> new RuntimeException("Region not found"));
        Category category = categoryRepository.findById(storeRequestDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // 상점 저장
        Store store = Store.builder()
                .name(storeRequestDto.getName())
                .address(storeRequestDto.getAddress())
                .phoneNumber(storeRequestDto.getPhoneNumber())
//                .region(region)
                .category(category)
                .user(user)
                .build();

        storeRepository.save(store);
        return ResponseEntity.ok("상점이 성공적으로 등록되었습니다.");
    }

    @Transactional
    public ResponseEntity<String> updateStore(UUID storeId, StoreEditRequestDto storeEditRequestDto, String username) {

        //사용자 정보를 가져옴
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //권한 확인
        if(!UserRoleEnum.OWNER.equals(user.getRole())) {
            return ResponseEntity.status(403).body("수정 권한이 없습니다.");
        }

        //카테고리 조회
        Category category = categoryRepository.findById(storeEditRequestDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        //기존 상점 정보 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        // 상점 이름 중복 확인 (선택 사항)
        if (storeRepository.findByName(storeEditRequestDto.getName()).isPresent() &&
                !store.getName().equals(storeEditRequestDto.getName())) {
            return ResponseEntity.status(400).body("상점 이름이 이미 존재합니다.");
        }

        // 상점 정보 업데이트
        store.setName(storeEditRequestDto.getName());
        store.setAddress(storeEditRequestDto.getAddress());
        store.setPhoneNumber(storeEditRequestDto.getPhoneNumber());
        store.setCategory(category);
        // store.setRegion(region); // 필요시 지역 수정 추가
        // store.setUser(user); // 사용자는 수정하지 않는다고 가정
        storeRepository.save(store);
        return ResponseEntity.ok("상점정보가 성공적으로 업데이트되었습니다.");
    }

    @Transactional
    public ResponseEntity<String> deleteStore(String username, UUID storeId) {
    // 사용자 정보 가져옴
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

    //권한 확인
        if(!UserRoleEnum.OWNER.equals(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }

        //상접 삭제 로직
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("상점을 찾을 수 없습니다."));

        //상점 삭제
        storeRepository.delete(store);
        return ResponseEntity.ok("상점이 삭제처리되었습니다.");

    }



}
