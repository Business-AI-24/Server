package com.sparta.business.domain.owner.service;

import com.sparta.business.domain.common.repository.ProductRepository;
import com.sparta.business.domain.common.repository.StoreRepository;
import com.sparta.business.domain.common.repository.UserRepository;
import com.sparta.business.domain.owner.dto.ProductEditRequestDto;
import com.sparta.business.domain.owner.dto.ProductRequestDto;
import com.sparta.business.entity.Product;
import com.sparta.business.entity.Store;
import com.sparta.business.entity.User;
import com.sparta.business.entity.UserRoleEnum;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public ProductService(ProductRepository productRepository, UserRepository userRepository, StoreRepository storeRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }

    //권한과 소유자 확인 메서드
    private void vaildateUserAndStoreOwnership(User user,UUID storeId) {
        if(!UserRoleEnum.OWNER.equals(user.getRole())) {
            throw new AccessDeniedException("메뉴 접근 권한이 없습니다.");
        }

        Store store = storeRepository.findById(storeId)
                .orElseThrow(()-> new RuntimeException("상점이 존재하지 않습니다."));

        if(!store.getUser().getId().equals(user.getId())){
            throw new AccessDeniedException("이 상점의 소유주가 아닙니다.");
        }
    }

    // 상점 조회 메서드
    private Store getStore(UUID storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(()-> new RuntimeException("상점이 존재하지 않습니다."));
    }


    //상품 조회 메서드
    private Product getProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(()->new RuntimeException("상품이 존재하지 않습니다."));
    }



    // ProductService -> 상품 추가
    @Transactional
    public ResponseEntity<String> addProduct(ProductRequestDto productRequestDto, User user) {

        //권한 확인 & 상점 owner 확인
        vaildateUserAndStoreOwnership(user, productRequestDto.getStoreId());

        Store store = getStore(productRequestDto.getStoreId());

        //메뉴 저장
        Product product = Product.builder()
                .name(productRequestDto.getProductName())
                .content(productRequestDto.getProductDescription())
                .price(productRequestDto.getProductPrice())
                .store(store)
                .build();

        productRepository.save(product);


        return ResponseEntity.ok("메뉴가 성공적으로 등록되었습니다.");
    }

    //상품 수정
    @Transactional
    public ResponseEntity<String> updateProduct(UUID product_id, ProductEditRequestDto productEditRequestDto, User user) {

        //권한 확인 & 상점 owner 확인
        vaildateUserAndStoreOwnership(user, productEditRequestDto.getStoreId());

        //상점 정보 조회
        Store store = getStore(productEditRequestDto.getStoreId());

        //기존 상품 정보 조회
        Product product = getProduct(product_id);


        //상품 수정
        product.setName(productEditRequestDto.getProductName());
        product.setContent(productEditRequestDto.getProductDescription());
        product.setPrice(productEditRequestDto.getProductPrice());
        product.setStore(store);


        productRepository.save(product);


        return ResponseEntity.ok("메뉴가 성공적으로 수정되었습니다.");

    }


    //상품 삭제
    @Transactional
    public ResponseEntity<String> deleteProduct(String username, UUID productId) {
        //사용자 정보 가져옴
        User user= userRepository.findByUsername(username)
                .orElseThrow(()->new RuntimeException("사용자가 없습니다."));

        //권한 확인
        if (!UserRoleEnum.OWNER.equals(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }

        //상품조회
        Product product= productRepository.findById(productId)
                .orElseThrow(()->new RuntimeException("삭제하려는 상품이 존재하지 않습니다."));

        //상점 조회
        Store store = product.getStore();

        //사용자가 상점의 소유자인지 확인
        if (!store.getUser().equals(user)){
            return ResponseEntity.status(403).body("이 상점의 소유자가 아닙니다.");
        }

        //메뉴 삭제 로직
        productRepository.delete(product);

        return ResponseEntity.ok("상품이 성공적으로 삭제되었습니다.");
    }

}
