package com.sparta.business.domain.master.repository;

import com.sparta.business.entity.Notice;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, UUID> {

}
