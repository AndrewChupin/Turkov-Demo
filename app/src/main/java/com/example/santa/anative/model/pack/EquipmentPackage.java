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
 * Данный класс является вспомогающим для объектов {@link Equipment}
 * Посредством передачи неободимой информации генерируется объет {@link Package} для удобного
 * хранения и передачи на сервер
 */

public class EquipmentPackage {


    /**
     * Метод генерирует {@link Package} для привязки нового оборудования к аккаунту
     * @param senderId адресс отправителя
     * @param name наименование оборудования
     * @param pin пин-код оборудования
     * @param serial serial number оборудоания
     * @return {@link Package} с даннымим о новом оборудовании
     */
    public static Package createAdditionEquipmentPackage(int senderId, String name, byte[] pin, String serial) {
        Package pack = generateNewPackage(senderId, senderId, Register.EQUIPMENT_CREATE, Package.TYPE_JSON); // TODO ADDRESS

        JSONObject message = new JSONObject();
        try {
            message.put("name", name);
            message.put("serial_number", serial);
            if (pin.length > 0) message.put("pin", new String(pin));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        byte[] messageArray = message.toString().getBytes();
        pack.setMessage(messageArray);
        pack.setLength(messageArray.length);

        return pack;
    }


    /**
     * Метод генерирует {@link Package} для включения/выключения всего расписания оборудования
     * @param senderId адрес отправителя
     * @param equipment оборудование {@link Equipment}
     * @return {@link Package}
     */
    public static Package createAllStateSettingsPackage(int senderId, Equipment equipment) {
        Package pack = generateNewPackage(senderId, equipment.getId(), Register.EQUIPMENT, Package.TYPE_SHORT); // TODO byte or short

        ByteArray message = new ByteArray();

        message.append(Register.EQUIPMENT_SETTINGS_STATE);
        byte state = (byte) (equipment.isSettingsEnable() ? 1 : 0);
        message.append(state);

        byte[] messageArray = message.array();
        pack.setMessage(messageArray);
        pack.setLength(messageArray.length);

        return pack;
    }


    /**
     * Данный метод генерирует {@link Package} для редактирования общей информации об оборудовании
     * @param senderId адрес отправителя
     * @param equipment {@link Equipment}
     * @return {@link Package}
     */
    public static Package createEditEquipmentPackage(int senderId, Equipment equipment) {
        Package pack = generateNewPackage(senderId, equipment.getId(), Register.EQUIPMENT, Package.TYPE_JSON);

        JSONObject message = new JSONObject();
        int len = 0;

        try {
            message.put("title", equipment.getTitle());
            message.put("country", equipment.getCountry());
            message.put("location", equipment.getLocation());
            message.put("address", equipment.getAddress());
            message.put("gps", equipment.getGps());

            byte[] byteMessage = message.toString().getBytes();
            pack.setMessage(byteMessage);
            len = byteMessage.length;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        pack.setLength(len);
        return pack;
    }


    /**
     * Данный метод генерирует стандартный {@link Package} для объекта типа {@link Equipment}
     * @param senderId адрес отправителя
     * @param equipmentId идентификатор {@link Equipment}
     * @param register номер {@link Register}
     * @return {@link Package}
     */
    private static Package generateNewPackage(int senderId, int equipmentId, byte register, int type) {
        Package pack = new Package();
        pack.setSender(ByteHelper.intToByteArray(senderId));
        pack.setRecipient(ByteHelper.intToByteArray(equipmentId));
        pack.setRegister(register);
        pack.setCommand(type);
        pack.setStatus(Package.STATUS_REQUEST);
        pack.setType(Package.TYPE_BIG_DATA);
        pack.setId((short) 123);
        pack.setTimestamp((int) System.currentTimeMillis());
        return pack;
    }

}
