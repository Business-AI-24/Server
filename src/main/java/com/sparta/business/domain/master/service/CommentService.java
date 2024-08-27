package com.sparta.business.domain.master.service;

import com.sparta.business.domain.master.dto.CommentRequestDto;
import com.sparta.business.domain.master.dto.CommentResponseDto;
import com.sparta.business.domain.master.dto.NoticeResponseDto;
import com.sparta.business.domain.master.repository.CommentRepository;
import com.sparta.business.domain.master.repository.ComplaintRepository;
import com.sparta.business.entity.Comment;
import com.sparta.business.entity.Complaint;
import com.sparta.business.entity.Notice;
import com.sparta.business.entity.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final ComplaintRepository complaintRepository;

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto, User user) {
        Complaint complaint = complaintRepository.findById(requestDto.getComplaint_id()).orElseThrow(() ->
            new IllegalArgumentException("complaint_id 에 해당하는 고객의 컴플레인이 없습니다."));
        Comment comment = commentRepository.save(new Comment(requestDto, complaint));
        return new CommentResponseDto(comment.getId(), "새로운 컴플레인에 대한 답글이 성공적으로 작성되었습니다.");
    }

    @Transactional
    public CommentResponseDto updateComment(String commentId, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(UUID.fromString(commentId)).orElseThrow(() ->
            new IllegalArgumentException("comment_id 에 해당하는 컴플레인 답글이 존재하지 않습니다."));
        comment.update(requestDto);
        return new CommentResponseDto(comment.getId(), "답글이 성공적으로 수정되었습니다.");
    }

    @Transactional
    public CommentResponseDto deleteComment(String commentId) {
        Comment comment = commentRepository.findById(UUID.fromString(commentId)).orElseThrow(() ->
            new IllegalArgumentException("comment_id 에 해당하는 컴플레인 답글이 존재하지 않습니다."));
        commentRepository.delete(comment);
        return new CommentResponseDto(comment.getId(), "답글이 성공적으로 삭제되었습니다.");
    }
}
