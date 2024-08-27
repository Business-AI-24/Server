package com.sparta.business.domain.master_customer.service;

import com.sparta.business.domain.common.repository.OrderRepository;
import com.sparta.business.domain.common.repository.ProductRepository;
import com.sparta.business.domain.common.repository.StoreRepository;
import com.sparta.business.domain.master_customer.dto.UserOrderListResponseDto;
import com.sparta.business.domain.master_customer.dto.UserOrderRequestDto;
import com.sparta.business.domain.master_customer.dto.UserOrderResponseDto;
import com.sparta.business.domain.master_customer.repository.OrderProductRepository;
import com.sparta.business.domain.master_customer.repository.PaymentRepository;
import com.sparta.business.entity.Order;
import com.sparta.business.entity.OrderProduct;
import com.sparta.business.entity.Payment;
import com.sparta.business.entity.Product;
import com.sparta.business.entity.Store;
import com.sparta.business.entity.User;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final StoreRepository storeRepository;
    private final PaymentRepository paymentRepository;


    /**
     * 상품 생성시에 total_price 값은 null 값으로 초기화
     * 동시에 고객이 주문할 상품 리스트를 OrderProduct 에 갱신하여 생성
     * 생성된 주문을 OrderProduct 와 매핑후 total_price 갱신
     * @param storeId
     * @param requestDto
     * @param user
     * @return
     */
    @Transactional
    public UserOrderResponseDto createOrder(String storeId, UserOrderRequestDto requestDto, User user) {
        Store store = storeRepository.findById(UUID.fromString(storeId)).orElseThrow(() ->
            new IllegalArgumentException("해당 가게는 존재하지 않습니다."));

        Payment payment = paymentRepository.save(new Payment(requestDto));
        Order order = orderRepository.save(new Order(requestDto, store, user, payment));

        AtomicLong totalPrice = new AtomicLong(0L);

        List<OrderProduct> orderProducts = requestDto.getProductIdCountMap().entrySet().stream()
            .map(entry -> {
                UUID productId = entry.getKey();
                Long count = entry.getValue();
                // Product 조회
                // 지금은 단순히 productId 만 가지고 확인하지만 추후 상품 품절을 고려
                // 해당 상품이 해당 상점의 것인지 확인하는 로직은 불필요하다고 판다.ㄴ
                Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("product_id 에 해당하는 상품이 존재하지 않습니다."));

                Long productTotalPrice = product.getPrice() * count;
                totalPrice.addAndGet(productTotalPrice);

                OrderProduct orderProduct = OrderProduct.builder()
                    .product(product)
                    .order(order)
                    .count(count)
                    .build();

                order.getOrderProductList().add(orderProduct);
                product.getOrderProductList().add(orderProduct);
                return orderProduct;
            })
            .collect(Collectors.toList());

        orderProductRepository.saveAll(orderProducts);
        order.setTotalPrice(totalPrice.get());
        payment.setPaymentPrice(totalPrice.get());

        return new UserOrderResponseDto(order.getId(), "order_id 에 해당하는 주문이 완료되었습니다.");
    }

    public Page<UserOrderListResponseDto> getOrders(PageRequest pageable, User user) {
        Page<Order> ordersPage = orderRepository.findByUserId(user.getId(), pageable);

        List<UserOrderListResponseDto> orderDtoList = ordersPage.getContent().stream()
            .map(UserOrderListResponseDto::new)
            .collect(Collectors.toList());

        return new PageImpl<>(orderDtoList, pageable, ordersPage.getTotalElements());
    }

    @Transactional
    public UserOrderResponseDto deleteOrderAndPayment(String orderId, User user) {
        Order order = orderRepository.findById(UUID.fromString(orderId)).orElseThrow(() ->
            new IllegalArgumentException("order_id 에 해당하는 주문이 없습니다."));
        orderRepository.delete(order);

        return new UserOrderResponseDto(order.getId(), "주문과 결제가 성공적으로 취소 되었습니다.");
    }
}
