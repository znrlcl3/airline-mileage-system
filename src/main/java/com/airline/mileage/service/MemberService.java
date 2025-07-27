// MemberService.java
// 위치: src/main/java/com/airline/mileage/service/MemberService.java

package com.airline.mileage.service;

import com.airline.mileage.dto.request.MemberRequestDto;
import com.airline.mileage.dto.request.MileageRequestDto;
import com.airline.mileage.dto.response.MemberResponseDto;
import com.airline.mileage.entity.Member;
import com.airline.mileage.entity.MemberGrade;
import com.airline.mileage.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {
    
    @Autowired
    private MemberRepository memberRepository;
    
    /**
     * 회원 가입
     */
    public MemberResponseDto createMember(MemberRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + requestDto.getEmail());
        }
        
        Member member = new Member(
            requestDto.getEmail(),
            requestDto.getPassword(),
            requestDto.getName(),
            requestDto.getPhone()
        );
        
        Member savedMember = memberRepository.save(member);
        
        return new MemberResponseDto(savedMember);
    }
    
    /**
     * 회원 조회 (ID)
     */
    @Transactional(readOnly = true)
    public MemberResponseDto getMember(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + id));
        return new MemberResponseDto(member);
    }
    
    /**
     * 회원 조회 (이메일)
     */
    @Transactional(readOnly = true)
    public MemberResponseDto getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + email));
        return new MemberResponseDto(member);
    }
    
    /**
     * 전체 회원 조회 (활성 회원만)
     */
    @Transactional(readOnly = true)
    public List<MemberResponseDto> getAllMembers() {
        return memberRepository.findAll()
            .stream()
            .map(MemberResponseDto::new)
            .collect(Collectors.toList());
    }
    
    /**
     * 회원 정보 수정
     */
    public MemberResponseDto updateMember(Long id, MemberRequestDto requestDto) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + id));
        
        if (!member.getEmail().equals(requestDto.getEmail()) && 
            memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + requestDto.getEmail());
        }
        
        member.setEmail(requestDto.getEmail());
        member.setName(requestDto.getName());
        member.setPhone(requestDto.getPhone());
        
        Member updatedMember = memberRepository.save(member);
        return new MemberResponseDto(updatedMember);
    }
    
    /**
     * 회원 삭제 (Soft Delete)
     */
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + id));
        
        memberRepository.delete(member);
    }
    
    /**
     * 마일리지 적립
     */
    public MemberResponseDto addMileage(Long memberId, MileageRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + memberId));
        
        // 마일리지 적립
        member.addMileage(requestDto.getMileage());
        
        Member updatedMember = memberRepository.save(member);
        return new MemberResponseDto(updatedMember);
    }
    
    /**
     * 마일리지 사용
     */
    public MemberResponseDto useMileage(Long memberId, MileageRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + memberId));
        
        // 마일리지 사용
        boolean success = member.useMileage(requestDto.getMileage());
        if (!success) {
            throw new IllegalArgumentException(
                String.format("사용 가능한 마일리지가 부족합니다. 요청: %d, 보유: %d", 
                    requestDto.getMileage(), member.getAvailableMileage())
            );
        }
        
        Member updatedMember = memberRepository.save(member);
        return new MemberResponseDto(updatedMember);
    }
    
    /**
     * 등급별 회원 조회
     */
    @Transactional(readOnly = true)
    public List<MemberResponseDto> getMembersByGrade(MemberGrade grade) {
        return memberRepository.findByGrade(grade)
            .stream()
            .map(MemberResponseDto::new)
            .collect(Collectors.toList());
    }
    
    /**
     * 마일리지 범위로 회원 검색
     */
    @Transactional(readOnly = true)
    public List<MemberResponseDto> getMembersByMileageRange(Integer minMileage, Integer maxMileage) {
        return memberRepository.findActiveByMileageRange(minMileage, maxMileage)
            .stream()
            .map(MemberResponseDto::new)
            .collect(Collectors.toList());
    }
    
    /**
     * 상위 마일리지 회원 조회
     */
    @Transactional(readOnly = true)
    public List<MemberResponseDto> getTopMileageMembers() {
        return memberRepository.findTop10ActiveByOrderByTotalMileageDesc()
            .stream()
            .limit(10)
            .map(MemberResponseDto::new)
            .collect(Collectors.toList());
    }
    
    /**
     * 회원 통계 조회
     */
    @Transactional(readOnly = true)
    public String getMemberStatistics() {
        long totalActive = memberRepository.countActiveMembers();
        long totalDeleted = memberRepository.countDeletedMembers();
        
        return String.format("총 활성 회원: %d명, 탈퇴 회원: %d명, 전체: %d명", 
            totalActive, totalDeleted, totalActive + totalDeleted);
    }
    
    /**
     * 이메일 중복 확인 (회원가입 전 체크용)
     */
    @Transactional(readOnly = true)
    public boolean isEmailExists(String email) {
        return memberRepository.existsByEmail(email);
    }
}