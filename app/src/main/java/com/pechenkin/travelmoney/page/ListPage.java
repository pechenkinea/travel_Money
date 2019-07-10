package com.pechenkin.travelmoney.page;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
    @Override
    protected int getFocusFieldId() {
        return 0;
    }

    protected abstract int getListViewId();
    protected abstract void onItemClick(ListView list, AdapterView<?> adapter, View view, int position, long id);
    protected abstract  boolean onItemLongClick(ListView list, AdapterView<?> adapter, View view, int position, long arg3);

    @Override
    public void addEvents() {
        final ListView list = MainActivity.INSTANCE.findViewById(getListViewId());
        if (list != null)
        {
            list.setOnItemClickListener((parent, view, position, id) -> ListPage.this.onItemClick(list, parent, view, position, id));

            list.setOnItemLongClickListener((parent, view, position, id) -> ListPage.this.onItemLongClick(list, parent, view, position, id));
        }
    }


}
