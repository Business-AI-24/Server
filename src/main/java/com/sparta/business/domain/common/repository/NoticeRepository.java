package com.sparta.business.domain.common.repository;

import com.sparta.business.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NoticeRepository extends JpaRepository<Notice, UUID> {
}
