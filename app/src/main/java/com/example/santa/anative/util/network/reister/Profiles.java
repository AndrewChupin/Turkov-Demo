package com.example.santa.anative.util.network.reister;

/**
 * Created by santa on 20.03.17.
 */

public class Profiles {

    private static final int PROFILE_TECH = 0x00000101;
    private static final int PROFILE_EQUIPMENT = 0x00000102;
    private static final int PROFILE_SETTING = 0x00000103;
    private static final int PROFILE_USER = 0x00000104;
    private static final int PROFILE_SERVICE_ORGANIZTION = 0x00000105;
    private static final int PROFILE_ENGINEERS = 0x00000106;
    private static final int PROFILE_ADMINISTRATOR = 0x00000107;

    private static final int AUTH_TECH = 0x00000201;
    private static final int AUTH_ENQUIPMENT = 0x00000202;
    private static final int AUTH_USERS = 0x00000203;
    private static final int AUTH_SERVICE_ORGANIZTION = 0x00000204;
    private static final int AUTH_ENGINEERS = 0x00000205;
    private static final int AUTH_ADMINISTRATOR = 0x00000206;

    private static final int EQUIPMENT_ID = 0x0000; // Уникальный идентификатор объекта (целое/32)
    private static final int EQUIPMENT_TYPE = 0x0801; // Тип объекта (целое/16)
    private static final int EQUIPMENT_SERIAL = 0x2002; // Серийный номер (строка/32)
    private static final int EQUIPMENT_NAME = 0x2003; // Наименование объекта (строка/128)
    private static final int EQUIPMENT_PURCHASE_BILL = 0x2004; // Номер счета или контракта на закупку (строка/64)
    private static final int EQUIPMENT_BEGIN_TIME = 0x0005; // Дата ввода в эксплуатацию (unixtime)
    private static final int EQUIPMENT_END_TIME_GARANT = 0x0006; // Дата окончания гарантии (unixtime)
    private static final int EQUIPMENT_END_TIME_SERVER = 0x0007; // Дата окончания аренды сервера
    private static final int EQUIPMENT_SERVICE_BILL = 0x2008; // Номер счета на сервисное обслуживание
    private static final int EQUIPMENT_END_TIME_SERVICE = 0x0009; // Дата окончания сервисного обслуживания
    private static final int EQUIPMENT_OWNER = 0x200A; // Владелец: фамилия, имя, отчество или название организации (строка/128)
    private static final int EQUIPMENT_COUNTRY_CODE = 0x200B; // Код страны (строка/2)
    private static final int EQUIPMENT_REGION_CODE = 0x080C; // Код региона (число/16)
    private static final int EQUIPMENT_LOCAL = 0x200D; // Населенный пункт (строка/128)
    private static final int EQUIPMENT_ADDRESS = 0x200E; // Адрес установки (строка/128)
    private static final int EQUIPMENT_OWENER_PHONE = 0x2010; // Контактный телефон владельца (строка/12)
    private static final int EQUIPMENT_OWENER_EMAIL = 0x2011; // Адрес электронной почты владельца (строка/80)
    private static final int EQUIPMENT_OWENER_ID = 0x0012; // Идентификатор пользователя-владельца (целое/32)
    private static final int EQUIPMENT_USERS_IDS = 0x2013; // Список идентификаторов пользователей (строка)
    private static final int EQUIPMENT_SERVICES_IDS = 0x2014; // Список идентификаторов обслуживающих организаций (строка)
    private static final int EQUIPMENT_HISTORY = 0x2015; // Запись из истории обслуживания (строка)
    private static final int EQUIPMENT_VIS_PROFILE = 0x0816; // Видимый профиль (целое/16) логический
    private static final int EQUIPMENT_GPS = 0xC00F; // Координаты GPS (строка/32)

