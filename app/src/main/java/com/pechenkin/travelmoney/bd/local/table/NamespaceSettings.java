package com.pechenkin.travelmoney.bd.local.table;

/**
 * Created by pechenkin on 09.04.2018.
 * Список настроек
 */

public interface NamespaceSettings {

    //Скрыть все подсказки

    //String HIDE_ALL_HELP = "HIDE_ALL_HELP"; // БОЛЬШЕ НЕ ИСПОЛЗУЕТСЯ
    String MAIN_PAGE_HELP_ADD_COST_BUTTON = "MAIN_PAGE_HELP_ADD_COST_BUTTON";
    String MAIN_PAGE_HELP_ADD_MEMBERS = "MAIN_PAGE_HELP_ADD_MEMBERS";
    String MEMBERS_LIST_HELP = "MEMBERS_LIST_HELP";
    String TRIPS_LIST_HELP = "TRIPS_LIST_HELP";

    //Подсказка о том, что можно удалять записи
    String DELETE_COST_SHOWED_HELP = "DELETE_COST_SHOWED_HELP";

    //Нужно ли дополнительно группировать траты участников с учетом  их цвета, например когда у семьи общий бюджет
    String GROUP_BY_COLOR = "GROUP_BY_COLOR";

    //Нужно ли группировать проводки в общем списке
    //String GROUP_COST = "GROUP_COST"; // БОЛЬШЕ НЕ ИСПОЛЗУЕТСЯ
    //String GROUP_COST_NEED_MESSAGE = "GROUP_COST_NEED_MESSAGE"; // БОЛЬШЕ НЕ ИСПОЛЗУЕТСЯ

    String TO_MEMBER_TEXT_LENGTH = "TO_MEMBER_TEXT_LENGTH";



    String LIKE_DIAGRAM_NAME = "LIKE_DIAGRAM_NAME";


}