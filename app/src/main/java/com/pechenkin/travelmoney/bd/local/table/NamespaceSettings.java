package com.pechenkin.travelmoney.bd.local.table;

/**
 * Created by pechenkin on 09.04.2018.
 * Список настроек
 */

public interface NamespaceSettings {


    //Подсказка о том, что можно удалять записи
    String DELETE_COST_SHOWED_HELP = "DELETE_COST_SHOWED_HELP";

    //Нужно ли дополнительно группировать траты участников с учетом  их цвета, например когда у семьи общий бюджет
    String GROUP_BY_COLOR = "GROUP_BY_COLOR";


    String TO_MEMBER_TEXT_LENGTH = "TO_MEMBER_TEXT_LENGTH";

    String NEED_MIGRATION = "NEED_MIGRATION";
    String NEED_ADD_TRIPS_UUID = "NEED_ADD_TRIPS_UUID";


    String LIKE_DIAGRAM_NAME = "LIKE_DIAGRAM_NAME";


}
