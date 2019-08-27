package com.pechenkin.travelmoney.page.cost.add.master;

import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.pechenkin.travelmoney.list.TransactionList;
import com.pechenkin.travelmoney.transaction.TransactionItem;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransactionItem;
import com.pechenkin.travelmoney.transaction.draft.ValidateException;
import com.pechenkin.travelmoney.utils.AfterTextWatcher;
import com.pechenkin.travelmoney.utils.DecimalDigitsInputFilter;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.TripManager;
import com.pechenkin.travelmoney.page.ListPage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.main.MainPage;
import com.pechenkin.travelmoney.utils.stream.StreamList;

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

        PageOpener.INSTANCE.open(MasterWho.class, getParam());
    }

    @Override
    protected int getPageId() {
        return R.layout.add_transaction;
    }

    @Override
    protected String getTitleHeader() {
        return MainActivity.INSTANCE.getString(R.string.costInfo);
    }

    @Override
    protected boolean fillFields() {

        DraftTransaction draftTransaction = getParam().getDraftTransaction();

        if (draftTransaction.getCreditItems().size() == 0) {
            return false;
        }

        Member creditMember = getParam().getDraftTransaction().getCreditItems().First().getMember();

        String memberCostInfoText = MainActivity.INSTANCE.getString(R.string.costMember) + " " + creditMember.getName();
        ((TextView) MainActivity.INSTANCE.findViewById(R.id.memberCostInfo))
                .setText(memberCostInfoText);

        ((TextInputEditText) MainActivity.INSTANCE.findViewById(R.id.cost_comment))
                .setText(getParam().getDraftTransaction().getComment());

        if (getParam().getDraftTransaction().getSum() > 0) {
            ((TextInputEditText) MainActivity.INSTANCE.findViewById(R.id.cost_sum))
                    .setText(Help.kopToTextRub(getParam().getDraftTransaction().getSum()).replaceAll(" ", ""));
        }

        if (getParam().getDraftTransaction().getImageUrl().length() > 0) {
            ((TextView) MainActivity.INSTANCE.findViewById(R.id.cost_dir_textView))
                    .setText(getParam().getDraftTransaction().getImageUrl());

            MainActivity.INSTANCE.findViewById(R.id.hasPhoto).setVisibility(View.VISIBLE);
        }

        if (draftTransaction.getDebitItems().size() == 0) {

            StreamList<Member> tripMembers = new StreamList<>(TripManager.INSTANCE.getActiveTrip().getActiveMembers());

            tripMembers.ForEach(member ->
                    draftTransaction.addDebitItem(new DraftTransactionItem(member, 0, 0))
            );

            draftTransaction.update();

        }


        this.list = MainActivity.INSTANCE.findViewById(getListViewId());
        this.adapter = new TransactionList(MainActivity.INSTANCE, getParam().getDraftTransaction(), list);
        this.list.setAdapter(adapter);


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

            DraftTransaction draftTransaction = getParam().getDraftTransaction();
            StreamList<TransactionItem> selectDebit = draftTransaction.getDebitItems().Filter(
                    transactionItem -> transactionItem.getDebit() > 0 || !((DraftTransactionItem) transactionItem).isChange()
            );

            if (selectDebit.size() == 0) {
                draftTransaction.getDebitItems().ForEach(transactionItem -> ((DraftTransactionItem) transactionItem).setChange(false));

            } else if (selectDebit.size() == draftTransaction.getDebitItems().size()) {
                draftTransaction.getDebitItems().ForEach(transactionItem -> ((DraftTransactionItem) transactionItem).setDebit(0));

            } else {

                draftTransaction.getDebitItems().ForEach(transactionItem -> {
                    if (transactionItem.getDebit() == 0) {
                        ((DraftTransactionItem) transactionItem).setChange(false);
                    }
                });
            }

            list.invalidateViews();


        });


        //Завершение работы с описанием
        TextInputEditText commentText = MainActivity.INSTANCE.findViewById(R.id.cost_comment);
        commentText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                Help.setActiveEditText(R.id.cost_sum, true);
                return true;
            }
            return false;
        });
        commentText.addTextChangedListener(new AfterTextWatcher(editable ->
                getParam().getDraftTransaction().setComment(editable.toString())
        ));

        final TextInputEditText cost_sum = MainActivity.INSTANCE.findViewById(R.id.cost_sum);
        cost_sum.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(6, 2)});

        cost_sum.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                Help.hideKeyboard();
                MainActivity.INSTANCE.findViewById(R.id.cost_sum).clearFocus();
                MainActivity.INSTANCE.findViewById(R.id.cost_comment).clearFocus();
                return true;
            }
            return false;
        });

        cost_sum.addTextChangedListener(new AfterTextWatcher(editable -> {
            int sum = Help.textRubToIntKop(editable.toString());
            ((DraftTransactionItem) getParam().getDraftTransaction().getCreditItems().First()).setCredit(sum);
            this.list.invalidateViews();
        }));

        getParam().getDraftTransaction().setDraftUpdateListener(() -> {

            int transactionSum = getParam().getDraftTransaction().getSum();
            int sum = Help.textRubToIntKop(getTextInputEditText(cost_sum));

            if (transactionSum != sum) {
                cost_sum.setText(Help.kopToTextRub(getParam().getDraftTransaction().getSum()).replaceAll(" ", ""));
                cost_sum.setSelection(getTextInputEditText(cost_sum).length());
            }

        });


        //Кнопка для фотографии
        FloatingActionButton photoButton = MainActivity.INSTANCE.findViewById(R.id.buttonPhoto);
        photoButton.setOnClickListener(view -> {
            Help.alert("Не реализовано");
        });

    }


    @Override
    protected int getListViewId() {
        return R.id.list_members;
    }

    @Override
    protected void onItemClick(ListView list, int position) {

        Help.hideKeyboard();
        MainActivity.INSTANCE.findViewById(R.id.cost_sum).clearFocus();
        MainActivity.INSTANCE.findViewById(R.id.cost_comment).clearFocus();


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

    @Override
    protected int getFocusFieldId() {

        if (getParam().getDraftTransaction().getComment().length() == 0) {
            return R.id.cost_comment;
        } else if (getParam().getDraftTransaction().getSum() == 0) {
            return R.id.cost_sum;
        }
        return 0;
    }

}
