package com.airline.mileage.repository;

import com.airline.mileage.entity.Member;
import com.airline.mileage.entity.MemberGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Member> findByGrade(MemberGrade grade);
    List<Member> findByNameContaining(String name);
    
    @Query("SELECT m FROM Member m WHERE m.email = :email")
    Optional<Member> findByEmailIncludingDeleted(@Param("email") String email);
    
    @Query("SELECT m FROM Member m")
    List<Member> findAllIncludingDeleted();
    
    // 삭제된 회원만 조회
    @Query("SELECT m FROM Member m WHERE m.deleted = true")
    List<Member> findDeletedMembers();
    
    // 활성 회원 수 조회
    @Query("SELECT COUNT(m) FROM Member m WHERE m.deleted = false")
    Long countActiveMembers();
    
    // 탈퇴 회원 수 조회  
    @Query("SELECT COUNT(m) FROM Member m WHERE m.deleted = true")
    Long countDeletedMembers();
    
    // 등급별 활성 회원 조회
    @Query("SELECT m FROM Member m WHERE m.grade = :grade AND m.deleted = false")
    List<Member> findActiveByGrade(@Param("grade") MemberGrade grade);
    
    // 마일리지 범위로 활성 회원 검색
    @Query("SELECT m FROM Member m WHERE m.totalMileage BETWEEN :minMileage AND :maxMileage AND m.deleted = false")
    List<Member> findActiveByMileageRange(@Param("minMileage") Integer minMileage, 
                                         @Param("maxMileage") Integer maxMileage);
    
    // 상위 마일리지 활성 회원 조회
    @Query("SELECT m FROM Member m WHERE m.deleted = false ORDER BY m.totalMileage DESC")
    List<Member> findTop10ActiveByOrderByTotalMileageDesc();
}