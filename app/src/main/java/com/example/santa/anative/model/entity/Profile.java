package com.example.santa.anative.model.entity;

import java.util.Formatter;
import java.util.Locale;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.RealmModule;

/**
 * Created by santa on 09.03.17.
 */

@RealmClass
public class Profile implements RealmModel {

    @PrimaryKey
    private String deviceId;
    private String name;
    private String surname;
    private String patronymic;
    private String company;
    private String email;
    private String phone;
    private String serverName;

    private int clientId;
    private int keyLength;

    private byte[] sessionId;
    private byte[] keyFirst;
    private byte[] keySecond;
    private byte[] randomKey;
    private byte[] sessionTime;
    private byte[] timestamp;
    private byte[] token;

    private int lastSessionTime;
    private int registrationTime;


    private RealmList<Equipment> equipments;

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getLastSessionTime() {
        return lastSessionTime;
    }

    public void setLastSessionTime(int lastSessionTime) {
        this.lastSessionTime = lastSessionTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public byte[] getRandomKey() {
        return randomKey;
    }

    public void setRandomKey(byte[] randomKey) {
        this.randomKey = randomKey;
    }

    public byte[] getSessionId() {
        return sessionId;
    }

    public void setSessionId(byte[] sessionId) {
        this.sessionId = sessionId;
    }

    public byte[] getKeyFirst() {
        return keyFirst;
    }

    public void setKeyFirst(byte[] keyFirst) {
        this.keyFirst = keyFirst;
    }

    public byte[] getKeySecond() {
        return keySecond;
    }

    public void setKeySecond(byte[] keySecond) {
        this.keySecond = keySecond;
    }

    public int getKeyLength() {
        return keyLength;
    }

    public void setKeyLength(int keyLength) {
        this.keyLength = keyLength;
    }

    public int getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(int registrationTime) {
        this.registrationTime = registrationTime;
    }

    public RealmList<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(RealmList<Equipment> equipments) {
        this.equipments = equipments;
    }

    public byte[] getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(byte[] sessionTime) {
        this.sessionTime = sessionTime;
    }

    public byte[] getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(byte[] timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getToken() {
        return token;
    }

    public void setToken(byte[] token) {
        this.token = token;
    }

    public String generateId() {
        Formatter id = new Formatter();
        id.format(Locale.ENGLISH, "%s#%s", email, deviceId);
        return id.toString();
    }
}
