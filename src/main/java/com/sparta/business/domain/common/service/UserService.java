package com.sparta.business.domain.common.service;

import com.sparta.business.domain.common.dto.*;
import com.sparta.business.domain.common.repository.*;
import com.sparta.business.entity.*;

import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final NoticeRepository noticeRepository;
    private final ReviewRepository reviewRepository;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    private final ProductRepository productRepository;


    public void signup(SignUpRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // nickName 중복확인
        String nickname = requestDto.getNickname();
        Optional<User> checkEmail = userRepository.findByNickname(nickname);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임 입니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.valueOf(requestDto.getType());
        if (role.equals(UserRoleEnum.MASTER)) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.MASTER;
        }

        // 사용자 등록
        userRepository.save(new User(requestDto, password, role));
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).get();
        User user = userRepository.findById(order.getUser().getId()).get();
        return new OrderResponseDto(order.getId(),order.getUser().getId(),order.getTotalPrice(),order.getUser().getAddress(),order.getRequest());
    }

    @Transactional(readOnly = true)
    public Page<StoreResponseDto> getStores(int page, int limit, Boolean sort) {
        Pageable pageable = PageRequest.of(page,limit);

        Page<Store> storePage = storeRepository.findAll(pageable);

        Page<StoreResponseDto> storeResponsePage = storePage.map(store -> {
            StoreResponseDto dto = new StoreResponseDto();
            dto.setName(store.getName());
            dto.setCategory(store.getCategory().getType()); // Category 테이블의 type 값을 가져와 설정
            dto.setAddress(store.getAddress());
            dto.setPhoneNumber(store.getPhoneNumber());
            dto.setIs_open(store.getIs_open());
            return dto;
        });

        return storeResponsePage;
    }

    @Transactional(readOnly = true)
    public Page<StoreResponseDto> getStoresForCategory(int page, int limit, Boolean sort, UUID categoryId) {
        Pageable pageable = PageRequest.of(page,limit);

        Page<Store> storePage = storeRepository.findAllByCategory_Id(categoryId,pageable);

        Page<StoreResponseDto> storeResponsePage = storePage.map(store -> {
            StoreResponseDto dto = new StoreResponseDto();
            dto.setName(store.getName());
            dto.setCategory(store.getCategory().getType()); // Category 테이블의 type 값을 가져와 설정
            dto.setAddress(store.getAddress());
            dto.setPhoneNumber(store.getPhoneNumber());
            dto.setIs_open(store.getIs_open());
            return dto;
        });

        return storeResponsePage;
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProductsForStore(int page, int limit, Boolean sort, UUID storeId) {
        Pageable pageable = PageRequest.of(page,limit);

        Page<Product> productPage = productRepository.findAllByStore_Id(storeId,pageable);

        Page<ProductResponseDto> productResponsePage = productPage.map(product -> {
            ProductResponseDto dto = new ProductResponseDto();
            dto.setProductId(product.getId());
            dto.setName(product.getName());
            dto.setPrice(product.getPrice());
            dto.setContent(product.getContent());
            return dto;
        });

        return productResponsePage;
    }

    @Transactional(readOnly = true)
    public Page<NoticeResponseDto> getNotices(int page, int limit, Boolean sort) {
        Pageable pageable = PageRequest.of(page,limit);

        Page<Notice> noticePage = noticeRepository.findAll(pageable);

        Page<NoticeResponseDto> noticeResponsePage = noticePage.map(notice -> {
            NoticeResponseDto dto = new NoticeResponseDto();
            dto.setTitle(notice.getTitle());
            dto.setContent(notice.getContent());
            return dto;
        });

        return noticeResponsePage;
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> getReviewsForStore(int page, int limit, Boolean sort, UUID storeId) {
        Pageable pageable = PageRequest.of(page,limit);

        Page<Review> reviewPage = reviewRepository.findAllByStore_Id(storeId,pageable);

        Page<ReviewResponseDto> reviewResponsePage = reviewPage.map(review -> {
            ReviewResponseDto dto = new ReviewResponseDto();
            dto.setNickname(review.getUser().getNickname());
            dto.setComment(review.getComment());
            dto.setRating(review.getRating());
            return dto;
        });

        return reviewResponsePage;
    }
}
