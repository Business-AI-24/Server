package com.sparta.business.domain.master.controller;

import com.sparta.business.domain.master.dto.RegionEditRequestDto;
import com.sparta.business.domain.master.dto.RegionRequestDto;
import com.sparta.business.domain.master.dto.RegionResponseDto;
import com.sparta.business.domain.master.service.RegionService;
import com.sparta.business.filter.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class RegionController {

    private final RegionService regionService;


    //카테고리 등록
    @PostMapping("/region")
    public ResponseEntity<String> addRegion(
            @RequestBody RegionRequestDto regionRequestDto ,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String regionName = regionRequestDto.getRegionName();
        System.out.println("categoryTypes: " + regionName);
        System.out.println("userDetails: " + userDetails);
        return regionService.addRegion(regionName,userDetails.getUser());

    }

    //카테고리 수정
    @PutMapping("/region/{region_id}")
    public ResponseEntity<String> updateRegion(
            @PathVariable UUID region_id,
            @RequestBody RegionEditRequestDto regionEditRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
           {
               //로그인한 사용자의 ID를 얻는 방법
             String username = userDetails.getUsername();
                System.out.println("regionEditRequestDto: " + regionEditRequestDto);
                System.out.println("user: " + username);
                System.out.println("region_id: " + region_id);
               return regionService.updateRegion(region_id, regionEditRequestDto, username);


    }

    //카테고리 삭제
    @DeleteMapping("/region/{region_id}")
    public ResponseEntity<String> deleteRegion(
            @PathVariable UUID region_id,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {

        //로그인한 사용자의 ID 얻는 방법
        String username = userDetails.getUsername();

        return regionService.deleteRegion(region_id,username);

    }

    @GetMapping("/region")
    public ResponseEntity<List<RegionResponseDto>> getRegion(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return new ResponseEntity<>(regionService.getRegion(userDetails.getUsername()), HttpStatus.OK);
    }



    }




