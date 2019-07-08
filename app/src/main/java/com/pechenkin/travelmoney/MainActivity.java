package com.pechenkin.travelmoney;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pechenkin.travelmoney.bd.DBHelper;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.page.AboutPage;
import com.pechenkin.travelmoney.page.AddCostsListPage;
import com.pechenkin.travelmoney.page.MainPage;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.SettingsPage;
import com.pechenkin.travelmoney.page.SumResultListPage;
import com.pechenkin.travelmoney.page.member.MembersListPage;
import com.pechenkin.travelmoney.page.trip.AddTripPage;
import com.pechenkin.travelmoney.page.trip.TripsListPage;
import com.pechenkin.travelmoney.speech.recognition.CostCreator;
import com.pechenkin.travelmoney.speech.recognition.SpeechRecognitionHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private DBHelper dbHelper;

    synchronized public DBHelper getDbHelper() {
        return dbHelper;
    }

    static public MainActivity INSTANCE;

    public static int TAKE_COST_FOTO = 2;
    public static int VOICE_RECOGNITION_REQUEST_CODE = 3;

    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getApplicationContext());
        INSTANCE = this;
        t_members.updateMembersCache();
        PageOpener.INSTANCE.open(MainPage.class);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!PageOpener.INSTANCE.getCurrentPage().onOptionsItemSelected(item)) {
            switch (item.getItemId()) {
                case R.id.action_trip_add:
                    PageOpener.INSTANCE.open(AddTripPage.class);
                    return true;

                case R.id.staff_peoples:
                    PageOpener.INSTANCE.open(MembersListPage.class);
                    return true;

                case R.id.staff_trips:
                    PageOpener.INSTANCE.open(TripsListPage.class);
                    return true;

                case R.id.settings:
                    PageOpener.INSTANCE.open(SettingsPage.class);
                    return true;

                case R.id.menu_about:
                    PageOpener.INSTANCE.open(AboutPage.class);
                    return true;

                case R.id.trip_summary:
                    PageOpener.INSTANCE.open(SumResultListPage.class);
                    return true;


                case R.id.test:
                    String text = "Я себе 25030 с половиной";
                    CostCreator cc = new CostCreator(text);
                    PageParam param = new PageParam.BuildingPageParam().setCostCreator(cc).getParam();
                    PageOpener.INSTANCE.open(AddCostsListPage.class, param);
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
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        ImageButton ImageButton = findViewById(R.id.toolBarBackButton);
        if (ImageButton != null) {
            ImageButton.setOnClickListener(v -> onBackPressed());
        }
    }

    @Override
    public void onBackPressed() {
        PageOpener.INSTANCE.getCurrentPage().clickBackButton();
    }


    public String photoFileUri = null;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            // если это результаты отправки на получение фото
            if (requestCode == TAKE_COST_FOTO) {
                TextView textDir = findViewById(R.id.cost_dir_textView);
                if (photoFileUri != null) {
                    textDir.setText(photoFileUri);
                    MainActivity.INSTANCE.findViewById(R.id.hasPhoto).setVisibility(View.VISIBLE);
                    photoFileUri = null;
                }
            }
            // если это результаты распознавания речи
            else if (requestCode == VOICE_RECOGNITION_REQUEST_CODE) {
                // получаем список текстовых строк - результат распознавания
                // строк может быть несколько, так как не всегда удается точно распознать речь
                // более релевантные результаты идут в начале списка
                final ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                // все, в массиве matches мы получили результаты...
                if (matches != null && matches.size() > 0) {
                    CostCreator cc = null;

                    for (int i = 0; i < matches.size(); i++) {
                        cc = new CostCreator(matches.get(i));
                        if (cc.hasCosts())
                            break;
                    }

                    if (cc.hasCosts()) {
                        PageParam param = new PageParam.BuildingPageParam().setCostCreator(cc).getParam();
                        PageOpener.INSTANCE.open(AddCostsListPage.class, param);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
                        builder.setTitle("")
                                .setMessage(String.format("Не удалось разобрать\n%s", matches.get(0)))
                                .setCancelable(false)

                                .setNeutralButton("Повторить", (dialog, which) -> {
                                    dialog.cancel();
                                    SpeechRecognitionHelper.run(MainActivity.INSTANCE);
                                })
                                .setPositiveButton("Продолжить", (dialog, which) -> {
                                    dialog.cancel();
                                    CostCreator cc1 = new CostCreator(matches.get(0));
                                    PageParam param = new PageParam.BuildingPageParam().setCostCreator(cc1).getParam();
                                    PageOpener.INSTANCE.open(AddCostsListPage.class, param);
                                })
                                .setNegativeButton("Отмена",
                                        (dialog, id) -> dialog.cancel());

                        AlertDialog alert = builder.create();
                        alert.show();

                    }

                }

            }

        }

    }


}
