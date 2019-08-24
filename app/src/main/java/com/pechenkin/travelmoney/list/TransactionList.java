package com.pechenkin.travelmoney.list;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransactionItem;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.utils.MemberIcons;

public class TransactionList extends BaseAdapter {

    private final DraftTransaction draftTransaction;
    private static LayoutInflater inflater = null;

    public TransactionList(Activity a, DraftTransaction draftTransaction) {
        this.draftTransaction = draftTransaction;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    // +1 т.к. есть строка итогов
    public int getCount() {
        return this.draftTransaction.getDebitItems().size() + 1;
    }


    public DraftTransactionItem getItem(int position) {
        if (this.draftTransaction.getDebitItems().size() > position) {
            return (DraftTransactionItem) this.draftTransaction.getDebitItems().get(position);
        } else {
            return null;
        }

    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DraftTransactionItem draftTransactionItem = getItem(position);

        if (draftTransactionItem == null) {
            holder.onlySum();
            holder.memberSumText.setText(Help.kopToTextRub(this.draftTransaction.getSum()));
            return convertView;
        }

        holder.toDefault();


        View.OnClickListener editClickListener = v -> {
            final ListView listView;
            try {
                listView = (ListView) v.getParent().getParent();
            } catch (Exception ex) {
                Help.alert(ex.getMessage());
                return;
            }

            final EditText input = new EditText(MainActivity.INSTANCE);
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            input.setText(Help.kopToTextRub(draftTransactionItem.getDebit()));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
            builder.setTitle("")
                    .setCancelable(false)
                    .setPositiveButton("Ок", (dialog, which) -> {
                        //((TextView) v).setText(input.getText());
                        draftTransactionItem.setDebit(Help.textRubToIntKop(String.valueOf(input.getText())));
                        dialog.cancel();
                        listView.invalidateViews();
                    })
                    .setNegativeButton("Отмена",
                            (dialog, id) -> dialog.cancel())
                    .setNeutralButton("По умолчанию", (dialog, which) -> {
                        draftTransactionItem.setChange(false);
                        dialog.cancel();
                        listView.invalidateViews();
                    });


            final AlertDialog alert = builder.create();
            alert.setView(input);
            if (alert.getWindow() != null)
                alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            input.setOnEditorActionListener((v1, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    draftTransactionItem.setDebit(Help.textRubToIntKop(String.valueOf(input.getText())));
                    alert.cancel();
                    listView.invalidateViews();
                    return true;
                }
                return false;
            });

            alert.show();

            Help.setActiveEditText(input, true);

        };

        holder.memberSumText.setOnClickListener(editClickListener);
        holder.editButton.setOnClickListener(editClickListener);

        Member member = draftTransactionItem.getMember();
        holder.name.setText(member.getName());

        holder.name.setTextColor(member.getColor());

        holder.icon.setVisibility(View.VISIBLE);
        holder.icon.setImageDrawable(MemberIcons.getIconById(member.getIcon()));
        holder.icon.setColorFilter(member.getColor());


        if (draftTransactionItem.getDebit() > 0 || !draftTransactionItem.isChange()) {
            holder.memberSumText.setVisibility(View.VISIBLE);

            holder.check.setImageResource(R.drawable.ic_check_on_24);

            holder.editButton.setVisibility(View.VISIBLE);

            if (draftTransactionItem.isChange()) {
                holder.memberSumText.setText(Html.fromHtml("<b>" + Help.kopToTextRub(draftTransactionItem.getDebit()) + "</b> "));

            } else {
                holder.memberSumText.setText(Help.kopToTextRub(draftTransactionItem.getDebit()));
            }

        } else {
            holder.check.setImageResource(R.drawable.ic_check_off_24);
        }


        return convertView;
    }

    static class ViewHolder {
        final TextView name;
        final ImageButton check;
        final ImageButton editButton;
        final TextView memberSumText;
        final AppCompatImageView icon;

        ViewHolder(View convertView) {
            this.name = convertView.findViewById(R.id.lm_name);
            this.check = convertView.findViewById(R.id.lm_check);
            this.editButton = convertView.findViewById(R.id.listEditButton);
            this.memberSumText = convertView.findViewById(R.id.memberSumText);
            this.icon = convertView.findViewById(R.id.icon);
        }

        void toDefault() {
            this.name.setVisibility(View.VISIBLE);
            this.check.setVisibility(View.VISIBLE);
            this.editButton.setVisibility(View.VISIBLE);
            this.icon.setVisibility(View.VISIBLE);
            this.memberSumText.setVisibility(View.INVISIBLE);
        }

        void onlySum() {
            this.name.setVisibility(View.INVISIBLE);
            this.check.setVisibility(View.INVISIBLE);
            this.editButton.setVisibility(View.INVISIBLE);
            this.icon.setVisibility(View.INVISIBLE);
            this.memberSumText.setVisibility(View.VISIBLE);
        }


    }
}
