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

    @Transactional
    public ResponseEntity<String> addProduct(ProductRequestDto productRequestDto, User user) {
        //권한 확인
        if (!UserRoleEnum.OWNER.equals(user.getRole())) {
            return ResponseEntity.status(403).body("메뉴 등록 권한이 없습니다.");
        }

        //상점 정보 조회
        Store store = storeRepository.findById(productRequestDto.getStoreId())
                        .orElseThrow(()-> new RuntimeException("상점이 존재하지 않습니다."));


        // 사용자가 상점의 소유자인지 확인
        if (!store.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("이 상점의 소유자가 아닙니다.");
        }


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

    @Transactional
    public ResponseEntity<String> updateProduct(ProductEditRequestDto productEditRequestDto, User user) {
        //권한 확인
        if (!UserRoleEnum.OWNER.equals(user.getRole())) {
            return ResponseEntity.status(403).body("메뉴 수정 권한이 없습니다.");
        }

        //상점 정보 조회
        Store store = storeRepository.findById(productEditRequestDto.getStoreId())
                .orElseThrow(()-> new RuntimeException("상점이 존재하지 않습니다."));



        // 사용자가 상점의 소유자인지 확인
        if (!store.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("이 상점의 소유자가 아닙니다.");
        }



        //메뉴 저장
        Product product = Product.builder()
                .name(productEditRequestDto.getProductName())
                .content(productEditRequestDto.getProductDescription())
                .price(productEditRequestDto.getProductPrice())
                .store(store)
                .build();

        productRepository.save(product);


        return ResponseEntity.ok("메뉴가 성공적으로 수정되었습니다.");

    }
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
