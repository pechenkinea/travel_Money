package com.pechenkin.travelmoney.page.cost.add.master;

import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.pechenkin.travelmoney.page.main.MainPage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pechenkin on 15.05.2018.
 * Страница мастера добавления траты. За кого платил
 */

public class MasterWhom extends ListPage {

    @Override
    public void clickBackButton() {

        PageParam.BuildingPageParam param = new PageParam.BuildingPageParam(getParam());
        param.setSelectedMembers(getSelectedMembers());
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
            desc = " (" + getParam().getMember().getName() + " " + Help.doubleToString(getParam().getSum()) + ")";
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return MainActivity.INSTANCE.getString(R.string.whom) + desc;
    }

    @Override
    protected boolean fillFields() {

        if (!hasParam() || getParam().getSum() == 0 || getParam().getMember() == null || getParam().getSum() < 0)
        {
            return false;
        }


        MainActivity.INSTANCE.findViewById(R.id.member_add_button)
                .setVisibility(View.INVISIBLE);

        List<Member> tripMembers = t_trips.getActiveTrip().getActiveMembers();
        ListView list1 =  MainActivity.INSTANCE.findViewById(getListViewId());
        if (tripMembers.size() == 0)
        {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoData));
            return false;
        }
        else
        {
            AdapterMembersList adapter = new AdapterMembersList(MainActivity.INSTANCE, CostMember.createCostMemberBaseTableRow(tripMembers, getParam().getSum()), getParam().getSum());
            list1.setAdapter(adapter);

            if (hasParam())
            {
                if (getParam().getSelectedMembers().size() > 0)
                {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        if (getParam().getSelectedMembers().contains(adapter.getItem(i).getMember())) {
                            list1.setItemChecked(i, true);
                        }
                    }
                }
                else if (getParam().getMember() != null)
                {
                    for (int i = 0; i < adapter.getCount(); i++) {
                        if (adapter.getItem(i).getMember().equals(getParam().getMember())) {
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

    private Set<Member> getSelectedMembers()
    {
        Set<Member> list_id = new HashSet<>();
        ListView list = MainActivity.INSTANCE.findViewById(getListViewId());
        SparseBooleanArray sbArray = list.getCheckedItemPositions();

        ListAdapter adapter = list.getAdapter();

        for (int i = 0; i < sbArray.size(); i++) {
            int key = sbArray.keyAt(i);
            if (sbArray.get(key)) {
                Member item = (Member) adapter.getItem(key);
                if (item != null) {
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
            Set<Member> list_members = getSelectedMembers();

            if (list_members.size() == 0)
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
                        t_trips.getActiveTrip().addCost(getParam().getMember(), item.getMember(), getParam().getName(), item.getSum(), getParam().getPhotoUrl(), getParam().getSelectDate(), false);
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
