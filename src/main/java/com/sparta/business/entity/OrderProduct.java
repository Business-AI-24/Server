package com.sparta.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder
@Table(name = "p_order_product")
@SQLDelete(sql = "UPDATE p_order_product SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class OrderProduct extends Auditing{

    @Id
    @GeneratedValue
    @Column(name = "order_product_id")
    private UUID id;

    @Column(name = "count", columnDefinition = "bigint default 1")
    private Long count = 1L;

    @Column(name = "is_public", columnDefinition = "boolean default true")
    private Boolean is_public = true;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}
