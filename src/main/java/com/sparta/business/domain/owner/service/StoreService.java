package com.sparta.business.domain.owner.service;

import com.sparta.business.domain.common.repository.OrderRepository;
import com.sparta.business.domain.common.repository.StoreRepository;
import com.sparta.business.domain.common.repository.UserRepository;
import com.sparta.business.domain.master.repository.CategoryRepository;
import com.sparta.business.domain.master.repository.RegionRepository;
import com.sparta.business.domain.owner.dto.StoreEditRequestDto;
import com.sparta.business.domain.owner.dto.StoreRequestDto;
import com.sparta.business.entity.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final OrderRepository orderRepository;



    //권한 확인 메서드
    private void validateOwner(User user) {
        if(!UserRoleEnum.OWNER.equals(user.getRole())) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
    }

    //사용자 조회 메서드
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    }

    //상점 등록
    @Transactional
    public ResponseEntity<String> createStore(StoreRequestDto storeRequestDto, User user) {
        // 권한 확인
       validateOwner(user);


        // Region 및 Category 조회
        Region region = regionRepository.findById(storeRequestDto.getRegionId())
                .orElseThrow(() -> new RuntimeException("지역이 존재하지 않습니다."));
        Category category = categoryRepository.findById(storeRequestDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("카테고리가 존재하지 않습니다."));

        // 상점 저장
        Store store = Store.builder()
                .name(storeRequestDto.getName())
                .address(storeRequestDto.getAddress())
                .phoneNumber(storeRequestDto.getPhoneNumber())
                .region(region)
                .category(category)
                .user(user)
                .build();

        storeRepository.save(store);
        return ResponseEntity.ok("상점이 성공적으로 등록되었습니다.");
    }

    @Transactional
    public ResponseEntity<String> updateStore(UUID storeId, StoreEditRequestDto storeEditRequestDto, String username) {

        //사용자 정보 조회 및 권한 확인
        User user = getUserByUsername(username);
        validateOwner(user);


        //카테고리 조회
        Category category = categoryRepository.findById(storeEditRequestDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        //기존 상점 정보 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        Region region = regionRepository.findById(storeEditRequestDto.getRegionId())
                .orElseThrow(() -> new RuntimeException("지역이 존재하지 않습니다."));

        // 상점 정보 업데이트
        store.setName(storeEditRequestDto.getName());
        store.setAddress(storeEditRequestDto.getAddress());
        store.setPhoneNumber(storeEditRequestDto.getPhoneNumber());
        store.setCategory(category);
        store.setRegion(region); // 필요시 지역 수정 추가

        storeRepository.save(store);
        return ResponseEntity.ok("상점정보가 성공적으로 업데이트되었습니다.");
    }

    //삭제
    @Transactional
    public ResponseEntity<String> deleteStore(String username, UUID storeId) {
    // 사용자 정보 가져옴
        User user = getUserByUsername(username);
        validateOwner(user);



        //상접 삭제 로직
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("상점을 찾을 수 없습니다."));

        //상점 삭제
        storeRepository.delete(store);
        return ResponseEntity.ok("상점이 삭제처리되었습니다.");

    }

    //주문 거절

    public ResponseEntity<String> rejectOrder(String username, UUID orderId) {

        //사용자 정보 가져옴
        User user = getUserByUsername(username);

        //권한 확인
        validateOwner(user);

        //주문 건에 대한 정보 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("해당하는 주문이 존재하지 않습니다."));


        //상점 조회
        Store store=order.getStore();

        //사용자가 order를 거절할 수 있는 상점의 소유자 인지 확인

        if(!store.getUser().equals(user)){
            return ResponseEntity.status(403).body("주문을 거절할 수 있는 권한이 없습니다.");
        }

        //주문 거절 로직
        orderRepository.delete(order);

        return ResponseEntity.ok("상품이 성공적으로 삭제되었습니다.");
    }




}
