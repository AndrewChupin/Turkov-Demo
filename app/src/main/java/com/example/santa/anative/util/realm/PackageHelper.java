package com.example.santa.anative.util.realm;

import android.util.Base64;
import android.util.Log;

import com.example.santa.anative.model.entity.Package;
import com.example.santa.anative.util.common.ByteHelper;
import com.example.santa.anative.util.common.Validate;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Locale;

import io.realm.PackageRealmProxy;

/**
 * Created by santa on 29.03.17.
 */

public final class PackageHelper {

    // Маска используется для преобразования параметров пакета в бинарную комманду
    private static final String CMD_BINARY_MASK = "%s%3s0%010d";
    // Маска используется для построения из параметров пакета сообщения, пригодного для передачи на сервер
    private static final String PACKAGE_MESSAGE_MASK = "0x%08X0x%08X0x%04X0x%04X0x%08X0x%04X";

    /**
     * Данная функция конвертирует входящий в нее пакет к виду, пригодному для передачи на сервер
     * @param pack объект для конвертирования
     * @return конвертированный в строку объект
     */
    public static String convertToMessage(Package pack) {
        long a = System.currentTimeMillis();
        String typeBinary = getBinaryType(pack.getType());
        String registerBinary = Integer.toBinaryString(pack.getRegister());
        String formatRegister = String.format("%12s", registerBinary).replace(" ", "0");
        int register = Integer.parseInt(typeBinary + formatRegister, 2);

        String statusBinary = getBinaryStatus(pack.getStatus());
        Log.d("Logos", "PackageHelper | convertToMessage: statusBinary " + statusBinary);
        String commandBinary = getBinaryCommand(pack.getCommand());
        Log.d("Logos", "PackageHelper | convertToMessage: commandBinary " + commandBinary);
        String lengthBinary = Integer.toBinaryString(pack.getLength());
        Log.d("Logos", "PackageHelper | convertToMessage: lengthBinary " + lengthBinary);
        String cmdBinary = String.format(Locale.ENGLISH, CMD_BINARY_MASK , statusBinary,
                commandBinary, Integer.parseInt(lengthBinary));
        int cmd = Integer.parseInt(cmdBinary, 2);
        Log.d("Logos", "PackageHelper | convertToMessage: cmdBinary " + cmdBinary);

        // Sunder - 8 Recipient - 8 Cmd - 4 Register - 4 Timestamp - 8 PackageId - 4
        String packageMessage = String.format(Locale.ENGLISH, PACKAGE_MESSAGE_MASK,
                pack.getRecipient(), pack.getSender(), cmd, register, pack.getTimestamp(), pack.getId());
        Log.d("Logos", "PackageHelper | convertToMessage | packageMessage: " + packageMessage);
        long b = System.currentTimeMillis();
        Log.d("Logos", "PackageHelper | convertToMessage | : " + (b - a));
        return packageMessage;
    }


    /**
     * Данная функция принимает входящий от сервера пакет с текстовом виде и конвертирует его в
     * удобный для использования объект
     * @param packEncode пакет в строковом виде
     * @return конвертированная в объект, строка
     */
    public static Package convertToPackage(String packEncode) {
        long a = System.currentTimeMillis();
        Package pack = new Package();
        /*
         * [, 1326D64B, 00000000, 0803, 0D23, 1BCD8CA0, 007B]
         * 1 - sunder 2 - recipient 3 - cmd 4 - register 5 - time 6 - id 7 - message
         */
        String[] packageParams = packEncode.split("0x");

        pack.setSender(Integer.parseInt(packageParams[1], 16));
        pack.setRecipient(Integer.parseInt(packageParams[2], 16));
        pack.setTimestamp(Integer.parseInt(packageParams[5], 16));
        pack.setId(Integer.parseInt(packageParams[6], 16));

        int cmdHex = Integer.parseInt(packageParams[3], 16);
        String binaryCommand = new BigInteger(String.valueOf(cmdHex)).toString(2);
        String formatCommand = String.format("%16s", binaryCommand).replace(" ", "0");
        Log.d("Logos", "PackageHelper | convertToPackage | formatCommand: " + formatCommand);

        int registerHex = Integer.parseInt(packageParams[4], 16);
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
                return Package.REQUEST;
            case "01":
                return Package.RESPONSE;
            case "10":
                return Package.REJECT;
            case "11":
                return Package.UNDEFINE;
            default:
                return -1;
        }
    }


    private static String getBinaryStatus(int status) {
        switch (status) {
            case Package.REQUEST:
                return "00";
            case Package.RESPONSE:
                return "01";
            case Package.REJECT:
                return "10";
            case Package.UNDEFINE:
                return "11";
            default:
                return "";
        }
    }


    private static int getCommand(String command) {
        switch (command) {
            case "000":
                return Package.READ;
            case "001":
                return Package.WRITE;
            case "010":
                return Package.READ_BIG;
            case "011":
                return Package.WRITE_BIG;
            default:
                return -1;
        }
    }


    private static String getBinaryCommand(int command) {
        switch (command) {
            case Package.READ:
                return "000";
            case Package.WRITE:
                return "001";
            case Package.READ_BIG:
                return "010";
            case Package.WRITE_BIG:
                return "011";
            default:
                return "";
        }
    }


    private static int getType(String type) {
        switch (type) {
            case "0000":
                return Package.INTEGER;
            case "0001":
                return Package.SHORT;
            case "0010":
                return Package.CHAR;
            case "0011":
                return Package.DOUBLE;
            case "0100":
                return Package.STRING;
            case "0101":
                return Package.FLOAT;
            case "0110":
                return Package.BIG_DATA;
            case "0111":
                return Package.JSON;
            default:
                return -1;
        }
    }


    private static String getBinaryType(int type) {
        switch (type) {
            case Package.INTEGER:
                return "0000";
            case Package.SHORT:
                return "0001";
            case Package.CHAR:
                return "0010";
            case Package.DOUBLE:
                return "0011";
            case Package.STRING:
                return "0100";
            case Package.FLOAT:
                return "0101";
            case Package.BIG_DATA:
                return "0110";
            case Package.JSON:
                return "0111";
            default:
                return "";
        }
    }

}
