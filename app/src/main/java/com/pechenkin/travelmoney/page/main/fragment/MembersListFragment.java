package com.pechenkin.travelmoney.page.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.pechenkin.travelmoney.page.member.EditMemderPage;

public class MembersListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_members_list, container, false);

        if (t_trips.ActiveTrip == null) {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoActiveTask));
            return fragmentView;
        }

        MembersQueryResult allMembers = t_members.getAll();
        ListView list = fragmentView.findViewById(R.id.list_members);
        if (list != null) {
            if (!allMembers.hasRows()) {
                Help.message(MainActivity.INSTANCE.getString(R.string.errorNoData));
                list.setAdapter(null);
            } else {
                MemberBaseTableRow[] members = allMembers.getAllRows();
                AdapterMembersList adapter = new AdapterMembersList(MainActivity.INSTANCE, CostMemberBaseTableRow.createCostMemberBaseTableRow(members, 0), true);
                list.setAdapter(adapter);

                for (int i = 0; i < adapter.getCount(); i++) {
                    long m_id = adapter.getItem(i).getMemberRow().id;
                    if (t_trips.isMemberInTrip(t_trips.ActiveTrip.id, m_id)) {
                        list.setItemChecked(i, true);
                    }
                }
            }


            list.setOnItemClickListener((parent, view, position, id) -> {

                        AdapterMembersList adapter = (AdapterMembersList) list.getAdapter();
                        BaseTableRow item = adapter.getItem(position).getMemberRow();

                        if (t_trips.isMemberInTrip(t_trips.ActiveTrip.id, item.id)) {
                            t_trips.removeMemberInTrip(t_trips.ActiveTrip.id, item.id);
                            list.setItemChecked(position, false);
                        } else {
                            t_trips.addMemberInTrip(t_trips.ActiveTrip.id, item.id);
                            list.setItemChecked(position, true);
                        }
                        list.invalidateViews();
                    }

            );

            list.setOnItemLongClickListener((parent, view, position, id) -> {
                AdapterMembersList adapter = (AdapterMembersList) list.getAdapter();
                MemberBaseTableRow item = adapter.getItem(position).getMemberRow();

                PageOpener.INSTANCE.open(EditMemderPage.class, new PageParam.BuildingPageParam().setId(item.id).getParam());

                return true;
            });

        }


        return fragmentView;

    }

}
