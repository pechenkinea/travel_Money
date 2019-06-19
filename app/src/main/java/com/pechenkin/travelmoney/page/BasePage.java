package com.pechenkin.travelmoney.page;


import android.widget.TextView;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.NamespaceSettings;
import com.pechenkin.travelmoney.bd.table.t_settings;

/**
 * Created by pechenkin on 19.04.2018.
 * Базовый класс для страницы
 */

public abstract class BasePage implements Page {

    protected abstract int getPageId();
    protected abstract String getTitleHeader();
    protected abstract boolean fillFields();
    protected abstract int getFocusFieldId();
    protected abstract void helps();

    private PageParam param;

    protected boolean hasParam()
    {
        return param != null;
    }
    protected PageParam getParam()
    {
        if (param == null)
            return new PageParam.BuildingPageParam().getParam();

        return  param;
    }

    @Override
    public void setParam(PageParam param) {
        this.param = param;
    }

    public void open(){
        //Открываем страницу
        MainActivity.INSTANCE.setContentView(getPageId());

        //Настраиваем тулбар
        TextView toolbarTitle = (TextView)MainActivity.INSTANCE.findViewById(R.id.toolBarOutMainTitle);
        if (toolbarTitle != null)
        {
            toolbarTitle.setText(getTitleHeader());
        }

        //Заполняем поля
        if (!fillFields()) {
            clickBackButton();
            return;
        }

        addEvents();

        if (!t_settings.INSTANCE.active(NamespaceSettings.HIDE_ALL_HELP)) {
            try {
                helps();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        //Если есть поле, на которое надо назначить фокус ставим туда курсор и открываем клавиатуру
        if (getFocusFieldId() != 0) {
            Help.setActiveEditText(getFocusFieldId());
        }
        else // Скрываем клавиатуру если фокус не нужен
        {
            Help.hideKeyboard();
        }
    }


}
