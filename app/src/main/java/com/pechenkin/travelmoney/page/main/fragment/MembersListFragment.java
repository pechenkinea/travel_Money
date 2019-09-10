package com.pechenkin.travelmoney.page.main.fragment;

import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.TripManager;
import com.pechenkin.travelmoney.list.AdapterMembersList;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.member.AddMemberPage;
import com.pechenkin.travelmoney.page.member.EditMemberPage;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.utils.RunWithProgressBar;

import java.util.Collections;
import java.util.List;

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
            Member member = adapter.getItem(position);


            boolean isActive = TripManager.INSTANCE.getActiveTrip().memberIsActive(member);
            TripManager.INSTANCE.getActiveTrip().setMemberActive(member, !isActive);
            list.setItemChecked(position, !isActive);

            list.invalidateViews();
        });
        list.setOnItemLongClickListener((parent, view, position, id) -> {
            AdapterMembersList adapter = (AdapterMembersList) list.getAdapter();
            Member member = adapter.getItem(position);

            PageOpener.INSTANCE.open(EditMemberPage.class, new PageParam().setMember(member));

            return true;
        });
    }

    @Override
    public void doAfterRender() {


        new RunWithProgressBar<>(
                () -> TripManager.INSTANCE.getActiveTrip().getAllMembers(),
                members -> {

                    ListView list = fragmentView.findViewById(R.id.list_members);
                    if (list != null) {
                        if (members.size() == 0) {
                            Help.message(MainActivity.INSTANCE.getString(R.string.errorNoData));
                            list.setAdapter(null);
                        } else {

                            // сортируем так, что бы те, кто в текущей поездке отображались сверху
                            Collections.sort(members, (m1, m2) -> {

                                boolean m1InTRip = TripManager.INSTANCE.getActiveTrip().memberIsActive(m1);
                                boolean m2InTRip = TripManager.INSTANCE.getActiveTrip().memberIsActive(m2);

                                if (m1InTRip && !m2InTRip) {
                                    return -1;
                                } else if (!m1InTRip && m2InTRip) {
                                    return 1;
                                }

                                return Long.compare(m1.getId(), m2.getId());
                            });


                            AdapterMembersList adapter = new AdapterMembersList(MainActivity.INSTANCE, members, true);
                            list.setAdapter(adapter);

                            for (int i = 0; i < adapter.getCount(); i++) {
                                Member member = adapter.getItem(i);

                                if (TripManager.INSTANCE.getActiveTrip().memberIsActive(member)) {
                                    list.setItemChecked(i, true);
                                }
                            }
                        }
                    }

                });
    }

    @Override
    int[] getButtons() {
        return new int[]{
                R.id.member_add_button
        };
    }
}
