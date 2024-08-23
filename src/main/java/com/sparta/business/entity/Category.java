package com.sparta.business.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@Table(name = "p_category")
@SQLDelete(sql = "UPDATE p_category SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class Category extends Auditing{

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private UUID id;

    @Column(name = "type")
    private String type;

}
