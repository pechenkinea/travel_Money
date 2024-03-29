package com.pechenkin.travelmoney.list;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.dialog.EditSumDialog;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;
import com.pechenkin.travelmoney.transaction.draft.DraftTransactionItem;
import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.utils.MemberIcons;

public class TransactionList extends BaseAdapter {

    private static LayoutInflater inflater = null;

    private final DraftTransaction draftTransaction;
    private final ListView list;

    public TransactionList(Activity a, DraftTransaction draftTransaction, ListView list) {
        this.draftTransaction = draftTransaction;
        this.list = list;
        inflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public int getCount() {
        return this.draftTransaction.getDebitItems().size();
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
            return convertView;
        }

        holder.toDefault();


        View.OnClickListener editClickListener = v ->
                new EditSumDialog(draftTransactionItem.getDebit(), sum -> {
                    draftTransactionItem.setDebit(sum);
                    this.list.invalidateViews();
                });

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
            holder.editButton.setVisibility(View.VISIBLE);

            holder.check.setImageResource(R.drawable.ic_check_on_24);

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
            this.editButton.setVisibility(View.INVISIBLE);
            this.icon.setVisibility(View.VISIBLE);
            this.memberSumText.setVisibility(View.INVISIBLE);
        }

    }
}
