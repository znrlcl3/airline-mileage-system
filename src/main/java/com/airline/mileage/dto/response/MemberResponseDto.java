package com.airline.mileage.dto.response;

import com.airline.mileage.entity.Member;
import com.airline.mileage.entity.MemberGrade;
import java.time.LocalDateTime;

public class MemberResponseDto {
    
    private Long id;
    private String email;
    private String name;
    private String phone;
    private MemberGrade grade;
    private String gradeDisplayName;
    private Integer totalMileage;
    private Integer availableMileage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public MemberResponseDto() {}
    
    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.phone = member.getPhone();
        this.grade = member.getGrade();
        this.gradeDisplayName = member.getGrade().getDisplayName();
        this.totalMileage = member.getTotalMileage();
        this.availableMileage = member.getAvailableMileage();
        this.createdAt = member.getCreatedAt();
        this.updatedAt = member.getUpdatedAt();
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public MemberGrade getGrade() { return grade; }
    public void setGrade(MemberGrade grade) { this.grade = grade; }
    
    public String getGradeDisplayName() { return gradeDisplayName; }
    public void setGradeDisplayName(String gradeDisplayName) { this.gradeDisplayName = gradeDisplayName; }
    
    public Integer getTotalMileage() { return totalMileage; }
    public void setTotalMileage(Integer totalMileage) { this.totalMileage = totalMileage; }
    
    public Integer getAvailableMileage() { return availableMileage; }
    public void setAvailableMileage(Integer availableMileage) { this.availableMileage = availableMileage; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @Override
    public String toString() {
        return "MemberResponseDto{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", grade=" + grade +
                ", totalMileage=" + totalMileage +
                ", availableMileage=" + availableMileage +
                '}';
    }
}