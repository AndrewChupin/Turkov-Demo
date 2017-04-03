package com.example.santa.anative.model.entity;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by santa on 05.03.17.
 */

@RealmClass
public class Equipment implements RealmModel {

    @Ignore
    public static final int VENTILATION = 0;
    @Ignore
    public static final int CONVECTOR = 1;
    @Ignore
    public static final int DESICCANT = 2;

    @PrimaryKey
    private int id;
    private String title;
    private String name;
    private int type;
    private int pin;

    private String serialNumber;
    private String country;
    private String location;
    private String address;
    private String gps;

    private String temperature;
    private String humidity;

    private boolean isEnable;
    private int status;
    private int state;

    private RealmList<Setting> settings;
    private RealmList<Error> errors;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public RealmList<Setting> getSettings() {
        return settings;
    }

    public void setSettings(RealmList<Setting> settings) {
        this.settings = settings;
    }

    public RealmList<Error> getErrors() {
        return errors;
    }

    public void setErrors(RealmList<Error> errors) {
        this.errors = errors;
    }

}
