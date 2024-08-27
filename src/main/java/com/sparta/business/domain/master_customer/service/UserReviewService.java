package com.sparta.business.domain.master_customer.service;

import com.sparta.business.domain.common.repository.ReviewRepository;
import com.sparta.business.domain.common.repository.StoreRepository;
import com.sparta.business.domain.master_customer.dto.UserReviewRequestDto;
import com.sparta.business.domain.master_customer.dto.UserReviewResponseDto;
import com.sparta.business.entity.Review;
import com.sparta.business.entity.Store;
import com.sparta.business.entity.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReviewService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public UserReviewResponseDto createReview(UserReviewRequestDto requestDto, User user) {
        Store store = storeRepository.findById(requestDto.getStoreId()).orElseThrow(() ->
            new IllegalArgumentException("store_id 에 해당하는 상점이 없습니다."));

        Review review = reviewRepository.save(new Review(requestDto, store, user));

        return new UserReviewResponseDto(review.getId(), "리뷰가 성공적으로 생성되었습니다.");
    }

    @Transactional
    public UserReviewResponseDto updateReview(UUID reviewId, UserReviewRequestDto requestDto, User user) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() ->
            new IllegalArgumentException("review_id 에 해당하는 리뷰가 없습니다."));
        review.update(requestDto);
        return new UserReviewResponseDto(review.getId(), "리뷰가 성공적으로 수정되었습니다.");
    }

    @Transactional
    public UserReviewResponseDto deleteReview(UUID reviewId, User user) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() ->
            new IllegalArgumentException("review_id 에 해당하는 리뷰가 없습니다."));
        reviewRepository.delete(review);
        return new UserReviewResponseDto(review.getId(), "리뷰가 성공적으로 삭제되었습니다.");

    }
}
