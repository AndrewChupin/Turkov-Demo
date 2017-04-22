package com.example.santa.anative.model.pack;

import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.model.entity.Package;
import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.util.common.ByteArray;
import com.example.santa.anative.util.common.ByteHelper;
import com.example.santa.anative.util.network.reister.Register;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by santa on 08.04.17.
 * Данный класс является вспомогающим для объектов {@link Profile}
 * Посредством передачи неободимой информации генерируется объет {@link Package} для удобного
 * хранения и передачи на сервер
 */

public class ProfilePackage {


    /**
     * Данный метод генерирует {@link Package} для редактирования общей информации об {@link Profile}
     * @param profile необходим для получения измененной информации объекта {@link Profile}
     * @return {@link Package}
     */
    public static Package createEditProfilePackage(Profile profile) {
        Package pack = generateNewPackage(profile.getClientId());
        JSONObject message = new JSONObject();
        try {
            message.put("name", profile.getName());
            message.put("surname", profile.getSurname());
            message.put("patronymic", profile.getPatronymic());
            message.put("phone", profile.getPhone());
            message.put("email", profile.getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        byte[] messageArray = message.toString().getBytes();
        pack.setMessage(messageArray);
        pack.setLength(messageArray.length);

        return pack;
    }


    public static void editProfileByMessage(Profile profile, byte[] message) {
        try {
            JSONObject result = new JSONObject(new String(message));

            profile.setName(result.getString("name"));
            profile.setSurname(result.getString("surname"));
            profile.setPatronymic(result.getString("patronymic"));
            profile.setPhone(result.getString("phone"));
            profile.setEmail(result.getString("email"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Данный метод генерирует стандартный {@link Package} для объекта типа {@link Profile}
     * @param senderId адрес отправителя
     * @return {@link Package}
     */
    public static Package generateNewPackage(int senderId) {
        Package pack = new Package();
        pack.setSender(ByteHelper.intToByteArray(senderId));
        pack.setRecipient(ByteHelper.intToByteArray(senderId)); // TODO
        pack.setRegister(Register.PROFILE);
        pack.setCommand(Package.COMMAND_WRITE_BIG);
        pack.setStatus(Package.STATUS_REQUEST);
        pack.setType(Package.TYPE_JSON);
        pack.setId((short) 123);
        pack.setTimestamp((int) System.currentTimeMillis());
        return pack;
    }

}
