package com.example.santa.anative.model.entity;

/**
 * Created by santa on 05.03.17.
 */

public class Equipment {

    public static final String VENTILATION = "Ventilation";
    public static final String CONVECTOR = "Convector";
    public static final String DEHUMODOFIER = "Dehumidifier";


    private int id;

    private String name;
    private String type;
    private String temperature;
    private String humidity;

    private boolean isEnable;

   // private RealmList<Setting> equipmentSettings;



    public Equipment(String name, String type, String temperature, String humidity, boolean isEnable) {
        this.name = name;
        this.type = type;
        this.temperature = temperature;
        this.humidity = humidity;
        this.isEnable = isEnable;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

   /* public RealmList<Setting> getEquipmentSettings() {
        return equipmentSettings;
    }

    public void setEquipmentSettings(RealmList<Setting> equipmentSettings) {
        this.equipmentSettings = equipmentSettings;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
