package com.pechenkin.travelmoney.cost;

import com.pechenkin.travelmoney.bd.Member;

import java.util.Date;

/**
 * Created by pechenkin on 06.04.2018.
 * Интерфейс для элемента списка операций
 */

public interface Cost {
    /**
     * getId проводки
     */
    long getId();

    /**
     * Кто дал денег
     */
    Member getMember();

    /**
     * Кому дал денег
     */
    Member getToMember();

    /**
     * Сколько дал денег (в копейках)
     */
    int getSum();

    /**
     * возвращает статус проводки
     */
    boolean isActive();

    /**
     * Если true то операция является возвратом долга
     */
    boolean isRepayment();

    /**
     * меняет статус проводке
     */
    void setActive(boolean value);

    /**
     * Возвращает путь к изображению привязанному к проводке
     */
    String getImageDir();

    /**
     * Возвращает дату проводки
     */
    Date getDate();

    /**
     * Возвращает комментарий к проводке
     */
    String getComment();


}
