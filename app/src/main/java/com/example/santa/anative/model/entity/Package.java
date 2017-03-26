package com.example.santa.anative.model.entity;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by santa on 25.03.17.
 */

@RealmClass
public class Package implements RealmModel {

    @PrimaryKey
    private int id;
    private int group;
    private int addressFrom;
    private int subaddressFrom;
    private int addressTo;
    private int subaddressTo;
    private int timestamp;
    private int command;
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getAddressFrom() {
        return addressFrom;
    }

    public void setAddressFrom(int addressFrom) {
        this.addressFrom = addressFrom;
    }

    public int getSubaddressFrom() {
        return subaddressFrom;
    }

    public void setSubaddressFrom(int subaddressFrom) {
        this.subaddressFrom = subaddressFrom;
    }

    public int getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(int addressTo) {
        this.addressTo = addressTo;
    }

    public int getSubaddressTo() {
        return subaddressTo;
    }

    public void setSubaddressTo(int subaddressTo) {
        this.subaddressTo = subaddressTo;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
