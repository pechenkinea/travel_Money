package com.pechenkin.travelmoney.page.cost.add.master;

import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pechenkin.travelmoney.list.TransactionList;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransactionItem;
import com.pechenkin.travelmoney.transaction.draft.ValidateException;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.TripManager;
import com.pechenkin.travelmoney.list.AdapterMembersList;
import com.pechenkin.travelmoney.page.ListPage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.cost.add.data.CostMember;
import com.pechenkin.travelmoney.page.main.MainPage;
import com.pechenkin.travelmoney.utils.stream.StreamList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pechenkin on 15.05.2018.
 * Страница мастера добавления траты. За кого платил
 */

public class MasterWhom extends ListPage {


    private TransactionList adapter = null;
    private ListView list = null;

    @Override
    public void clickBackButton() {
        if (getParam().getBackPage() != null) {
            PageOpener.INSTANCE.open(getParam().getBackPage());
            return;
        }

        PageOpener.INSTANCE.open(MasterCostInfo.class, getParam());
    }

    @Override
    protected int getPageId() {
        return R.layout.view_list_members;
    }

    @Override
    protected String getTitleHeader() {
        String desc = "";
        try {
            desc = " (" + getParam().getDraftTransaction().getCreditItems().First().getMember().getName() + ")";
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return MainActivity.INSTANCE.getString(R.string.whom) + desc;
    }

    @Override
    protected boolean fillFields() {

        DraftTransaction draftTransaction = getParam().getDraftTransaction();

        if (draftTransaction.getCreditItems().size() == 0) {
            return false;
        }

        MainActivity.INSTANCE.findViewById(R.id.member_add_button).setVisibility(View.INVISIBLE);

        if (draftTransaction.getDebitItems().size() == 0) {

            StreamList<Member> tripMembers = new StreamList<>(TripManager.INSTANCE.getActiveTrip().getActiveMembers());

            tripMembers.ForEach(member ->
                    draftTransaction.addDebitItem(new DraftTransactionItem(member, 0, 0))
            );

            draftTransaction.updateSum();

        }




        this.list = MainActivity.INSTANCE.findViewById(getListViewId());
        this.adapter = new TransactionList(MainActivity.INSTANCE, getParam().getDraftTransaction());
        this.list.setAdapter(adapter);

        MainActivity.INSTANCE.findViewById(R.id.member_checkAll_button).setVisibility(View.VISIBLE);

        return true;
    }


    @Override
    public void addEvents() {
        super.addEvents();

        FloatingActionButton member_list_commit = MainActivity.INSTANCE.findViewById(R.id.member_list_commit);
        member_list_commit.setOnClickListener(v -> {

            try {
                getParam().getDraftTransaction().validate();
            } catch (ValidateException e) {
                Help.message(e.getMessage());
                return;
            }

            TripManager.INSTANCE.getActiveTrip().addTransaction(getParam().getDraftTransaction());

            Help.message(MainActivity.INSTANCE.getString(R.string.messageAddCost));
            PageOpener.INSTANCE.open(MainPage.class);

        });


        FloatingActionButton member_checkAll_button = MainActivity.INSTANCE.findViewById(R.id.member_checkAll_button);

        member_checkAll_button.setOnClickListener(v -> {

            Help.alert("не реализовано");

        });


    }


    @Override
    protected int getListViewId() {
        return R.id.list_members;
    }

    @Override
    protected void onItemClick(ListView list, int position) {

        DraftTransactionItem item = this.adapter.getItem(position);
        if (item != null) {

            if (item.getDebit() == 0 && item.isChange()) {
                item.setChange(false);
            } else {
                item.setDebit(0);
            }

        }

        list.invalidateViews();
    }


}
