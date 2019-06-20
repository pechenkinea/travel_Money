package com.pechenkin.travelmoney.page.cost.add;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.pechenkin.travelmoney.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;
import com.pechenkin.travelmoney.bd.table.result.MembersQueryResult;
import com.pechenkin.travelmoney.bd.table.row.BaseTableRow;
import com.pechenkin.travelmoney.bd.table.t_costs;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_trips;
import com.pechenkin.travelmoney.list.AdapterMembersList;
import com.pechenkin.travelmoney.page.BasePage;
import com.pechenkin.travelmoney.page.MainPage;
import com.pechenkin.travelmoney.page.PageOpenner;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * Created by pechenkin on 19.04.2018.
 * Страница добавления траты
 * <p>
 * Больше не используется. теперь добавление через мастер
 */
@Deprecated
public class AddCostPage extends BasePage {
    @Override
    public void clickBackButton() {
        PageOpenner.INSTANCE.open(MainPage.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    private List<Long> list_id = new ArrayList<>();

    private void commitForm() {
        //Help.hideKeyboard();

        EditText et_comment = MainActivity.INSTANCE.findViewById(R.id.cost_comment);
        String comment = et_comment.getText().toString();
        if (comment.length() == 0) {
            Help.message("Заполните описание");
            Help.setActiveEditText(R.id.cost_comment);
            return;
        }

        Spinner spin_member = MainActivity.INSTANCE.findViewById(R.id.cost_member);

        if (spin_member.getCount() < 1) {
            Help.hideKeyboard();
            Help.message("Выберите кто платил");
            return;
        }
        String member = spin_member.getSelectedItem().toString();
        long member_id = t_members.getIdByName(member);

        if (member_id < 0) {
            Help.hideKeyboard();
            Help.message("Ошибка. Не найден участник " + member);
            return;
        }

        if (list_id.size() == 0) {
            Help.hideKeyboard();
            Help.message("Выберите \"За кого\"");
            return;
        }


        EditText et_sum = MainActivity.INSTANCE.findViewById(R.id.cost_sum);

        String sum = et_sum.getText().toString();

        if (sum.length() == 0) {
            Help.message("Введите сумму");
            Help.setActiveEditText(R.id.cost_sum);
            return;
        }


        TextView tv_dir = MainActivity.INSTANCE.findViewById(R.id.cost_dir_textView);
        String image_dir = tv_dir.getText().toString();


        double sum_on_one;
        try {
            double f_sum = Help.StringToDouble(sum);
            sum_on_one = f_sum / list_id.size();

            if (sum_on_one > 10000000) {
                Help.alert("Сумма свыше 10 000 000 на участника не поддерживается.");
                Help.setActiveEditText(R.id.cost_sum);
                return;
            }

        } catch (NumberFormatException e) {
            Help.message("Ошибка вычислений. Запись не добавлена");
            return;
        }

        Date date = new Date();
        for (long to_memmber_id : list_id) {
            t_costs.add(member_id, to_memmber_id, comment, sum_on_one, image_dir, t_trips.ActiveTrip.id, date);
        }

        Help.message("Запись добавлена. По " + Help.DoubleToString(sum_on_one));

        list_id.clear();

        PageOpenner.INSTANCE.open(MainPage.class);
    }

    @Override
    public void addEvents() {

        //Кнопак добавления
        Button commitButton = MainActivity.INSTANCE.findViewById(R.id.cost_add_button);
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitForm();
            }
        });


        final ListView list1 = MainActivity.INSTANCE.findViewById(R.id.cost_list_to_member);
        //Выбор участника из списка
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AdapterMembersList adapter = (AdapterMembersList) list1.getAdapter();
                BaseTableRow item = adapter.getItem(position).getMemberRow();

