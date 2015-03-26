package com.qubaopen.survey.entity.booking;

/**
 * Created by mars on 15/3/26.
 */
public enum ResolveType {
    /**
     * 无需操作
     */
    None("无需操作"),
    /**
     * 医师未响应
     */
    DoctorNoResponse("医师未响应"),
    /**
     * 医师拒绝
     */
    DoctorRefusal("医师拒绝"),
    /**
     * 用户未响应
     */
    UserNoResponse("用户未响应"),
    /**
     * 加急确认
     */
    QuickConfirm("加急确认"),
    /**
     * 订单改约
     */
    BookingChange("订单改约"),
    /**
     * 订单回访
     */
    BookingReview("订单回访");

    private String status;

    ResolveType(String status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return status;
    }
}