    private static final int CLIENT_ID = 0x0000; // Идентификатор пользователя (целое/32)
    private static final int CLIENT_EMAIL = 0x2001; // Адрес электронной почты пользователя (строка/80)
    private static final int CLIENT_PHONE = 0x2002; // Телефон (строка/12)
    private static final int CLIENT_REG_TIME = 0x0003; // Дата регистрации (целое/32) unixtime
    private static final int CLIENT_LAST_TIME = 0x0004; // Дата и время последней аутентификации (целое/32) unixtime
    private static final int CLIENT_MOBILE_ID = 0x2005; // Идентификатор мобильного устройства (строка)17
    private static final int CLIENT_EQUIPMENTS_IDS = 0x2006; // Список связанных объектов управления

    private static final int SERVICE_ID = 0x0000; // Идентификатор обслуживающей организации (целое/32)
    private static final int SERVICE_EMAIL = 0x2001; // Адрес электронной почты (строка/80)
    private static final int SERVICE_PHONE = 0x2002; // Телефон (строка/12)
    private static final int SERVICE_REG_TIME = 0x0003; // Дата регистрации (целое/32) unixtime
    private static final int SERVICE_LAST_TIME = 0x0004; // Дата и время последней аутентификации (целое/32) unixtime
    private static final int SERVICE_COUNTRY_CODE = 0x2005; // Код страны (строка/2)
    private static final int SERVICE_REGION_CODE = 0x0806; // Код региона (число/16)
    private static final int SERVICE_LOCAL = 0x2007; // Населенный пункт (строка/128)
    private static final int SERVICE_ENTITY_NAME = 0x2008; // Название юридического лица (строка/128)

    private static final int STAFF_ID = 0x0000; // Идентификатор обслуживающей организации (целое/32)
    private static final int STAFF_EMAIL = 0x2001; // Адрес электронной почты (строка/80)
    private static final int STAFF_PHONE = 0x2002; // Телефон (строка/12)
    private static final int STAFF_REG_TIME = 0x0003; // Дата регистрации (целое/32) unixtime
    private static final int STAFF_LAST_TIME = 0x0004; // Дата и время последней аутентификации (целое/32) unixtime
    private static final int SERVICE_MASK_ROLE = 0x0005; // Маска ролей пользователя (целое/32)

    private static final int SERVER_ID = 0x0000; // Идентификатор сервера (целое/32)
    private static final int SERVER_IP4 = 0x2001; // Адрес IPv4 (строка)
    private static final int SERVER_IP6 = 0x2002; // Адрес IPv6
    private static final int SERVER_STATE = 0x0003; // Состояние сервера (целое/16)
    private static final int SERVER_NEXT_DISABLE_TIME = 0x0004; // Дата следующего планового отключения (целое/32) unixtime
    private static final int SERVER_DATA_ADDRESS = 0x0005; // Адрес датацентра (строка/128)

    private static final int OBJSTATE = 0x0001; // R Состояние (целое/32)
    private static final int MAINSWITCH = 0x0802; // RW Главный выключатель (целое/16)
    private static final int OBJTYPE = 0x0803; // R Тип (целое/16)
    private static final int OBJSERIAL = 0x2004; // R Серийный номер (строка/32)
    private static final int MOTORCOUNT = 0x0005; // R Счетчик моточасов (целое/32) в секундах
    private static final int MCAS = 0x0006; // RW Счетчик моточасов после техобслуживания (целое/32) в секундах
    private static final int SW_MAJOR = 0x0807; // R Версия ПО (целое/16)
    private static final int SW_MINOR = 0x0808; // Субверсия ПО (целое/16)
    private static final int SET_WARM = 0x1009; // RW Уставка при работе на обогрев, градусы цельсия, фиксиро- ванная точка, один знак после точки, 16 бит
    private static final int SET_COOL = 0x100A; //  RW Уставка при работе на холод, фиксированная точка, один знак после точки, 16 бит
    private static final int SET_DRY = 0x100B; // RW Уставка при работе на осушение, фиксированная точка, один знак после точки, 16 бит
    private static final int SET_AUTO = 0x100C; // RW Универсальная уставка на все режимы, фиксированная точка, один знак после точки, 16 бит

}
