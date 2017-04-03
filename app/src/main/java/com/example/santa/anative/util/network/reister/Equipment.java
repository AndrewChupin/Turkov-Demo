package com.example.santa.anative.util.network.reister;

/**
 * Created by santa on 22.03.17.
 */

public final class Equipment {

    private static final int EQUIPMENT_ID = 0x0000; // Уникальный идентификатор объекта (целое/32)
    private static final int EQUIPMENT_TYPE = 0x1001; // Тип объекта (целое/16)
    private static final int EQUIPMENT_SERIAL = 0x4002; // Серийный номер (строка/32)
    private static final int EQUIPMENT_NAME = 0x4003; // Наименование объекта (строка/128)
    private static final int EQUIPMENT_PURCHASE_BILL = 0x4004; // Номер счета или контракта на закупку (строка/64)
    private static final int EQUIPMENT_BEGIN_TIME = 0x0005; // Дата ввода в эксплуатацию (unixtime)
    private static final int EQUIPMENT_END_TIME_GARANT = 0x0006; // Дата окончания гарантии (unixtime)
    private static final int EQUIPMENT_END_TIME_SERVER = 0x0007; // Дата окончания аренды сервера
    private static final int EQUIPMENT_SERVICE_BILL = 0x4008; // Номер счета на сервисное обслуживание
    private static final int EQUIPMENT_END_TIME_SERVICE = 0x0009; // Дата окончания сервисного обслуживания
    private static final int EQUIPMENT_OWNER = 0x400A; // Владелец: фамилия, имя, отчество или название организации (строка/128)
    private static final int EQUIPMENT_COUNTRY_CODE = 0x400B; // Код страны (строка/2)
    private static final int EQUIPMENT_REGION_CODE = 0x100C; // Код региона (число/16)
    private static final int EQUIPMENT_LOCAL = 0x400D; // Населенный пункт (строка/128)
    private static final int EQUIPMENT_ADDRESS = 0x400E; // Адрес установки (строка/128)
    private static final int EQUIPMENT_OWENER_PHONE = 0x4010; // Контактный телефон владельца (строка/12)
    private static final int EQUIPMENT_OWENER_EMAIL = 0x4011; // Адрес электронной почты владельца (строка/80)
    private static final int EQUIPMENT_OWENER_ID = 0x0012; // Идентификатор пользователя-владельца (целое/32)
    private static final int EQUIPMENT_USERS_IDS = 0x8013; // Список идентификаторов пользователей (строка)
    private static final int EQUIPMENT_SERVICES_IDS = 0x8014; // Список идентификаторов обслуживающих организаций (строка)
    private static final int EQUIPMENT_HISTORY = 0xA015; // Запись из истории обслуживания (строка)
    private static final int EQUIPMENT_VIS_PROFILE = 0x1016; // Видимый профиль (целое/16) логический
    private static final int EQUIPMENT_GPS = 0x4017; // Координаты GPS (строка/32)
    private static final int EQUIPMENT_IMAGE = 0x6018; // Изображение объекта управления (большой объект) image/png.10
    private static final int EQUIPMENT_LIST = 0x8110; // Список идентификаторов всех объектов управления для посред- ника


    private static final int EQUIPMENT_META_MAIN = 0x7101; // Общая информация.
    private static final int EQUIPMENT_META_FINANCE = 0x7102; // Финансовая информация
    private static final int EQUIPMENT_META_OWNER = 0x7103; // Владелец
    private static final int EQUIPMENT_META_GEO = 0x7104; // География объекта


    private static final int EQUIPMENT_META_REG_REQUEST = 0x4105; // Регистр запроса регистрации нового формуляра.
    private static final int EQUIPMENT_META_REG_RESPONSE = 0x4106; // Регистр подтверждения запроса регистрации нового формуляра.
    private static final int EQUIPMENT_META_PASS_REQUEST = 0x4107; // Регистр запроса смены пароля объекта управления.
    private static final int EQUIPMENT_META_PASS_RESPONSE = 0x4108; // Регистр подтверждения смены пароля объекта управления.

}
