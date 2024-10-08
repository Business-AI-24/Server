package com.sparta.business.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.business.domain.master_customer.dto.UserReviewRequestDto;
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
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder
@Table(name = "p_review")
@SQLDelete(sql = "UPDATE p_review SET deleted_at = CURRENT_TIMESTAMP WHERE review_id = ?")
public class Review extends Auditing{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "review_id")
    private UUID id;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "is_public", columnDefinition = "boolean default true")
    private Boolean is_public = true;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public Review(UserReviewRequestDto requestDto, Store store, User user) {
        this.rating = requestDto.getRating();
        this.comment = requestDto.getComment();
        this.store = store;
        this.user = user;
        store.getReviewList().add(this);
//        user.getReviewList().add(this);
    }

    public void update(UserReviewRequestDto requestDto) {
        this.rating = requestDto.getRating();
        this.comment = requestDto.getComment();
    }
}
