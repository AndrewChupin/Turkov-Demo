package com.example.santa.anative.util.network.reister;

/**
 * Created by santa on 20.03.17.
 */

public class Profiles {

    private static final int META_JSON = 0x7101; // Представление профиля обслуживающей организации в формате TYPE_JSON объекта.

    private static final int EQUIPMENT_ID = 0x0000; // Идентификатор объекта управления
    private static final int EQUIPMENT_TYPE = 0x1000; // Тип объекта управления
    private static final int EQUIPMENT_NUM = 0x1001; // Номер регистра объекта управления
    private static final int EQUIPMENT_MASK = 0x0002; // Битовая маска прав доступа к регистру
    private static final int EQUIPMENT_TEXT = 0x4003; // Текстовое описание регистра
    private static final int EQUIPMENT_MAX_LEN = 0x1004; // Максимальная длина регистра (актуально для типов "стока" и "большой объект").
    private static final int EQUIPMENT_VIEW_RULE = 0x1005; // Порядок отображения на экране
    private static final int EQUIPMENT_VIEW_TYPE = 0x1006; // Способ отображения на экране, битовый массив
    private static final int EQUIPMENT_PREFIX = 0x4007; // Метка (префикс) на экране
    private static final int EQUIPMENT_SUFFIX = 0x4008; // Суффикс на экране
    private static final int EQUIPMENT_KEEP_DATA = 0x1009; // Хранимые показания, логический Метарегистры
    private static final int EQUIPMENT_ARRAY_JSON = 0xF102; // Представление всех регистров объекта управления в виде массивов TYPE_JSON объектов
    private static final int EQUIPMENT_LIST = 0x9103; // Список номеров регистров объекта управления

    private static final int CLIENT_NUM_ID = 0x0000; // Числовой идентификатор пользователя или клиента (битовый массив/32 бита)
    private static final int CLIENT_MOBILE_ID = 0x4001; // Идентификатор мобильного устройства (строка)17
    private static final int CLIENT_PHONE = 0x4002; // Телефон (строка/12)
    private static final int CLIENT_REG_TIME = 0x0003; // Дата регистрации (целое/32) unixtime
    private static final int CLIENT_LAST_TIME = 0x0004; // Дата и время последней аутентификации (целое/32) unixtime
    private static final int CLIENT_EMAIL = 0x4005; // Адрес электронной почты пользователя (строка/80)
    private static final int CLIENT_EQUIPMENTS_IDS = 0x8006; // Список связанных объектов управления
    private static final int CLIENT_MOBILE_LIST = 0x8102; // Список всех мобильных устройств, связанных с пользователем.

    private static final int SERVICE_ID = 0x0000; // Идентификатор обслуживающей организации (целое/32)
    private static final int SERVICE_EMAIL = 0x4001; // Адрес электронной почты (строка/80)
    private static final int SERVICE_PHONE = 0x4002; // Телефон (строка/12)
    private static final int SERVICE_REG_TIME = 0x0003; // Дата регистрации (целое/32) unixtime
    private static final int SERVICE_LAST_TIME = 0x0004; // Дата и время последней аутентификации (целое/32) unixtime
    private static final int SERVICE_COUNTRY_CODE = 0x4005; // Код страны (строка/2)
    private static final int SERVICE_REGION_CODE = 0x1006; // Код региона (число/16)
    private static final int SERVICE_LOCAL = 0x4007; // Населенный пункт (строка/128)
    private static final int SERVICE_ENTITY_NAME = 0x4008; // Название юридического лица (строка/128)

    private static final int STAFF_ID = 0x0000; // Идентификатор обслуживающей организации (целое/32)
    private static final int STAFF_EMAIL = 0x4001; // Адрес электронной почты (строка/80)
    private static final int STAFF_PHONE = 0x4002; // Телефон (строка/12)
    private static final int STAFF_REG_TIME = 0x0003; // Дата регистрации (целое/32) unixtime
    private static final int STAFF_LAST_TIME = 0x0004; // Дата и время последней аутентификации (целое/32) unixtime
    private static final int SERVICE_MASK_ROLE = 0x0005; // Маска ролей пользователя (целое/32)

    private static final int SERVER_ID = 0x0000; // Идентификатор сервера (целое/32)
    private static final int SERVER_IP4 = 0x4001; // Адрес IPv4 (строка)
    private static final int SERVER_IP6 = 0x4002; // Адрес IPv6
    private static final int SERVER_STATE = 0x1003; // Состояние сервера (целое/16)
    private static final int SERVER_NEXT_DISABLE_TIME = 0x4004; // Дата следующего планового отключения (целое/32) unixtime
    private static final int SERVER_DATA_ADDRESS = 0x4005; // Адрес датацентра (строка/128)

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
