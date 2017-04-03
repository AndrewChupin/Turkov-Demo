package com.example.santa.anative.model.entity;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

import io.realm.ProfileRealmProxy;
import io.realm.RealmModel;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by santa on 25.03.17.
 */

@RealmClass
public class Package implements RealmModel {

    @Ignore
    public static final int MIN_SIZE = 18;

    // Builder Status
    @Ignore
    public static final int REQUEST = 0;
    @Ignore
    public static final int RESPONSE = 1;
    @Ignore
    public static final int REJECT = 2;
    @Ignore
    public static final int UNDEFINE = 3;

    // Command
    @Ignore
    public static final int READ = 0;
    @Ignore
    public static final int WRITE = 1;
    @Ignore
    public static final int READ_BIG = 2;
    @Ignore
    public static final int WRITE_BIG = 3;

    // Data Type
    @Ignore
    public static final int INTEGER = 0;
    @Ignore
    public static final int SHORT = 1;
    @Ignore
    public static final int CHAR = 2;
    @Ignore
    public static final int DOUBLE = 3;
    @Ignore
    public static final int STRING = 4;
    @Ignore
    public static final int FLOAT = 5;
    @Ignore
    public static final int BIG_DATA = 6;
    @Ignore
    public static final int JSON = 7;

    @PrimaryKey
    private int recipient;
    private int sender;
    private int timestamp;
    private int command;
    private int length;
    private int status;
    private int register;
    private int type;
    private int id;
    private String message;


    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getRecipient() {
        return recipient;
    }

    public void setRecipient(int recipient) {
        this.recipient = recipient;
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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRegister() {
        return register;
    }

    public void setRegister(int register) {
        this.register = register;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Package{" +
                "recipient=" + recipient +
                ", sender=" + sender +
                ", timestamp=" + timestamp +
                ", command=" + command +
                ", length=" + length +
                ", status=" + status +
                ", register=" + register +
                ", type=" + type +
                ", id=" + id +
                ", message='" + message + '\'' +
                '}';
    }

}
