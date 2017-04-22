package com.example.santa.anative.model.entity;

import java.util.Arrays;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by santa on 25.03.17.
 */

@RealmClass
public class Package implements RealmModel {

    // Builder Status
    @Ignore public static final int STATUS_REQUEST = 0;
    @Ignore public static final int STATUS_RESPONSE = 1;
    @Ignore public static final int STATUS_REJECT = 2;
    @Ignore public static final int STATUS_UNDEFINE = 3;

    // Command
    @Ignore public static final int COMMAND_READ = 0;
    @Ignore public static final int COMMAND_WRITE = 1;
    @Ignore public static final int COMMAND_READ_BIG = 2;
    @Ignore public static final int COMMAND_WRITE_BIG = 3;

    // Data Type
    @Ignore public static final int TYPE_INTEGER = 0;
    @Ignore public static final int TYPE_SHORT = 1;
    @Ignore public static final int TYPE_CHAR = 2;
    @Ignore public static final int TYPE_DOUBLE = 3;
    @Ignore public static final int TYPE_STRING = 4;
    @Ignore public static final int TYPE_FLOAT = 5;
    @Ignore public static final int TYPE_BIG_DATA = 6;
    @Ignore public static final int TYPE_JSON = 7;

    @PrimaryKey
    private int register;

    private byte[] recipient;
    private byte[] sender;
    private byte[] message;

    private int timestamp;
    private int command;
    private int length;
    private int status;

    private int type;
    private short id;

    public byte[] getRecipient() {
        return recipient;
    }

    public void setRecipient(byte[] recipient) {
        this.recipient = recipient;
    }

    public byte[] getSender() {
        return sender;
    }

    public void setSender(byte[] sender) {
        this.sender = sender;
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

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Package{" +
                "recipient=" + Arrays.toString(recipient) +
                ", sender=" + Arrays.toString(sender) +
                ", timestamp=" + timestamp +
                ", command=" + command +
                ", length=" + length +
                ", status=" + status +
                ", register=" + register +
                ", type=" + type +
                ", id=" + id +
                ", message='" + Arrays.toString(message) + '\'' +
                '}';
    }
}
