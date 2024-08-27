package com.sparta.business.domain.master.service;

import com.sparta.business.domain.common.dto.OrderResponseDto;
import com.sparta.business.domain.common.repository.UserRepository;
import com.sparta.business.domain.master.dto.RegionEditRequestDto;
import com.sparta.business.domain.master.dto.RegionResponseDto;
import com.sparta.business.domain.master.repository.RegionRepository;
import com.sparta.business.entity.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private UserRepository userRepository;

    //카테고리 저장
    @Transactional

    public ResponseEntity<String> addRegion(String regionName, User user) {

        //권한 확인
        if (!UserRoleEnum.MASTER.equals(user.getRole())) {
            System.out.println("RegionService->" + user.getRole());
            return ResponseEntity.status(403).body("지역 등록 권한이 없습니다.");
        }

        //중복 확인
        Optional<Region> existingRegion = regionRepository.findByRegionName(regionName);
        if (existingRegion.isPresent()) {
            return ResponseEntity.badRequest().body("이미 존재하는 지역 이름 입니다.");
        }

        //카테고리 저장
        Region region = new Region();
        region.setRegionName(regionName);
        regionRepository.save(region);
        return ResponseEntity.ok("지역이 성공적으로 등록되었습니다. ");


        }

    //카테고리 수정
    @Transactional
    public ResponseEntity<String> updateRegion(UUID regionId, RegionEditRequestDto regionEditRequestDto, String username) {

        //사용자 정보를 가져옴
        User user= userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("로그인 후 수정하세요."));

        // 권한 확인
        if (!UserRoleEnum.MASTER.equals(user.getRole())) {
            return ResponseEntity.status(403).body("권한이 없습니다.");
        }

        // 카테고리 수정 로직
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException("지역 이름을 찾을 수 없습니다."));

        //카테고리 수정
        region.setRegionName(regionEditRequestDto.getRegionName());
        regionRepository.save(region);


        return ResponseEntity.ok("지역이름이 성공적으로 수정되었습니다.");

    }
    @Transactional
    public ResponseEntity<String> deleteRegion(UUID regionId, String username) {

        //사용자 정보 가져옴
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("로그인 후 삭제하세요"));

        //권한 확인
        if (!UserRoleEnum.MASTER.equals(user.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }

        // 카테고리 삭제 로직
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException("지역이름을 찾을 수 없습니다."));

        // 카테고리 삭제 로직
        regionRepository.delete(region);
        //
        return  ResponseEntity.ok("지역이 삭제처리되었습니다.");
    }

    @Transactional(readOnly = true)
    public List<RegionResponseDto> getRegion(String username) {
        //사용자 정보 가져옴
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("로그인 후 삭제하세요"));

        //권한 확인
        if (!UserRoleEnum.MASTER.equals(user.getRole())) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        List<Region> regionList = regionRepository.findByDeletedAtIsNull();
        return regionList.stream()
                .map(region -> new RegionResponseDto(region.getId(),region.getRegionName()))
                .collect(Collectors.toList());
    }
}



