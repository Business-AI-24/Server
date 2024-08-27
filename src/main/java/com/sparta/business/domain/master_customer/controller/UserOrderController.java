package com.sparta.business.domain.master_customer.controller;

import com.sparta.business.domain.master_customer.dto.UserOrderListResponseDto;
import com.sparta.business.domain.master_customer.dto.UserOrderRequestDto;
import com.sparta.business.domain.master_customer.dto.UserOrderResponseDto;
import com.sparta.business.domain.master_customer.service.UserOrderService;
import com.sparta.business.entity.User;
import com.sparta.business.entity.UserRoleEnum;
import com.sparta.business.filter.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/customer/orders")
public class UserOrderController {

    private final UserOrderService userOrderService;

    /**
     * 주문상품에 담긴 상품들을 주문하는 로직
     * @param userDetails
     * @return
     */
    @PostMapping("/{store_id}")
    public ResponseEntity<UserOrderResponseDto> createOrder(
        @PathVariable("store_id") String id,
        @RequestBody UserOrderRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        try {
            User user = userDetails.getUser();
            if (user.getRole().equals(UserRoleEnum.OWNER)) {
                throw new AccessDeniedException("접근 권한이 없습니다.");
            }
            return ResponseEntity.ok().body(userOrderService.createOrder(id, requestDto, user));
        } catch (AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            log.error("리뷰 생성 중 오류 발생: ", e);
            throw new RuntimeException("리뷰 생성 중 오류가 발생했습니다.");
        }
    }

    @GetMapping
    public ResponseEntity<Page<UserOrderListResponseDto>> getOrders(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "sort", defaultValue = "createdAt,desc") String sort,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        try {
            String[] sortParams = sort.split(",");
            String sortBy = sortParams[0];
            Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
            PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            User user = userDetails.getUser();
            if (user.getRole().equals(UserRoleEnum.OWNER)) {
                throw new AccessDeniedException("접근 권한이 없습니다.");
            }
            return ResponseEntity.ok().body(userOrderService.getOrders(pageable, user));
        } catch (AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            log.error("리뷰 생성 중 오류 발생: ", e);
            throw new RuntimeException("리뷰 생성 중 오류가 발생했습니다.");
        }

    }

    @DeleteMapping("/{order_id}")
    public ResponseEntity<UserOrderResponseDto> deleteOrderAndPayment(
        @PathVariable("order_id") String id,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        try {
            User user = userDetails.getUser();
            if (user.getRole().equals(UserRoleEnum.OWNER)) {
                throw new AccessDeniedException("접근 권한이 없습니다.");
            }
            return ResponseEntity.ok().body(userOrderService.deleteOrderAndPayment(id,user));
        } catch (AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            log.error("리뷰 생성 중 오류 발생: ", e);
            throw new RuntimeException("리뷰 생성 중 오류가 발생했습니다.");
        }
    }

}
