package com.pechenkin.travelmoney.dialog;

import android.app.AlertDialog;
import android.text.InputType;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.utils.Help;

public class EditSumDialog {

    public EditSumDialog(int sum, @NonNull PositiveButtonSelect positiveButtonSelect) {

        final EditText input = new EditText(MainActivity.INSTANCE);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setText(Help.kopToTextRub(sum));

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
        builder.setTitle("")
                .setCancelable(true)
                .setPositiveButton("Ок", (dialog, which) -> {
                    positiveButtonSelect.select(Help.textRubToIntKop(String.valueOf(input.getText())));
                    dialog.cancel();
                });


        final AlertDialog alert = builder.create();
        alert.setView(input);
        if (alert.getWindow() != null)
            alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        input.setOnEditorActionListener((v1, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                positiveButtonSelect.select(Help.textRubToIntKop(String.valueOf(input.getText())));
                alert.cancel();
                return true;
            }
            return false;
        });

        alert.show();

        Help.setActiveEditText(input, true);

    }


    public interface PositiveButtonSelect {
        void select(int sum);
    }


}
