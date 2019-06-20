package com.pechenkin.travelmoney.cost;

import java.util.Date;

/**
 * Created by pechenkin on 06.04.2018.
 * Интерфейс для элемента списка операций
 */

public interface Cost {
    /**
     * id проводки
     */
    long id();

    /**
     * Кто дал денег
     */
    long member();

    /**
     * Кому дал денег
     */
    long to_member();

    /**
     * Сколько дал денег
     */
    double sum();

    /**
     * возвращает статус проводки
     * @return 0 - проводка удалена, 1 - проводка активна
     */
    long active();

    /**
     * меняет статус проводке
     */
    void setActive(int i);

    /**
     * Возвращает путь к изображению привязанному к проводке
     */
    String image_dir();

    /**
     * Возвращает дату проводки
     */
    Date date();

    /**
     * Возвращает комментарий к проводке
     */
    String comment();

    /**
     * Задает id группы
     */
    void setGroupId(int groupId);

    /**
     * возвращает id группы
     */
    int getGroupId();


}
