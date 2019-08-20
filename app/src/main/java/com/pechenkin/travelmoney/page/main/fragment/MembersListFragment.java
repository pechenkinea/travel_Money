package com.pechenkin.travelmoney.page.main.fragment;

import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.local.table.t_members;
import com.pechenkin.travelmoney.bd.local.table.t_trips;
import com.pechenkin.travelmoney.list.AdapterMembersList;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.cost.add.data.CostMember;
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
            long itemId = adapter.getItem(position).getMemberId();
            Member member = t_members.getMemberById(itemId);



            if (t_trips.getActiveTripNew().memberIsActive(member)) {
                t_trips.getActiveTripNew().removeMember(member);
                list.setItemChecked(position, false);
            } else {
                t_trips.getActiveTripNew().setMemberActive(member);
                list.setItemChecked(position, true);
            }
            list.invalidateViews();
        });
        list.setOnItemLongClickListener((parent, view, position, id) -> {
            AdapterMembersList adapter = (AdapterMembersList) list.getAdapter();
            long itemId = adapter.getItem(position).getMemberId();

            PageOpener.INSTANCE.open(EditMemberPage.class, new PageParam.BuildingPageParam().setId(itemId).getParam());

            return true;
        });
    }

    @Override
    public void doAfterRender() {

        Member[] members = t_trips.getActiveTripNew().getAllMembers();
        ListView list = fragmentView.findViewById(R.id.list_members);
        if (list != null) {
            if (members.length == 0) {
                Help.message(MainActivity.INSTANCE.getString(R.string.errorNoData));
                list.setAdapter(null);
            } else {

                // сортируем так, что бы те, кто в текущей поездке отображались сверху
                Arrays.sort(members, (m1, m2) -> {

                    boolean m1InTRip = t_trips.getActiveTripNew().memberIsActive(m1);
                    boolean m2InTRip = t_trips.getActiveTripNew().memberIsActive(m2);

                    if (m1InTRip && !m2InTRip){
                        return -1;
                    }
                    else if (!m1InTRip && m2InTRip){
                        return 1;
                    }

                    return Long.compare(m1.getId(), m2.getId());
                });

                AdapterMembersList adapter = new AdapterMembersList(MainActivity.INSTANCE, CostMember.createCostMemberBaseTableRow(members, 0), true);
                list.setAdapter(adapter);

                for (int i = 0; i < adapter.getCount(); i++) {
                    long m_id = adapter.getItem(i).getMemberId();
                    Member member = t_members.getMemberById(m_id);

                    if (t_trips.getActiveTripNew().memberIsActive(member)) {
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
