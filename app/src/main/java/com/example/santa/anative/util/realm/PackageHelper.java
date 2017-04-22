package com.example.santa.anative.util.realm;

import android.util.Log;

import com.example.santa.anative.model.entity.Package;
import com.example.santa.anative.util.common.ByteArray;
import com.example.santa.anative.util.common.ByteHelper;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Locale;


/**
 * Created by santa on 29.03.17.
 */

public final class PackageHelper {

    // Маска используется для преобразования параметров пакета в бинарную комманду
    private static final String CMD_BINARY_MASK = "%s%3s0%010d";

    /**
     * Данная функция конвертирует входящий в нее пакет к виду, пригодному для передачи на сервер
     * @param pack объект для конвертирования
     * @return конвертированный в строку объект
     */
    public static byte[] convertToMessage(Package pack) {
        long a = System.currentTimeMillis();
        String typeBinary = getBinaryType(pack.getType());
        String registerBinary = Integer.toBinaryString(pack.getRegister());
        String formatRegister = String.format("%12s", registerBinary).replace(" ", "0");
        short register = Short.parseShort(typeBinary + formatRegister, 2);

        String statusBinary = getBinaryStatus(pack.getStatus());
        Log.d("Logos", "PackageHelper | convertToMessage: statusBinary " + statusBinary);
        String commandBinary = getBinaryCommand(pack.getCommand());
        Log.d("Logos", "PackageHelper | convertToMessage: commandBinary " + commandBinary);
        String lengthBinary = Integer.toBinaryString(pack.getLength());
        Log.d("Logos", "PackageHelper | convertToMessage: lengthBinary " + lengthBinary);
        String cmdBinary = String.format(Locale.ENGLISH, CMD_BINARY_MASK , statusBinary,
                commandBinary, Integer.parseInt(lengthBinary));
        short cmd = Short.parseShort(cmdBinary, 2);
        Log.d("Logos", "PackageHelper | convertToMessage: cmdBinary " + cmdBinary);

        // Sunder - 8 Recipient - 8 Cmd - 4 Register - 4 Timestamp - 8 PackageId - 4
        ByteArray request = new ByteArray();
        request.append(pack.getSender(),
                pack.getRecipient(),
                ByteHelper.shortToByteArray(cmd),
                ByteHelper.shortToByteArray(register),
                ByteHelper.intToByteArray(pack.getTimestamp()),
                ByteHelper.shortToByteArray(pack.getId()),
                pack.getMessage()
        );
        Log.d("Logos", "PackageHelper | convertToMessage | : " + Arrays.toString(request.array()));
        long b = System.currentTimeMillis();
        Log.d("Logos", "PackageHelper | convertToMessage | : " + (b - a));
        return request.array();
    }


    /**
     * Данная функция принимает входящий от сервера пакет с текстовом виде и конвертирует его в
     * удобный для использования объект
     * @param packEncode пакет в строковом виде
     * @return конвертированная в объект, строка
     */
    public static Package convertToPackage(byte[] packEncode) {
        long a = System.currentTimeMillis();
        Package pack = new Package();
        /*
         * [, 1326D64B, 00000000, 0803, 0D23, 1BCD8CA0, 007B]
         * 1 - sunder 2 - recipient 3 - cmd 4 - register 5 - time 6 - id 7 - message
         */

        pack.setSender(Arrays.copyOfRange(packEncode, 0, 4));
        pack.setRecipient(Arrays.copyOfRange(packEncode, 4, 8));
        pack.setTimestamp(ByteHelper.byteArrayToInt(Arrays.copyOfRange(packEncode, 12, 16)));
        pack.setId(ByteHelper.byteArrayToShort(Arrays.copyOfRange(packEncode, 16, 18)));
        pack.setMessage(Arrays.copyOfRange(packEncode, 18, packEncode.length));

        short cmdHex = ByteHelper.byteArrayToShort(Arrays.copyOfRange(packEncode, 8, 10));
        String binaryCommand = new BigInteger(String.valueOf(cmdHex)).toString(2);
        String formatCommand = String.format("%16s", binaryCommand).replace(" ", "0");
        Log.d("Logos", "PackageHelper | convertToPackage | formatCommand: " + formatCommand);

        short registerHex = ByteHelper.byteArrayToShort(Arrays.copyOfRange(packEncode, 10, 12));
        String binaryRegister = new BigInteger(String.valueOf(registerHex)).toString(2);
        String formatRegister = String.format("%16s", binaryRegister).replace(" ", "0");
        Log.d("Logos", "PackageHelper | convertToPackage | formatRegister: " + formatRegister);

        pack.setType(getType(formatRegister.substring(0,4)));
        pack.setRegister(Integer.parseInt(formatRegister.substring(4), 2));
        pack.setStatus(getStatus(formatCommand.substring(0, 2)));
        pack.setCommand(getCommand(formatCommand.substring(2, 5)));
        pack.setLength(Integer.parseInt(formatCommand.substring(7), 2));

        Log.d("Logos", "PackageHelper | convertToPackage: pack" + pack.toString());
        long b = System.currentTimeMillis();
        Log.d("Logos", "PackageHelper | convertToPackage | : " + (b - a));
        return pack;
    }


    private static int getStatus(String status) {
        switch (status) {
            case "00":
                return Package.STATUS_REQUEST;
            case "01":
                return Package.STATUS_RESPONSE;
            case "10":
                return Package.STATUS_REJECT;
            case "11":
                return Package.STATUS_UNDEFINE;
            default:
                return -1;
        }
    }


    private static String getBinaryStatus(int status) {
        switch (status) {
            case Package.STATUS_REQUEST:
                return "00";
            case Package.STATUS_RESPONSE:
                return "01";
            case Package.STATUS_REJECT:
                return "10";
            case Package.STATUS_UNDEFINE:
                return "11";
            default:
                return "";
        }
    }


    private static int getCommand(String command) {
        switch (command) {
            case "000":
                return Package.COMMAND_READ;
            case "001":
                return Package.COMMAND_WRITE;
            case "010":
                return Package.COMMAND_READ_BIG;
            case "011":
                return Package.COMMAND_WRITE_BIG;
            default:
                return -1;
        }
    }


    private static String getBinaryCommand(int command) {
        switch (command) {
            case Package.COMMAND_READ:
                return "000";
            case Package.COMMAND_WRITE:
                return "001";
            case Package.COMMAND_READ_BIG:
                return "010";
            case Package.COMMAND_WRITE_BIG:
                return "011";
            default:
                return "";
        }
    }


    private static int getType(String type) {
        switch (type) {
            case "0000":
                return Package.TYPE_INTEGER;
            case "0001":
                return Package.TYPE_SHORT;
            case "0010":
                return Package.TYPE_CHAR;
            case "0011":
                return Package.TYPE_DOUBLE;
            case "0100":
                return Package.TYPE_STRING;
            case "0101":
                return Package.TYPE_FLOAT;
            case "0110":
                return Package.TYPE_BIG_DATA;
            case "0111":
                return Package.TYPE_JSON;
            default:
                return -1;
        }
    }


    private static String getBinaryType(int type) {
        switch (type) {
            case Package.TYPE_INTEGER:
                return "0000";
            case Package.TYPE_SHORT:
                return "0001";
            case Package.TYPE_CHAR:
                return "0010";
            case Package.TYPE_DOUBLE:
                return "0011";
            case Package.TYPE_STRING:
                return "0100";
            case Package.TYPE_FLOAT:
                return "0101";
            case Package.TYPE_BIG_DATA:
                return "0110";
            case Package.TYPE_JSON:
                return "0111";
            default:
                return "";
        }
    }

}
