package com.airline.mileage.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MileageRequestDto {
    
    @NotNull(message = "마일리지는 필수입니다")
    @Min(value = 1, message = "마일리지는 1 이상이어야 합니다")
    private Integer mileage;
    
    private String reason; // 적립/사용 사유
    
    public MileageRequestDto() {}
    
    public MileageRequestDto(Integer mileage, String reason) {
        this.mileage = mileage;
        this.reason = reason;
    }
    
    public Integer getMileage() { return mileage; }
    public void setMileage(Integer mileage) { this.mileage = mileage; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    @Override
    public String toString() {
        return "MileageRequestDto{" +
                "mileage=" + mileage +
                ", reason='" + reason + '\'' +
                '}';
    }
}