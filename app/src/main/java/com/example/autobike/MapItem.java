package com.example.autobike;

import java.util.Date;

public class MapItem {
    private String imageUrl;
    private float distanceSum,averageSpeed,elapsedTime,altitude;
    private Date timeDate;


    public MapItem() {
    }

    public MapItem(String imageUrl, float distanceSum, float averageSpeed, float elapsedTime, float altitude, Date timeDate) {
        this.imageUrl = imageUrl;
        this.distanceSum = distanceSum;
        this.averageSpeed = averageSpeed;
        this.elapsedTime = elapsedTime;
        this.altitude = altitude;
        this.timeDate = timeDate;
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
    public Date getTimeDate() {
        return timeDate;
    }

    /**
     * 设置
     * @param timeDate
     */
    public void setTimeDate(Date timeDate) {
        this.timeDate = timeDate;
    }

    public String toString() {
        return "MapItem{imageUrl = " + imageUrl + ", distanceSum = " + distanceSum + ", averageSpeed = " + averageSpeed + ", elapsedTime = " + elapsedTime + ", altitude = " + altitude + ", timeDate = " + timeDate + "}";
    }
}
