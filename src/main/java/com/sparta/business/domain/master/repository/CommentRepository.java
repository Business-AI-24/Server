package com.sparta.business.domain.master.repository;

import com.sparta.business.entity.Comment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    Comment findByComplaintId(UUID complaintId);
}
