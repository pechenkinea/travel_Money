package com.pechenkin.travelmoney;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pechenkin.travelmoney.bd.DBHelper;
import com.pechenkin.travelmoney.bd.table.t_members;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.cost.add.AddCostsListPage;
import com.pechenkin.travelmoney.page.main.MainPage;
import com.pechenkin.travelmoney.speech.recognition.CostCreator;
import com.pechenkin.travelmoney.speech.recognition.SpeechRecognitionHelper;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private DBHelper dbHelper;

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    static public MainActivity INSTANCE;

    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        INSTANCE = this;
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getApplicationContext());
        t_members.updateMembersCache();
        //PageOpener.INSTANCE.open(MainPage.class);

        CostCreator c = new CostCreator("Я за всех 500 магазин");
        PageParam param = new PageParam.BuildingPageParam().setCostCreator(c).getParam();
        PageOpener.INSTANCE.open(AddCostsListPage.class, param);

    }

    @Override
    public void onBackPressed() {
        PageOpener.INSTANCE.getCurrentPage().clickBackButton();
    }


    public String photoFileUri = null;

    public static final int TAKE_COST_PHOTO = 2;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 3;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {

            // если это результаты отправки на получение фото
            if (requestCode == TAKE_COST_PHOTO) {
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
