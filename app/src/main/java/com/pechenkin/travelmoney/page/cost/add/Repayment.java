package com.pechenkin.travelmoney.page.cost.add;

import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.TMConst;
import com.pechenkin.travelmoney.bd.table.query.row.MemberTableRow;
import com.pechenkin.travelmoney.bd.table.t_costs;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.cost.add.listener.DateOnClickListener;
import com.pechenkin.travelmoney.page.main.MainPage;

import java.util.Date;

public class Repayment extends BasePage {


    private Date selectDate = new Date();
    private long memberId = -1;
    private long toMemberId = -1;

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
        if (sum.length() == 0 || Help.StringToDouble(sum) <= 0) {
            Help.message(MainActivity.INSTANCE.getString(R.string.errorFillSum));
            Help.setActiveEditText(R.id.cost_sum, true);
            return;
        }


        if (Help.StringToDouble(sum) > TMConst.ERROR_SUM) {
            Help.message(String.format(MainActivity.INSTANCE.getString(R.string.errorBigSum) + "", Help.doubleToString(TMConst.ERROR_SUM)));
            Help.setActiveEditText(R.id.cost_sum);
            return;
        }

        t_costs.add(memberId, toMemberId, comment, Help.StringToDouble(sum), "", t_trips.getActiveTrip().id, selectDate, true);
        PageOpener.INSTANCE.open(MainPage.class);

    }

    @Override
    protected boolean fillFields() {

        if (hasParam()) {
            memberId = getParam().getId();
            toMemberId = getParam().getToMemberId();

            MemberTableRow member = t_members.getMemberById(memberId);
            ((TextView) MainActivity.INSTANCE.findViewById(R.id.member)).setText(member.name);

            MemberTableRow toMember = t_members.getMemberById(toMemberId);
            ((TextView) MainActivity.INSTANCE.findViewById(R.id.to_member)).setText(toMember.name);

            final TextInputEditText cost_sum = MainActivity.INSTANCE.findViewById(R.id.cost_sum);
            cost_sum.setText(Help.doubleToString(getParam().getSum()).replaceAll(" ", ""));

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
