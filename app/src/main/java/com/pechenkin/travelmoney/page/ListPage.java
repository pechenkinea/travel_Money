package com.pechenkin.travelmoney.page;

import android.widget.ListView;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.page.main.MainPage;

/**
 * Created by pechenkin on 19.04.2018.
 * Шблон для страниц, где только один ListView
 */

public abstract class ListPage extends BasePage {
    @Override
    public void clickBackButton() {
        PageOpener.INSTANCE.open(MainPage.class);
    }

    @Override
    protected int getFocusFieldId() {
        return 0;
    }

    protected abstract int getListViewId();
    protected abstract void onItemClick(ListView list, int position);

    @Override
    public void addEvents() {
        final ListView list = MainActivity.INSTANCE.findViewById(getListViewId());
        if (list != null)
        {
            list.setOnItemClickListener((parent, view, position, id) -> ListPage.this.onItemClick(list, position));
        }
    }


}
