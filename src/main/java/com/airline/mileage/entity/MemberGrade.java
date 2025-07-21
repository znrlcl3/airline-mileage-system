package com.airline.mileage.entity;

public enum MemberGrade {
    BASIC("일반", 0, 1.0),
    SILVER("실버", 20000, 1.2),
    GOLD("골드", 50000, 1.5),
    DIAMOND("다이아몬드", 100000, 2.0);
    
    private final String displayName;
    private final int requiredMileage;
    private final double mileageRate;
    
    MemberGrade(String displayName, int requiredMileage, double mileageRate) {
        this.displayName = displayName;
        this.requiredMileage = requiredMileage;
        this.mileageRate = mileageRate;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getRequiredMileage() {
        return requiredMileage;
    }
    
    public double getMileageRate() {
        return mileageRate;
    }
    
    public static MemberGrade getGradeByMileage(int totalMileage) {
        if (totalMileage >= DIAMOND.requiredMileage) {
            return DIAMOND;
        } else if (totalMileage >= GOLD.requiredMileage) {
            return GOLD;
        } else if (totalMileage >= SILVER.requiredMileage) {
            return SILVER;
        } else {
            return BASIC;
        }
    }
}