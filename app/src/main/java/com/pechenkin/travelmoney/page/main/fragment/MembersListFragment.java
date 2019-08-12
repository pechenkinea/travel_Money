package com.pechenkin.travelmoney.page.main.fragment;

import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.query.QueryResult;
import com.pechenkin.travelmoney.bd.table.query.IdAndNameTableRow;
import com.pechenkin.travelmoney.bd.table.query.row.MemberTableRow;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.list.AdapterMembersList;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.cost.add.master.CostMember;
import com.pechenkin.travelmoney.page.member.AddMemberPage;
import com.pechenkin.travelmoney.page.member.EditMemberPage;

import java.util.Arrays;

public class MembersListFragment extends BaseMainPageFragment {

    @Override
    int getViewId() {
        return R.layout.fragment_members_list;
    }

    @Override
    void setListeners() {

        FloatingActionButton addMemberButton = fragmentView.findViewById(R.id.member_add_button);
        addMemberButton.setOnClickListener(v -> PageOpener.INSTANCE.open(AddMemberPage.class));

        ListView list = fragmentView.findViewById(R.id.list_members);
        list.setOnItemClickListener((parent, view, position, id) -> {

            AdapterMembersList adapter = (AdapterMembersList) list.getAdapter();
            IdAndNameTableRow item = adapter.getItem(position).getMemberRow();

            if (t_trips.isMemberInTrip(t_trips.getActiveTrip().id, item.id)) {
                t_trips.removeMemberInTrip(t_trips.getActiveTrip().id, item.id);
                list.setItemChecked(position, false);
            } else {
                t_trips.addMemberInTrip(t_trips.getActiveTrip().id, item.id);
                list.setItemChecked(position, true);
            }
            list.invalidateViews();
        });
        list.setOnItemLongClickListener((parent, view, position, id) -> {
            AdapterMembersList adapter = (AdapterMembersList) list.getAdapter();
            MemberTableRow item = adapter.getItem(position).getMemberRow();

            PageOpener.INSTANCE.open(EditMemberPage.class, new PageParam.BuildingPageParam().setId(item.id).getParam());

            return true;
        });
    }

    @Override
    public void doAfterRender() {

        QueryResult<MemberTableRow> allMembers = t_members.getAll();
        ListView list = fragmentView.findViewById(R.id.list_members);
        if (list != null) {
            if (!allMembers.hasRows()) {
                Help.message(MainActivity.INSTANCE.getString(R.string.errorNoData));
                list.setAdapter(null);
            } else {
                MemberTableRow[] members = allMembers.getAllRows();

                // сортируем так, что бы те, кто в текущей поездке отображались сверху
                Arrays.sort(members, (m1, m2) -> {

                    boolean m1InTRip = m1.inTrip(t_trips.getActiveTrip().id);
                    boolean m2InTRip = m2.inTrip(t_trips.getActiveTrip().id);

                    if (m1InTRip && !m2InTRip){
                        return -1;
                    }
                    else if (!m1InTRip && m2InTRip){
                        return 1;
                    }

                    return Long.compare(m1.id, m2.id);
                });

                AdapterMembersList adapter = new AdapterMembersList(MainActivity.INSTANCE, CostMember.createCostMemberBaseTableRow(members, 0), true);
                list.setAdapter(adapter);

                for (int i = 0; i < adapter.getCount(); i++) {
                    long m_id = adapter.getItem(i).getMemberRow().id;
                    if (t_trips.isMemberInTrip(t_trips.getActiveTrip().id, m_id)) {
                        list.setItemChecked(i, true);
                    }
                }
            }
        }

    }

    @Override
    int[] getButtons() {
        return new int[]{
                R.id.member_add_button
        };
    }
}
