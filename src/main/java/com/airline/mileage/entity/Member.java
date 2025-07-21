package com.airline.mileage.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "members")
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false, length = 50)
    private String name;
    
    @Column(length = 20)
    private String phone;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberGrade grade = MemberGrade.BASIC;
    
    @Column(nullable = false)
    private Integer totalMileage = 0;
    
    @Column(nullable = false)
    private Integer availableMileage = 0;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    // 기본 생성자
    public Member() {}
    
    // 생성자
    public Member(String email, String password, String name, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.grade = MemberGrade.BASIC;
        this.totalMileage = 0;
        this.availableMileage = 0;
    }
    
    // Getter, Setter
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public MemberGrade getGrade() {
        return grade;
    }
    
    public void setGrade(MemberGrade grade) {
        this.grade = grade;
    }
    
    public Integer getTotalMileage() {
        return totalMileage;
    }
    
    public void setTotalMileage(Integer totalMileage) {
        this.totalMileage = totalMileage;
    }
    
    public Integer getAvailableMileage() {
        return availableMileage;
    }
    
    public void setAvailableMileage(Integer availableMileage) {
        this.availableMileage = availableMileage;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // 비즈니스 메서드
    public void addMileage(Integer mileage) {
        this.totalMileage += mileage;
        this.availableMileage += mileage;
        updateGrade();
    }
    
    public boolean useMileage(Integer mileage) {
        if (this.availableMileage >= mileage) {
            this.availableMileage -= mileage;
            return true;
        }
        return false;
    }
    
    private void updateGrade() {
        if (totalMileage >= 100000) {
            this.grade = MemberGrade.DIAMOND;
        } else if (totalMileage >= 50000) {
            this.grade = MemberGrade.GOLD;
        } else if (totalMileage >= 20000) {
            this.grade = MemberGrade.SILVER;
        } else {
            this.grade = MemberGrade.BASIC;
        }
    }
}