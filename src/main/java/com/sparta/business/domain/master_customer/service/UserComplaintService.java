package com.sparta.business.domain.master_customer.service;

import com.sparta.business.domain.master.repository.ComplaintRepository;
import com.sparta.business.domain.master_customer.dto.UserComplaintResponseDto;
import com.sparta.business.domain.master_customer.dto.UserComplaintsRequestDto;
import com.sparta.business.entity.Complaint;
import com.sparta.business.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserComplaintService {

    private final ComplaintRepository complaintRepository;

    @Transactional
    public UserComplaintResponseDto createComplaint(UserComplaintsRequestDto requestDto, User user) {

        Complaint complaint = complaintRepository.save(new Complaint(requestDto, user));
        return new UserComplaintResponseDto(complaint.getId(), "컴플레인이 성공적으로 등록되었습니다.");
    }
}
