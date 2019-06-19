package com.pechenkin.travelmoney;

import com.pechenkin.travelmoney.bd.DBHelper;
import com.pechenkin.travelmoney.bd.NamespaceSettings;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.bd.table.t_settings;
import com.pechenkin.travelmoney.page.AboutPage;
import com.pechenkin.travelmoney.page.AddCostsListPage;
import com.pechenkin.travelmoney.page.trip.AddTripPage;
import com.pechenkin.travelmoney.page.MainPage;
import com.pechenkin.travelmoney.page.member.MembersListPage;
import com.pechenkin.travelmoney.page.PageOpenner;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.SumResultListPage;
import com.pechenkin.travelmoney.page.trip.TripsListPage;
import com.pechenkin.travelmoney.speech.recognition.CostCreator;
import com.pechenkin.travelmoney.speech.recognition.SpeechRecognitionHelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private DBHelper dbHelper;

    synchronized public DBHelper getDbHelper() {
        return dbHelper;
    }

    static public MainActivity INSTANCE;

    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getApplicationContext());
        INSTANCE = this;
        t_members.updateMembersCache();
        PageOpenner.INSTANCE.open(MainPage.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
            try {
                @SuppressLint("PrivateApi")
                Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                m.setAccessible(true);
                m.invoke(menu, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!PageOpenner.INSTANCE.getCurrentPage().onOptionsItemSelected(item)) {
            switch (item.getItemId()) {
                case R.id.action_trip_add:
                    PageOpenner.INSTANCE.open(AddTripPage.class);
                    return true;

                case R.id.staff_peoples:
                    PageOpenner.INSTANCE.open(MembersListPage.class);
                    return true;

                case R.id.staff_trips:
                    PageOpenner.INSTANCE.open(TripsListPage.class);
                    return true;

                case R.id.hide_show_helps:
                    t_settings.INSTANCE.revertBoolean(NamespaceSettings.HIDE_ALL_HELP);
                    PageOpenner.INSTANCE.getCurrentPage().open();
                    return true;
                case R.id.group_by_color:
                    t_settings.INSTANCE.revertBoolean(NamespaceSettings.GROUP_BY_COLOR);
                    PageOpenner.INSTANCE.getCurrentPage().open();
                    return true;

                case R.id.menu_about:
                    PageOpenner.INSTANCE.open(AboutPage.class);
                    return true;

                case R.id.trip_summary:
                    PageOpenner.INSTANCE.open(SumResultListPage.class);
                    return true;


                case R.id.test:
                    String text = "Я себе 25030 с половиной";
                    CostCreator cc = new CostCreator(text);
                    PageParam param = new PageParam.BuildingPageParam().setCostCreator(cc).getParam();
                    PageOpenner.INSTANCE.open(AddCostsListPage.class, param);
                    return true;

                case R.id.testBigCostList:
                    Help.createBigCostList();
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem hide_show_helps = menu.findItem(R.id.hide_show_helps);
        if (hide_show_helps != null) {
            hide_show_helps.setTitle(t_settings.INSTANCE.active(NamespaceSettings.HIDE_ALL_HELP) ? "Включить подсказки" : "Выключить подсказки");
            hide_show_helps.setIcon(t_settings.INSTANCE.active(NamespaceSettings.HIDE_ALL_HELP) ? R.drawable.hint_off : R.drawable.hint_on);
        }

        MenuItem group_by_color = menu.findItem(R.id.group_by_color);
        if (group_by_color != null) {
            group_by_color.setTitle(t_settings.INSTANCE.active(NamespaceSettings.GROUP_BY_COLOR) ? "Выключить группировку по цветам" : "Включить группировку по цветам");
            group_by_color.setIcon(t_settings.INSTANCE.active(NamespaceSettings.GROUP_BY_COLOR) ? R.drawable.hint_on : R.drawable.hint_off);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        ImageButton ImageButton = findViewById(R.id.toolBarBackButton);
        if (ImageButton != null) {
            ImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        PageOpenner.INSTANCE.getCurrentPage().clickBackButton();
    }


    public static int TAKE_COST_FOTO = 3;
    public static int VOICE_RECOGNITION_REQUEST_CODE = 3;
    public Uri outputFileUri = null;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_COST_FOTO && resultCode == RESULT_OK) {
            TextView textDir = findViewById(R.id.cost_dir_textView);
            if (outputFileUri != null) {
                textDir.setText(outputFileUri.toString());
                MainActivity.INSTANCE.findViewById(R.id.hasFoto).setVisibility(View.VISIBLE);
            }
        }

        // если это результаты распознавания речи
        // и процесс распознавания прошел успешно
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // получаем список текстовых строк - результат распознавания
            // строк может быть несколько, так как не всегда удается точно распознать речь
            // более релевантные результаты идут в начале списка
            final ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            // все, в массиве matches мы получили результаты...
            if (matches.size() > 0) {
                CostCreator cc = null;

                for (int i = 0; i < matches.size(); i++) {
                    cc = new CostCreator(matches.get(i));
                    if (cc.hasCosts())
                        break;
                }

                if (cc != null && cc.hasCosts()) {
                    PageParam param = new PageParam.BuildingPageParam().setCostCreator(cc).getParam();
                    PageOpenner.INSTANCE.open(AddCostsListPage.class, param);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
                    builder.setTitle("")
                            .setMessage(String.format("Не удалось разобрать\n%s", matches.get(0)))
                            .setCancelable(false)

                            .setNeutralButton("Повторить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    SpeechRecognitionHelper.run(MainActivity.INSTANCE);
                                }
                            })
                            .setPositiveButton("Продолжить", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    CostCreator cc = new CostCreator(matches.get(0));
                                    PageParam param = new PageParam.BuildingPageParam().setCostCreator(cc).getParam();
                                    PageOpenner.INSTANCE.open(AddCostsListPage.class, param);
                                }
                            })
                            .setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }

            }

        }
    }


}
