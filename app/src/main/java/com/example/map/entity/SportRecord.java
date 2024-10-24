package com.example.map.entity;

import java.time.LocalDate;
import java.util.Date;

public class SportRecord {
    private String imageUrl;
    private float distanceSum,averageSpeed,elapsedTime,altitude;
    private LocalDate timeDate;
    private Integer status;


    public SportRecord() {
    }

    public SportRecord(String imageUrl, float distanceSum, float averageSpeed, float elapsedTime, float altitude, LocalDate timeDate, Integer status) {
        this.imageUrl = imageUrl;
        this.distanceSum = distanceSum;
        this.averageSpeed = averageSpeed;
        this.elapsedTime = elapsedTime;
        this.altitude = altitude;
        this.timeDate = timeDate;
        this.status = status;
    }

    /**
     * 获取
     * @return imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 设置
     * @param imageUrl
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 获取
     * @return distanceSum
     */
    public float getDistanceSum() {
        return distanceSum;
    }

    /**
     * 设置
     * @param distanceSum
     */
    public void setDistanceSum(float distanceSum) {
        this.distanceSum = distanceSum;
    }

    /**
     * 获取
     * @return averageSpeed
     */
    public float getAverageSpeed() {
        return averageSpeed;
    }

    /**
     * 设置
     * @param averageSpeed
     */
    public void setAverageSpeed(float averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    /**
     * 获取
     * @return elapsedTime
     */
    public float getElapsedTime() {
        return elapsedTime;
    }

    /**
     * 设置
     * @param elapsedTime
     */
    public void setElapsedTime(float elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    /**
     * 获取
     * @return altitude
     */
    public float getAltitude() {
        return altitude;
    }

    /**
     * 设置
     * @param altitude
     */
    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    /**
     * 获取
     * @return timeDate
     */
    public LocalDate getTimeDate() {
        return timeDate;
    }

    /**
     * 设置
     * @param timeDate
     */
    public void setTimeDate(LocalDate timeDate) {
        this.timeDate = timeDate;
    }

    /**
     * 获取
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String toString() {
        return "SportRecord{imageUrl = " + imageUrl + ", distanceSum = " + distanceSum + ", averageSpeed = " + averageSpeed + ", elapsedTime = " + elapsedTime + ", altitude = " + altitude + ", timeDate = " + timeDate + ", status = " + status + "}";
    }
}
