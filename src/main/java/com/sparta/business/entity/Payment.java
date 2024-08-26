package com.sparta.business.entity;

import jakarta.persistence.*;

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
@Table(name = "p_payment")
@SQLDelete(sql = "UPDATE p_payment SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class Payment extends Auditing{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "payment_id",columnDefinition = "UUID")
    private UUID id;

    @Column(name = "payment_price", nullable = false)
    private Long paymentPrice;

    @Column(name = "pg_name", nullable = false)
    private String pg_name;

    @Column(name = "transaction_id", nullable = false)
    private String transactionID;

    @Column(name = "payment_method", columnDefinition = "boolean default true")
    private Boolean paymentMethod = true;

    @Column(name = "is_public", columnDefinition = "boolean default true")
    private Boolean is_public = true;

}
