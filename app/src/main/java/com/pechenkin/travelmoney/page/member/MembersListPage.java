package com.pechenkin.travelmoney.page.member;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.result.MembersQueryResult;
import com.pechenkin.travelmoney.bd.table.row.BaseTableRow;
import com.pechenkin.travelmoney.bd.table.row.MemberBaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.list.AdapterMembersList;
import com.pechenkin.travelmoney.list.CostMemberBaseTableRow;
import com.pechenkin.travelmoney.page.ListPage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.main.MainPageNew;

/**
 * Created by pechenkin on 19.04.2018.]
 * Страница с перечнем участников
 */

public class MembersListPage extends ListPage {


    @Override
    protected int getPageId() {
        return R.layout.view_list_members;
    }

    @Override
    protected String getTitleHeader() {
        return MainActivity.INSTANCE.getString(R.string.membersList) +  "(" + t_trips.ActiveTrip.name + ")";
    }

    @Override
    public void addEvents() {
        super.addEvents();

        FloatingActionButton addMemberButton = MainActivity.INSTANCE.findViewById(R.id.member_add_button);
        addMemberButton.setOnClickListener(v -> PageOpener.INSTANCE.open(AddMemderPage.class));

        FloatingActionButton member_list_commit = MainActivity.INSTANCE.findViewById(R.id.member_list_commit);
        member_list_commit.setOnClickListener(v -> PageOpener.INSTANCE.open(MainPageNew.class));

    }

    @Override
    protected boolean fillFields() {
        if (t_trips.ActiveTrip == null)
        {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoActiveTask));
            return false;
        }

        MembersQueryResult allMembers = t_members.getAll();
        ListView list1 =  MainActivity.INSTANCE.findViewById(getListViewId());
        if (!allMembers.hasRows())
        {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoData));
            list1.setAdapter(null);
        }
        else
        {
            MemberBaseTableRow[] members = allMembers.getAllRows();
            AdapterMembersList adapter = new AdapterMembersList(MainActivity.INSTANCE, CostMemberBaseTableRow.createCostMemberBaseTableRow(members, 0), true);
            list1.setAdapter(adapter);

            for (int i = 0; i < adapter.getCount(); i++)
            {
                long m_id = adapter.getItem(i).getMemberRow().id;
                if (t_trips.isMemberInTrip(t_trips.ActiveTrip.id, m_id))
                {
                    list1.setItemChecked(i, true);
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

        if (t_trips.isMemberInTrip(t_trips.ActiveTrip.id, item.id))
        {
            t_trips.removeMemberInTrip(t_trips.ActiveTrip.id, item.id);
            list.setItemChecked(position, false);
        }
        else
        {
            t_trips.addMemberInTrip(t_trips.ActiveTrip.id, item.id);
            list.setItemChecked(position, true);
        }
        list.invalidateViews();
    }

    @Override
    protected boolean onItemLongClick(ListView list, AdapterView<?> a, View view, int position, long arg3) {
        AdapterMembersList adapter =  (AdapterMembersList)list.getAdapter();
        MemberBaseTableRow item = adapter.getItem(position).getMemberRow();

        PageOpener.INSTANCE.open(EditMemderPage.class, new PageParam.BuildingPageParam().setId(item.id).getParam());

        return true;
    }


}
