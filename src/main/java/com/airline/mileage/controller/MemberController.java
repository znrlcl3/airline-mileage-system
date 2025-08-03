package com.airline.mileage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.airline.mileage.dto.request.MemberRequestDto;
import com.airline.mileage.dto.request.MileageRequestDto;
import com.airline.mileage.dto.response.MemberResponseDto;
import com.airline.mileage.entity.MemberGrade;
import com.airline.mileage.service.MemberService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/api/members")
@CrossOrigin(origins = "*") 
public class MemberController {
    
    @Autowired
    private MemberService memberService;
    
    /**
     * API 테스트용 엔드포인트
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Member API 테스트");
    }
    
    /**
     * 회원 목록 페이지
     */
    @GetMapping("/view/list")
    public String memberListPage(Model model) {
        List<MemberResponseDto> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        return "member/list";
    }
    
    /**
     * 회원 등록 페이지
     */
    @GetMapping("/view/create")
    public String memberCreatePage(Model model) {
        model.addAttribute("member", new MemberRequestDto());
        return "member/create";
    }
    
    /**
     * 회원 등록 처리
     */
    @PostMapping("/view/create")
    public String memberCreateProcess(@Valid @ModelAttribute MemberRequestDto memberDto, 
                                     BindingResult bindingResult, Model model) {
        try {
            if (bindingResult.hasErrors()) {
                model.addAttribute("member", memberDto);
                model.addAttribute("error", "입력값을 확인해주세요.");
                return "member/create";
            }
            memberService.createMember(memberDto);
            return "redirect:/api/members/view/list";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("member", memberDto);
            model.addAttribute("error", e.getMessage());
            return "member/create";
        }
    }
    
    @GetMapping("/view/{id}")
    public String memberDetailPage(@PathVariable Long id, Model model) {
        try {
            MemberResponseDto member = memberService.getMember(id);
            model.addAttribute("member", member);
            return "member/detail";
        } catch (IllegalArgumentException e) {
            return "redirect:/api/members/view/list";
        }
    }
    
    /**
     * 회원 가입
     */
    @PostMapping
    public ResponseEntity<MemberResponseDto> createMember(@Valid @RequestBody MemberRequestDto requestDto) {
        try {
            MemberResponseDto responseDto = memberService.createMember(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 전체 회원 조회
     */
    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getAllMembers() {
        List<MemberResponseDto> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }
    
    /**
     * 회원 상세 조회 (ID)
     */
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable Long id) {
        try {
            MemberResponseDto member = memberService.getMember(id);
            return ResponseEntity.ok(member);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 회원 조회 (이메일)
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<MemberResponseDto> getMemberByEmail(@PathVariable String email) {
        try {
            MemberResponseDto member = memberService.getMemberByEmail(email);
            return ResponseEntity.ok(member);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 회원 정보 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponseDto> updateMember(
            @PathVariable Long id, 
            @Valid @RequestBody MemberRequestDto requestDto) {
        try {
            MemberResponseDto updatedMember = memberService.updateMember(id, requestDto);
            return ResponseEntity.ok(updatedMember);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 회원 삭제 (Soft Delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable Long id) {
        try {
            memberService.deleteMember(id);
            return ResponseEntity.ok("회원 ID " + id + " 삭제 완료 (Soft Delete)");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 마일리지 적립
     */
    @PostMapping("/{id}/mileage/add")
    public ResponseEntity<MemberResponseDto> addMileage(
            @PathVariable Long id, 
            @Valid @RequestBody MileageRequestDto requestDto) {
        try {
            MemberResponseDto updatedMember = memberService.addMileage(id, requestDto);
            return ResponseEntity.ok(updatedMember);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 마일리지 사용
     */
    @PostMapping("/{id}/mileage/use")
    public ResponseEntity<MemberResponseDto> useMileage(
            @PathVariable Long id, 
            @Valid @RequestBody MileageRequestDto requestDto) {
        try {
            MemberResponseDto updatedMember = memberService.useMileage(id, requestDto);
            return ResponseEntity.ok(updatedMember);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 등급별 회원 조회
     */
    @GetMapping("/grade/{grade}")
    public ResponseEntity<List<MemberResponseDto>> getMembersByGrade(@PathVariable MemberGrade grade) {
        List<MemberResponseDto> members = memberService.getMembersByGrade(grade);
        return ResponseEntity.ok(members);
    }
    
    /**
     * 마일리지 범위로 회원 검색
     */
    @GetMapping("/mileage")
    public ResponseEntity<List<MemberResponseDto>> getMembersByMileageRange(
            @RequestParam Integer minMileage, 
            @RequestParam Integer maxMileage) {
        List<MemberResponseDto> members = memberService.getMembersByMileageRange(minMileage, maxMileage);
        return ResponseEntity.ok(members);
    }
    
    /**
     * 상위 마일리지 회원 조회
     */
    @GetMapping("/top-mileage")
    public ResponseEntity<List<MemberResponseDto>> getTopMileageMembers() {
        List<MemberResponseDto> topMembers = memberService.getTopMileageMembers();
        return ResponseEntity.ok(topMembers);
    }
    
    /**
     * 회원 통계
     */
    @GetMapping("/statistics")
    public ResponseEntity<String> getMemberStatistics() {
        String statistics = memberService.getMemberStatistics();
        return ResponseEntity.ok(statistics);
    }
    
    /**
     * 이메일 중복 확인
     */
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = memberService.isEmailExists(email);
        return ResponseEntity.ok(exists);
    }
    
    /**
     * 테스트 회원 생성
     */
    @PostMapping("/create-test")
    public ResponseEntity<MemberResponseDto> createTestMember() {
        MemberRequestDto testMember = new MemberRequestDto(
            "test" + System.currentTimeMillis() + "@example.com",
            "password123",
            "테스트회원" + (int)(Math.random() * 100),
            "010-1234-5678"
        );
        
        MemberResponseDto responseDto = memberService.createMember(testMember);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}