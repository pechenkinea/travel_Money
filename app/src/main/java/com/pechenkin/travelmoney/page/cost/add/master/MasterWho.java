package com.pechenkin.travelmoney.page.cost.add.master;

import android.view.View;
import android.widget.ListView;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.local.table.t_trips;
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

        MainActivity.INSTANCE.findViewById(R.id.member_add_button)
                .setVisibility(View.INVISIBLE);

         MainActivity.INSTANCE.findViewById(R.id.member_list_commit)
                .setVisibility(View.INVISIBLE);

        Member[] tripMembers = t_trips.getActiveTrip().getActiveMembers();
        ListView list1 = MainActivity.INSTANCE.findViewById(getListViewId());
        if (tripMembers.length == 0)
        {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoData));
            return false;
        }
        else
        {
            AdapterMembersList adapter = new AdapterMembersList(MainActivity.INSTANCE, CostMember.createCostMemberBaseTableRow(tripMembers, 0), false);
            adapter.setShowCheckBox(false);

            list1.setAdapter(adapter);

            if (hasParam() && getParam().getMember() != null)
            {
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (adapter.getItem(i).getMember().equals(getParam().getMember())) {
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
        Member member = adapter.getItem(position).getMember();
        PageParam param = new PageParam.BuildingPageParam(getParam()).setMember(member).getParam();
        PageOpener.INSTANCE.open(MasterCostInfo.class, param);
    }


}
