package com.pechenkin.travelmoney.page.cost.add.master;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.result.MembersQueryResult;
import com.pechenkin.travelmoney.bd.table.row.BaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.list.AdapterMembersList;
import com.pechenkin.travelmoney.list.CostMemberBaseTableRow;
import com.pechenkin.travelmoney.page.ListPage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;

/**
 * Created by pechenkin on 15.05.2018.
 * Страница мастера добавления траты. Кто платил
 */

public class MasterWho extends ListPage {
    @Override
    protected int getPageId() {
        return R.layout.view_list_members;
    }

    @Override
    protected String getTitleHeader() {
        return MainActivity.INSTANCE.getString(R.string.who);
    }

    @Override
    protected boolean fillFields() {
        if (t_trips.ActiveTrip == null)
        {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoActiveTask));
            return false;
        }

        MainActivity.INSTANCE.findViewById(R.id.member_add_button)
                .setVisibility(View.INVISIBLE);

         MainActivity.INSTANCE.findViewById(R.id.member_list_commit)
                .setVisibility(View.INVISIBLE);

        MembersQueryResult tripMembers = t_members.getAllByTripId(t_trips.ActiveTrip.id);
        ListView list1 = MainActivity.INSTANCE.findViewById(getListViewId());
        if (!tripMembers.hasRows())
        {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoData));
            return false;
        }
        else
        {
            AdapterMembersList adapter = new AdapterMembersList(MainActivity.INSTANCE, CostMemberBaseTableRow.createCostMemberBaseTableRow(tripMembers.getAllRows(), 0), false);
            adapter.setShowCheckBox(false);

            list1.setAdapter(adapter);

            if (hasParam() && getParam().getId() > -1)
            {
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (adapter.getItem(i).getMemberRow().id == getParam().getId()) {
                        list1.setItemChecked(i, true);
                        break;
                    }
                }
            }
        }

        return true;
    }



    @Override
    protected int getListViewId() {
        return R.id.list_members;
    }

    @Override
    protected void onItemClick(ListView list, AdapterView<?> a, View view, int position, long id) {

        AdapterMembersList adapter =  (AdapterMembersList)list.getAdapter();
        BaseTableRow item = adapter.getItem(position).getMemberRow();
        PageParam param = new PageParam.BuildingPageParam(getParam()).setId(item.id).getParam();
        PageOpener.INSTANCE.open(MasterCostInfo.class, param);
    }

    @Override
    protected boolean onItemLongClick(ListView list, AdapterView<?> adapter, View view, int position, long arg3) {
        return false;
    }
}