                SparseBooleanArray sbArray = list1.getCheckedItemPositions();
                if (!sbArray.get(position, false)) {
                    list1.setItemChecked(position, false);
                    list_id.removeAll(Collections.singletonList(item.id));
                } else {
                    list1.setItemChecked(position, true);
                    list_id.add(item.id);
                }
                list1.invalidateViews();
                setSum();
                Help.setActiveEditText(R.id.cost_sum);

            }
        });


        final EditText cost_sum = MainActivity.INSTANCE.findViewById(R.id.cost_sum);

        cost_sum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    commitForm();
                    return true;
                }
                return false;
            }
        });

        //Ввод суммы
        cost_sum.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int s, int b, int c) {
                setSum();
            }

            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence cs, int i, int j, int
                    k) {
            }
        });

        //Завершение работы с описанием
        EditText commentText = MainActivity.INSTANCE.findViewById(R.id.cost_comment);
        commentText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    Help.hideKeyboard();

                    Spinner member_s = MainActivity.INSTANCE.findViewById(R.id.cost_member);
                    member_s.performClick();

                    Help.setActiveEditText(R.id.cost_sum);
                    handled = true;
                }
                return handled;
            }
        });


        //Кнопка за всех
        Button checkAllButton = MainActivity.INSTANCE.findViewById(R.id.cost_check_getAll);
        checkAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListView list1 = MainActivity.INSTANCE.findViewById(R.id.cost_list_to_member);
                AdapterMembersList adapter = (AdapterMembersList) list1.getAdapter();
                list_id.clear();

                for (int i = 0; i < adapter.getCount(); i++) {
                    long m_id = adapter.getItemId(i);

                    if (t_trips.isMemberInTrip(t_trips.ActiveTrip.id, m_id)) {
                        list1.setItemChecked(i, true);
                        list_id.add(m_id);
                    }
                }

                Help.setActiveEditText(R.id.cost_sum);
                setSum();
            }
        });

        ImageButton fotoButton = MainActivity.INSTANCE.findViewById(R.id.buttonFoto);
        fotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Help.hideKeyboard();

                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    Help.alert("SD-карта не доступна: " + Environment.getExternalStorageState());
                    return;
                }

                Date now = new Date();
                File path = Environment.getExternalStorageDirectory();


                File f1 = new File(path.getAbsolutePath() + "/travel_Money");
                if (!f1.exists() && !f1.mkdirs()) {
                    Help.alert("Ошибка. Не удалось создать папку для хранения файла. " + f1.getAbsolutePath() + MainActivity.INSTANCE.getString(R.string.fileError));
                    return;
                }

                File file = new File(f1.getAbsolutePath(), now.getTime() + ".jpg");

                MainActivity.INSTANCE.outputFileUri = Uri.fromFile(file);

                if (MainActivity.INSTANCE.outputFileUri == null) {
                    Help.alert("Ошибка. Не удалось создать файл для фото.");
                    return;
                }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, MainActivity.INSTANCE.outputFileUri);

                MainActivity.INSTANCE.setResult(RESULT_OK, intent);

                MainActivity.INSTANCE.startActivityForResult(intent, MainActivity.TAKE_COST_FOTO);
            }
        });


    }

    @Override
    protected int getPageId() {
        return R.layout.add_cost;
    }

    @Override
    protected String getTitleHeader() {
        return MainActivity.INSTANCE.getString(R.string.addNewCost);
    }

    @Override
    protected boolean fillFields() {

        if (t_trips.ActiveTrip == null) {
            Help.message("Нет активных поездок");
            return false;
        }

        //кто
        final Spinner member_s = MainActivity.INSTANCE.findViewById(R.id.cost_member);
        MembersQueryResult tripMembers = t_members.getAllByTripId(t_trips.ActiveTrip.id);

        if (tripMembers.hasRows()) {
            Help.fill_cost_Spinner(tripMembers.getAllRows(), member_s);
        } else {
            Help.message("Нет участников");
            PageOpenner.INSTANCE.open(MainPage.class);
            return false;
        }

        final ListView list1 = MainActivity.INSTANCE.findViewById(R.id.cost_list_to_member);
        Help.fill_list_view(t_trips.ActiveTrip.id, list1);
        Help.setListViewHeightBasedOnChildren(list1);

        list_id.clear();


        return true;
    }

    @Override
    protected int getFocusFieldId() {
        return R.id.cost_comment;
    }

    @Override
    protected void helps() {

    }


    private void setSum() {
        EditText cost_sum = MainActivity.INSTANCE.findViewById(R.id.cost_sum);
        TextView cost_po = MainActivity.INSTANCE.findViewById(R.id.cost_tw_po);
        String str_sum = cost_sum.getText().toString();
        double sum = Help.StringToDouble(str_sum);

        if (list_id.size() != 0)
            sum = sum / list_id.size();


        str_sum = Help.DoubleToString(sum);
        cost_po.setText(str_sum);
    }
}
