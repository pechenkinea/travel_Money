package com.pechenkin.travelmoney.page.cost.add.master;

import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.query.member.MembersQueryResult;
import com.pechenkin.travelmoney.bd.table.t_costs;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.list.AdapterMembersList;
import com.pechenkin.travelmoney.page.ListPage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.main.MainPage;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by pechenkin on 15.05.2018.
 * Страница мастера добавления траты. За кого платил
 */

public class MasterWhom extends ListPage {

    @Override
    public void clickBackButton() {

        PageParam.BuildingPageParam param = new PageParam.BuildingPageParam(getParam());
        param.setSelectedIds(getSelectedIds());
        PageOpener.INSTANCE.open(MasterCostInfo.class, param.getParam());
    }

    @Override
    protected int getPageId() {
        return R.layout.view_list_members;
    }

    @Override
    protected String getTitleHeader() {
        String desc = "";
        try
        {
            desc = " (" + t_members.getMemberById(getParam().getId()).name + " " + Help.doubleToString(getParam().getSum()) + ")";
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return MainActivity.INSTANCE.getString(R.string.whom) + desc;
    }

    @Override
    protected boolean fillFields() {
        if (t_trips.ActiveTrip == null)
        {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoActiveTask));
            return false;
        }

        if (!hasParam() || getParam().getSum() == 0 || getParam().getId() < 0 || getParam().getSum() < 0)
        {
            return false;
        }


        MainActivity.INSTANCE.findViewById(R.id.member_add_button)
                .setVisibility(View.INVISIBLE);

        MembersQueryResult tripMembers = t_members.getAllByTripId(t_trips.ActiveTrip.id);
        ListView list1 =  MainActivity.INSTANCE.findViewById(getListViewId());
        if (!tripMembers.hasRows())
        {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoData));
            return false;
        }
        else
        {
            AdapterMembersList adapter = new AdapterMembersList(MainActivity.INSTANCE, CostMember.createCostMemberBaseTableRow(tripMembers.getAllRows(), getParam().getSum()), getParam().getSum());
            list1.setAdapter(adapter);

            if (hasParam())
            {
                if (getParam().getSelectedIds().size() > 0)
                {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        if (getParam().getSelectedIds().contains(adapter.getItem(i).getMemberRow().id)) {
                            list1.setItemChecked(i, true);
                        }
                    }
                }
                else if (getParam().getId() > -1)
                {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        if (adapter.getItem(i).getMemberRow().id == getParam().getId()) {
                            list1.setItemChecked(i, true);
                            break;
                        }
                    }
                }
            }

        }

        MainActivity.INSTANCE.findViewById(R.id.member_checkAll_button).setVisibility(View.VISIBLE);

        return true;
    }

    private Set<Long> getSelectedIds()
    {
        Set<Long> list_id = new HashSet<>();
        ListView list = MainActivity.INSTANCE.findViewById(getListViewId());
        SparseBooleanArray sbArray = list.getCheckedItemPositions();

        ListAdapter adapter = list.getAdapter();

        for (int i = 0; i < sbArray.size(); i++) {
            int key = sbArray.keyAt(i);
            if (sbArray.get(key)) {
                long item = adapter.getItemId(key);
                if (item >= 0) {
                    list_id.add(item);
                }
            }
        }
        return  list_id;
    }

    @Override
    public void addEvents() {
        super.addEvents();

        FloatingActionButton member_list_commit = MainActivity.INSTANCE.findViewById(R.id.member_list_commit);
        member_list_commit.setOnClickListener(v -> {

            //Кому
            Set<Long> list_id = getSelectedIds();

            if (list_id.size() == 0)
            {
                Help.message(MainActivity.INSTANCE.getString(R.string.errorFillMembers));
                return;
            }



            ListView list = MainActivity.INSTANCE.findViewById(getListViewId());
            SparseBooleanArray sbArray = list.getCheckedItemPositions();
            AdapterMembersList adapter = (AdapterMembersList)list.getAdapter();

            for (int i = 0; i < sbArray.size(); i++) {
                int key = sbArray.keyAt(i);
                if (sbArray.get(key)) {
                    CostMember item = adapter.getItem(key);
                    if (item != null && item.getSum() > 0) {
                        t_costs.add(getParam().getId(), item.getMemberRow().id, getParam().getName(), item.getSum(), getParam().getPhotoUrl(), t_trips.ActiveTrip.id, getParam().getSelectDate());
                    }
                }
            }

            Help.message(MainActivity.INSTANCE.getString(R.string.messageAddCost));
            PageOpener.INSTANCE.open(MainPage.class);

        });




        FloatingActionButton member_checkAll_button = MainActivity.INSTANCE.findViewById(R.id.member_checkAll_button);

        member_checkAll_button.setOnClickListener(v -> {

            ListView list = MainActivity.INSTANCE.findViewById(getListViewId());
            AdapterMembersList adapter = (AdapterMembersList)list.getAdapter();

            SparseBooleanArray sbArray = list.getCheckedItemPositions();

            boolean hasUnchecked = sbArray.size() == adapter.getCount();
            if (hasUnchecked) {
                for (int i = 0; i < sbArray.size(); i++) {
                    int key = sbArray.keyAt(i);
                    if (!sbArray.get(key)) {
                        hasUnchecked = false;
                        break;
                    }
                }
            }

            for (int i = 0; i < adapter.getCount(); i++)
            {
                list.setItemChecked(i, !hasUnchecked);
            }

        });



    }


    @Override
    protected int getListViewId() {
        return R.id.list_members;
    }

    @Override
    protected void onItemClick(ListView list, int position) {

        SparseBooleanArray sbArray = list.getCheckedItemPositions();
        AdapterMembersList adapter = (AdapterMembersList)list.getAdapter();
        if (sbArray.get(position, false))
        {
            list.setItemChecked(position, true);
            adapter.getItem(position).setChange(false);
        }
        else
        {
            list.setItemChecked(position, false);
        }
        list.invalidateViews();
    }


}
