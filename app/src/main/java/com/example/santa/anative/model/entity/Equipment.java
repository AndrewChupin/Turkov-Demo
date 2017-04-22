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
    public static final int FAN = 0;
    @Ignore
    public static final int CONVECTOR = 1;
    @Ignore
    public static final int DESICCANT = 2;

    @Ignore
    public static final int DEFAULT = 0;
    @Ignore
    public static final int WARM = 1;
    @Ignore
    public static final int COLD = 2;
    @Ignore
    public static final int VENTILATION = 3;

    @Ignore
    public static final byte AUTO = 0;
    @Ignore
    public static final byte FIRST = 1;
    @Ignore
    public static final byte SECOND = 2;
    @Ignore
    public static final byte THIRD = 3;

    @PrimaryKey
    private int id;
    private String title;
    private String name;
    private int type;

    private String pin;
    private String serialNumber;
    private String location;
    private String address;
    private String gps;

    private int country;
    private int temperature;
    private int humidity;
    private int filter;
    private int temperatureStreet;
    private int pressure;
    private int carbon;
    private int speed;

    private boolean isEnable;
    private boolean isSettingsEnable;
    private int status;
    private int state;

    private RealmList<Setting> settings;
    private RealmList<Error> errors;

    public boolean isSettingsEnable() {
        return isSettingsEnable;
    }

    public void setSettingsEnable(boolean settingsEnable) {
        isSettingsEnable = settingsEnable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
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

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }

    public int getTemperatureStreet() {
        return temperatureStreet;
    }

    public void setTemperatureStreet(int temperatureStreet) {
        this.temperatureStreet = temperatureStreet;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getCarbon() {
        return carbon;
    }

    public void setCarbon(int carbon) {
        this.carbon = carbon;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
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
