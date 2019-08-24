package com.pechenkin.travelmoney.transaction.adapter;

import android.graphics.Color;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;

public interface CostListItem {

    String DISABLE_COLOR_STR = MainActivity.INSTANCE.getString(R.string.disableCost);
    int DISABLE_COLOR = Color.parseColor(DISABLE_COLOR_STR);

    /**
     * задает настройки view для отображения траты в списке операций
     */
    void render(ListItemSummaryViewHolder listItemSummaryViewHolder);


    /**
     * Определяет, нужна ли анимация при клике на строку
     */
    boolean isClicked();


    /**
     * Действие при долгом нажатии на строку
     * Если действие есть надо вернуть true
     */
    boolean onLongClick();


}
