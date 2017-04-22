package com.example.santa.anative.network.connection;

/**
 * Created by santa on 09.03.17.
 */

public interface ConnectionDelegate {

    /**
     *
     * Данный метод принимает входящие сообщения от соединения с сервером и выбирает подходящий
     * протокол(стратегию) для дальнейшего поведелния
     * @param response сообщение сервера
     */
    void messageReceived(byte[] response);

    /**
     *
     * Данный метод принимает входящие сообщения от соединения с сервером и выбирает подходящий
     * протокол(стратегию) для дальнейшего поведелния
     * @param code код ошибки
     */
    void onConnectionEvent(int code);
}
