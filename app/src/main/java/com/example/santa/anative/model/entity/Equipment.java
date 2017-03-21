package com.example.santa.anative.model.entity;

import io.realm.RealmModel;
import io.realm.annotations.Ignore;
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
    public static final int DEHUMODOFIER = 2;


    private int id;
    private String name;
    private int type;
    private String temperature;
    private String humidity;

    private boolean isEnable;

   // private RealmList<Setting> equipmentSettings;


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
}
