package com.pechenkin.travelmoney.page.cost.add;

import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransactionItem;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.TMConst;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.TripManager;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.cost.add.listener.DateOnClickListener;
import com.pechenkin.travelmoney.page.main.MainPage;

import java.util.Date;

public class Repayment extends BasePage {


    private Date selectDate = new Date();

    @Override
    public void clickBackButton() {
        PageOpener.INSTANCE.open(MainPage.class);
    }


    private void commitForm() {

        TextInputEditText et_comment = MainActivity.INSTANCE.findViewById(R.id.cost_comment);
        String comment = getTextInputEditText(et_comment);
        if (comment.length() == 0) {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorFillDescription));
            Help.setActiveEditText(R.id.cost_comment);
            return;
        }

        TextInputEditText et_sum = MainActivity.INSTANCE.findViewById(R.id.cost_sum);
        String sum = getTextInputEditText(et_sum);
        if (sum.length() == 0 || Help.textRubToIntKop(sum) <= 0) {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorFillSum));
            Help.setActiveEditText(R.id.cost_sum, true);
            return;
        }

        int sumInt = Help.textRubToIntKop(sum);

        if (sumInt > TMConst.ERROR_SUM) {
            Help.message(String.format(MainActivity.INSTANCE.getString(R.string.errorBigSum) + "", Help.kopToTextRub(TMConst.ERROR_SUM)));
            Help.setActiveEditText(R.id.cost_sum);
            return;
        }

        DraftTransaction draftTransaction = getParam().getDraftTransaction();
        draftTransaction.setDate(selectDate)
                .setComment(comment);

        if (sumInt != draftTransaction.getSum()) {
            ((DraftTransactionItem) draftTransaction.getCreditItems().get(0)).setCredit(sumInt);
        }

        TripManager.INSTANCE.getActiveTrip().addTransaction(draftTransaction);
        PageOpener.INSTANCE.open(MainPage.class);

    }

    @Override
    protected boolean fillFields() {

        if (hasParam()) {
            Member member = getParam().getDraftTransaction().getCreditItems().First().getMember();
            Member toMember = getParam().getDraftTransaction().getDebitItems().First().getMember();

            ((TextView) MainActivity.INSTANCE.findViewById(R.id.member)).setText(member.getName());

            ((TextView) MainActivity.INSTANCE.findViewById(R.id.to_member)).setText(toMember.getName());

            final TextInputEditText cost_sum = MainActivity.INSTANCE.findViewById(R.id.cost_sum);
            cost_sum.setText(Help.kopToTextRub(getParam().getDraftTransaction().getSum()).replaceAll(" ", ""));

            ((TextView) MainActivity.INSTANCE.findViewById(R.id.textDate)).setText(Help.dateToDateTimeStr(selectDate));

            return true;
        }
        return false;
    }


    @Override
    public void addEvents() {


        FloatingActionButton commit_button = MainActivity.INSTANCE.findViewById(R.id.commit_button);
        commit_button.setOnClickListener(v -> commitForm());

        //Завершение работы с описанием
        TextInputEditText commentText = MainActivity.INSTANCE.findViewById(R.id.cost_comment);
        commentText.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                Help.setActiveEditText(R.id.cost_sum, true);
                return true;
            }
            return false;
        });

        //Кнопка "готово" на сумме
        final TextInputEditText cost_sum = MainActivity.INSTANCE.findViewById(R.id.cost_sum);
        cost_sum.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                commitForm();
                return true;
            }
            return false;
        });


        TextInputEditText textDate = MainActivity.INSTANCE.findViewById(R.id.textDate);
        textDate.setOnClickListener(new DateOnClickListener(selectDate, v -> this.selectDate = v));
    }


    @Override
    protected int getPageId() {
        return R.layout.repayment_cost;
    }

    @Override
    protected String getTitleHeader() {
        return "Возврат долга";
    }

    @Override
    protected int getFocusFieldId() {
        return R.id.cost_sum;
    }
}
