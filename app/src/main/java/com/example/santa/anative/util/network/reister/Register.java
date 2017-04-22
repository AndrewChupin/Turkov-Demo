package com.example.santa.anative.util.network.reister;

/**
 * Created by santa on 22.03.17.
 */

public final class Register {

    // SCHEDULE REGISTERS
    public static final byte SCHEDULE = 0x0002;

    // SCHEDULE
    public static final byte SCHEDULE_ALL_STATE = 0x01;

    // SCHEDULE SUB REGISTERS GET
    public static final byte SETTING_DAY = 0x01;
    public static final byte SETTING_NUMBER = 0x01;
    public static final byte SETTING_HOUR = 0x01;
    public static final byte SETTING_MINUTE = 0x01;
    public static final byte SETTING_STATE = 0x01;
    public static final byte SETTING_STATUS = 0x01; //  ON\OFF EQUIPMENT

    // SCHEDULE SUB REGISTERS COMMAND_WRITE
    public static final byte SETTING_TEMPERATURE = 0x01;
    public static final byte SETTING_SPEED = 0x01;
    public static final byte SETTING_HUMIDITY = 0x01;



    // EQUIPMENT REGISTERS
    public static final byte EQUIPMENT = 0x0003;
    public static final byte EQUIPMENT_CREATE = 0x0000;

    // EQUIPMENT SUB REGISTERS GET
    public static final byte EQUIPMENT_ID = 0x01;
    public static final byte EQUIPMENT_TYPE = 0x01;
    public static final byte EQUIPMENT_TITLE = 0x01;
    public static final byte EQUIPMENT_NAME = 0x01;
    public static final byte EQUIPMENT_SERIAL = 0x01;
    public static final byte EQUIPMENT_PIN = 0x01;
    public static final byte EQUIPMENT_COUNTRY = 0x01;
    public static final byte EQUIPMENT_LOCATION = 0x01;
    public static final byte EQUIPMENT_ADDRESS = 0x01;
    public static final byte EQUIPMENT_GPS = 0x01;

    // EQUIPMENT SUB REGISTERS COMMAND_WRITE
    public static final byte EQUIPMENT_SET_STATE = 0x01;
    public static final byte EQUIPMENT_SETTINGS_STATE = 0x01;
    public static final byte EQUIPMENT_SET_TEMPERATURE = 0x01;
    public static final byte EQUIPMENT_SET_HUMIDITY = 0x01;
    public static final byte EQUIPMENT_SET_SPEED = 0x01;



    // EQUIPMENT REGISTERS
    public static final byte PROFILE = 0x0004;

    // EQUIPMENT SUB REGISTERS GET
    public static final byte PROFILE_NAME = 0x01;
    public static final byte PROFILE_SURNAME = 0x01;
    public static final byte PROFILE_PATRONOMIC = 0x01;
    public static final byte PROFILE_PHONE = 0x01;
    public static final byte PROFILE_EMAIL = 0x01;



/*    public static final byte EQUIPMENT_ID = 0x0000; // Уникальный идентификатор объекта (целое/32)
    public static final byte EQUIPMENT_TYPE = 0x1001; // Тип объекта (целое/16)
    public static final byte EQUIPMENT_SERIAL = 0x4002; // Серийный номер (строка/32)
    public static final byte EQUIPMENT_NAME = 0x4003; // Наименование объекта (строка/128)
    public static final byte EQUIPMENT_PURCHASE_BILL = 0x4004; // Номер счета или контракта на закупку (строка/64)
    public static final byte EQUIPMENT_BEGIN_TIME = 0x0005; // Дата ввода в эксплуатацию (unixtime)
    public static final byte EQUIPMENT_END_TIME_GARANT = 0x0006; // Дата окончания гарантии (unixtime)
    public static final byte EQUIPMENT_END_TIME_SERVER = 0x0007; // Дата окончания аренды сервера
    public static final byte EQUIPMENT_SERVICE_BILL = 0x4008; // Номер счета на сервисное обслуживание
    public static final byte EQUIPMENT_END_TIME_SERVICE = 0x0009; // Дата окончания сервисного обслуживания
    public static final byte EQUIPMENT_OWNER = 0x400A; // Владелец: фамилия, имя, отчество или название организации (строка/128)
    public static final byte EQUIPMENT_COUNTRY_CODE = 0x400B; // Код страны (строка/2)
    public static final byte EQUIPMENT_REGION_CODE = 0x100C; // Код региона (число/16)
    public static final byte EQUIPMENT_LOCAL = 0x400D; // Населенный пункт (строка/128)
    public static final byte EQUIPMENT_ADDRESS = 0x400E; // Адрес установки (строка/128)
    public static final byte EQUIPMENT_OWENER_PHONE = 0x4010; // Контактный телефон владельца (строка/12)
    public static final byte EQUIPMENT_OWENER_EMAIL = 0x4011; // Адрес электронной почты владельца (строка/80)
    public static final byte EQUIPMENT_OWENER_ID = 0x0012; // Идентификатор пользователя-владельца (целое/32)
    public static final byte EQUIPMENT_USERS_IDS = 0x8013; // Список идентификаторов пользователей (строка)
    public static final byte EQUIPMENT_SERVICES_IDS = 0x8014; // Список идентификаторов обслуживающих организаций (строка)
    public static final byte EQUIPMENT_HISTORY = 0xA015; // Запись из истории обслуживания (строка)
    public static final byte EQUIPMENT_VIS_PROFILE = 0x1016; // Видимый профиль (целое/16) логический
    public static final byte EQUIPMENT_GPS = 0x4017; // Координаты GPS (строка/32)
    public static final byte EQUIPMENT_IMAGE = 0x6018; // Изображение объекта управления (большой объект) image/png.10
    public static final byte EQUIPMENT_LIST = 0x8110; // Список идентификаторов всех объектов управления для посред- ника


    public static final byte EQUIPMENT_META_MAIN = 0x7101; // Общая информация.
    public static final byte EQUIPMENT_META_FINANCE = 0x7102; // Финансовая информация
    public static final byte EQUIPMENT_META_OWNER = 0x7103; // Владелец
    public static final byte EQUIPMENT_META_GEO = 0x7104; // География объекта


    public static final byte EQUIPMENT_META_REG_REQUEST = 0x4105; // Регистр запроса регистрации нового формуляра.
    public static final byte EQUIPMENT_META_REG_RESPONSE = 0x4106; // Регистр подтверждения запроса регистрации нового формуляра.
    public static final byte EQUIPMENT_META_PASS_REQUEST = 0x4107; // Регистр запроса смены пароля объекта управления.
    public static final byte EQUIPMENT_META_PASS_RESPONSE = 0x4108; // Регистр подтверждения смены пароля объекта управления.*/


}

