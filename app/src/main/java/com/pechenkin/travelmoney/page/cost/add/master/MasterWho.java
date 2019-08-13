package com.pechenkin.travelmoney.page.cost.add.master;

import android.view.View;
import android.widget.ListView;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.query.QueryResult;
import com.pechenkin.travelmoney.bd.table.query.row.MemberTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.list.AdapterMembersList;
import com.pechenkin.travelmoney.page.ListPage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.cost.add.data.CostMember;

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
        if (t_trips.getActiveTrip() == null)
        {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoActiveTask));
            return false;
        }

        MainActivity.INSTANCE.findViewById(R.id.member_add_button)
                .setVisibility(View.INVISIBLE);

         MainActivity.INSTANCE.findViewById(R.id.member_list_commit)
                .setVisibility(View.INVISIBLE);

        QueryResult<MemberTableRow> tripMembers = t_members.getAllByTripId(t_trips.getActiveTrip().id);
        ListView list1 = MainActivity.INSTANCE.findViewById(getListViewId());
        if (!tripMembers.hasRows())
        {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoData));
            return false;
        }
        else
        {
            AdapterMembersList adapter = new AdapterMembersList(MainActivity.INSTANCE, CostMember.createCostMemberBaseTableRow(tripMembers.getAllRows(), 0), false);
            adapter.setShowCheckBox(false);

            list1.setAdapter(adapter);

            if (hasParam() && getParam().getId() > -1)
            {
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (adapter.getItem(i).getMemberId() == getParam().getId()) {
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
    protected void onItemClick(ListView list, int position) {

        AdapterMembersList adapter =  (AdapterMembersList)list.getAdapter();
        long itemId = adapter.getItem(position).getMemberId();
        PageParam param = new PageParam.BuildingPageParam(getParam()).setId(itemId).getParam();
        PageOpener.INSTANCE.open(MasterCostInfo.class, param);
    }


}
