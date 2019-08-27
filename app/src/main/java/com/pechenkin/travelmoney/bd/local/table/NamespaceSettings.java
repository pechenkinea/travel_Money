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

    String MIGRATION_COMPLETE = "MIGRATION_COMPLETE";



    String LIKE_DIAGRAM_NAME = "LIKE_DIAGRAM_NAME";


}
