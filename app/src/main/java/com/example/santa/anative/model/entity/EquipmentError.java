package com.example.santa.anative.model.entity;

/**
 * Created by santa on 11.03.17.
 */

public class EquipmentError {

    private int id;
    private long date;
    private String name;
    private String info;

    public EquipmentError(int id, long date, String name, String info) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
