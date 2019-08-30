package com.pechenkin.travelmoney.page.cost.add;

import android.text.InputFilter;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.TripManager;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.main.MainPage;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransactionItem;
import com.pechenkin.travelmoney.utils.AfterTextWatcher;
import com.pechenkin.travelmoney.utils.DecimalDigitsInputFilter;
import com.pechenkin.travelmoney.utils.Help;

public class Repayment extends BasePage {


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


        DraftTransaction draftTransaction = getParam().getDraftTransaction();

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
        cost_sum.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(6, 2)});

        cost_sum.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                commitForm();
                return true;
            }
            return false;
        });

        cost_sum.addTextChangedListener(new AfterTextWatcher(editable -> {
            int sum = Help.textRubToIntKop(editable.toString());
            ((DraftTransactionItem) getParam().getDraftTransaction().getCreditItems().First()).setCredit(sum);
        }));


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
