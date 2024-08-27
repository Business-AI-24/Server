package com.sparta.business.domain.master.service;

import com.sparta.business.domain.master.dto.NoticeRequestDto;
import com.sparta.business.domain.master.dto.NoticeResponseDto;
import com.sparta.business.domain.master.repository.NoticeRepository;
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
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public NoticeResponseDto createNotice(NoticeRequestDto requestDto, User user) {
        Notice notice = noticeRepository.save(new Notice(requestDto, user));
        return new NoticeResponseDto(notice.getId(), "새로운 공지사항이 성공적으로 작성되었습니다.");
    }

    @Transactional
    public NoticeResponseDto updateNotice(String noticeId, NoticeRequestDto requestDto) {
        Notice notice = noticeRepository.findById(UUID.fromString(noticeId)).orElseThrow(() ->
            new IllegalArgumentException("notice_id 에 해당하는 공지사항이 존재하지 않습니다."));
        notice.update(requestDto);
        return new NoticeResponseDto(notice.getId(), "공지상항이 성공적으로 수정되었습니다.");
    }

    @Transactional
    public NoticeResponseDto deleteNotice(String noticeId) {
        Notice notice = noticeRepository.findById(UUID.fromString(noticeId)).orElseThrow(() ->
            new IllegalArgumentException("notice_id 에 해당하는 공지사항이 존재하지 않습니다."));
        noticeRepository.delete(notice);
        return new NoticeResponseDto(notice.getId(), "공지사항이 성공적으로 삭제되었습니다.");
    }
}
