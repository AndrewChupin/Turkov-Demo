package com.example.santa.anative.model.pack;

import com.example.santa.anative.model.entity.Equipment;
import com.example.santa.anative.model.entity.Package;
import com.example.santa.anative.model.entity.Profile;
import com.example.santa.anative.model.entity.Setting;
import com.example.santa.anative.util.common.ByteArray;
import com.example.santa.anative.util.common.ByteHelper;
import com.example.santa.anative.util.network.reister.Register;

/**
 * Created by santa on 08.04.17.
 * Данный класс является вспомогающим для объектов {@link Setting}
 * Посредством передачи неободимой информации генерируется объет {@link Package} для удобного
 * хранения и передачи на сервер
 */

public class SettingPackage {


    /**
     * Данный метод генерирует {@link Package} для измения состояния конкретного {@link Setting}
     * @param senderId адрес отправителя
     * @param equipmentId {@link Equipment} к которому относится данный {@link Setting}
     * @param setting редактируемый объект {@link Setting}
     * @return {@link Package}
     */
    public static Package createStateSettingPackage(int senderId, int equipmentId, Setting setting) {
        Package pack = generateNewPackage(senderId, equipmentId);
        ByteArray message = new ByteArray();

        message.append(Register.SETTING_DAY);
        message.append((byte) setting.getDay());

        message.append(Register.SETTING_NUMBER);
        message.append((byte) setting.getNumber());

        message.append(Register.SETTING_STATE);
        byte state = (byte) (setting.isActive() ? 1 : 0);
        message.append(state);

        byte[] messageArray = message.array();
        pack.setMessage(messageArray);
        pack.setLength(messageArray.length);

        return pack;
    }


    /**
     * Данный метод генерирует стандартный {@link Package} для объекта типа {@link Setting}
     * @param senderId адрес отправителя
     * @param equipmentId идентификатор {@link Equipment}
     * @param equipmentType тип {@link Equipment}, служит маркером об объекте,
     *                      в зависимости от типа меняются специфичные для типа объекта данные
     * @param setting редактируемый объект {@link Setting}
     * @return {@link Package}
     */
    public static Package createSettingPackage(int senderId, int equipmentId, int equipmentType, Setting setting) {
        Package pack = generateNewPackage(senderId, equipmentId);

        ByteArray message = new ByteArray();
        message.append(Register.SETTING_DAY);
        message.append((byte) setting.getDay());

        message.append(Register.SETTING_NUMBER);
        message.append((byte) setting.getNumber());

        message.append(Register.SETTING_HOUR);
        message.append((byte) setting.getHour());

        message.append(Register.SETTING_MINUTE);
        message.append((byte) setting.getMinutes());

        message.append(Register.SETTING_STATE);
        byte state = (byte) (setting.isActive() ? 1 : 0);
        message.append(state);

        message.append(Register.SETTING_STATUS);
        byte status = (byte) (setting.isEnable() ? 1 : 0);
        message.append(status);

        switch (equipmentType) {
            case Equipment.FAN:
            case Equipment.CONVECTOR:
                message.append(Register.SETTING_TEMPERATURE);
                message.append((byte) setting.getTemperature());

                message.append(Register.SETTING_SPEED);
                message.append((byte) setting.getSpeed());
                break;
            case Equipment.DESICCANT:
                message.append(Register.SETTING_HUMIDITY);
                message.append((byte) setting.getHumidity());
                break;
        }

        byte[] messageArray = message.array();
        pack.setMessage(messageArray);
        pack.setLength(messageArray.length);

        return pack;
    }


    /**
     * Данный метод генерирует стандартный {@link Package} для объекта типа {@link Setting}
     * @param senderId адрес отправителя
     * @param equipmentId идентификатор {@link Equipment}
     * @return {@link Package}
     */
    private static Package generateNewPackage(int senderId, int equipmentId) {
        Package pack = new Package();
        pack.setSender(ByteHelper.intToByteArray(senderId));
        pack.setRecipient(ByteHelper.intToByteArray(equipmentId));
        pack.setRegister(Register.SCHEDULE);
        pack.setCommand(Package.COMMAND_WRITE_BIG);
        pack.setStatus(Package.STATUS_REQUEST);
        pack.setType(Package.TYPE_BIG_DATA);
        pack.setId((short) 123);
        pack.setTimestamp((int) System.currentTimeMillis());
        return pack;
    }
}
