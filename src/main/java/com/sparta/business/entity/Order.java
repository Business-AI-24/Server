package com.sparta.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.business.domain.master_customer.dto.UserOrderRequestDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder
@Table(name = "p_order")
@SQLDelete(sql = "UPDATE p_order SET deleted_at = CURRENT_TIMESTAMP WHERE order_id = ?")
public class Order extends Auditing{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "order_id")
    private UUID id;

    @Column(name = "total_price", nullable = false)
    private Long totalPrice;

    @Column(name = "request", nullable = false)
    private String request;

    @Column(name = "is_public", columnDefinition = "boolean default true")
    private Boolean is_public = true;

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status = true;

    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProductList = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public Order(UserOrderRequestDto requestDto, Store store, User user, Payment payment) {
        this.totalPrice = 0L;
        this.request = requestDto.getRequest();
        this.store = store;
        this.user = user;
        this.payment = payment;
//        user.getOrderList().add(this);
        store.getOrderList().add(this);
    }

}
