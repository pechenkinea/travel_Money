package com.pechenkin.travelmoney.page;


import com.google.android.material.textfield.TextInputEditText;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;

/**
 * Created by pechenkin on 19.04.2018.
 * Базовый класс для страницы
 */
public abstract class BasePage implements Page {

    protected abstract int getPageId();

    protected abstract String getTitleHeader();

    protected abstract boolean fillFields();

    protected int getFocusFieldId(){
        return 0;
    }

    private PageParam param;

    protected boolean hasParam() {
        return param != null;
    }

    protected PageParam getParam() {
        if (param == null)
            return new PageParam.BuildingPageParam().getParam();

        return param;
    }

    @Override
    public void setParam(PageParam param) {
        this.param = param;
    }

    public void open() {
        //Открываем страницу
        MainActivity.INSTANCE.setContentView(getPageId());

        //Настраиваем тулбар
        MainActivity.INSTANCE.setTitle(getTitleHeader());

        //Заполняем поля
        if (!fillFields()) {
            clickBackButton();
            return;
        }

        addEvents();


        //Если есть поле, на которое надо назначить фокус ставим туда курсор и открываем клавиатуру
        if (getFocusFieldId() != 0) {
            Help.setActiveEditText(getFocusFieldId());
        } else // Скрываем клавиатуру если фокус не нужен
        {
            Help.hideKeyboard();
        }
    }

    /**
     * Что бы каждый раз не проверять на null
     */
    protected String getTextInputEditText(TextInputEditText input) {
        return input.getText() != null ? input.getText().toString().trim() : "";
    }


}
