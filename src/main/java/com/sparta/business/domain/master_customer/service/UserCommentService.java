package com.sparta.business.domain.master_customer.service;

import com.sparta.business.domain.master.repository.CommentRepository;
import com.sparta.business.domain.master_customer.dto.UserCommentResponseDto;
import com.sparta.business.entity.Comment;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserCommentService {

    private final CommentRepository commentRepository;

    public UserCommentResponseDto getComment(UUID complaint_id) {
        Comment comment = commentRepository.findByComplaintId(complaint_id);
        return new UserCommentResponseDto(comment.getId(),
                                        comment.getTitle(),
                                        comment.getContent());
    }
}
