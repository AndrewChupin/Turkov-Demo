package com.example.santa.anative.model.entity;

/**
 * Created by santa on 09.03.17.
 */

public class Setting{

    public static final byte MONDAY = 1;
    public static final byte TUESDAY = 2;
    public static final byte WEDNESDAY = 3;
    public static final byte THURSDAY = 4;
    public static final byte SARDAY = 5;
    public static final byte SATURNDAY = 6;
    public static final byte SUNNDAY = 7;

    public static final byte AUTO = 0;
    public static final byte FIRST = 1;
    public static final byte SECOND = 2;
    public static final byte THIRD = 3;


    private int id;

    private int hour;
    private int minutes;
    private int day;

    private boolean isEnable;
    private boolean isActive;

    private int speed;
    private int temperature;



    public Setting(int hour, int minutes, int day, boolean isEnable, boolean isActive, int speed, int temperature) {
        this.hour = hour;
        this.minutes = minutes;
        this.day = day;
        this.isEnable = isEnable;
        this.isActive = isActive;
        this.speed = speed;
        this.temperature = temperature;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
