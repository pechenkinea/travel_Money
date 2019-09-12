package com.pechenkin.travelmoney;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import androidx.appcompat.app.AppCompatActivity;

import com.pechenkin.travelmoney.bd.local.helper.DBHelper;
import com.pechenkin.travelmoney.bd.local.helper.update.Migrate;
import com.pechenkin.travelmoney.bd.local.helper.update.TripsUpdate;
import com.pechenkin.travelmoney.bd.local.table.NamespaceSettings;
import com.pechenkin.travelmoney.bd.local.table.TableSettings;
import com.pechenkin.travelmoney.page.PageOpener;
import com.pechenkin.travelmoney.page.PageParam;
import com.pechenkin.travelmoney.page.cost.add.master.MasterWhom;
import com.pechenkin.travelmoney.page.main.MainPage;
import com.pechenkin.travelmoney.page.trip.AddTripPage;
import com.pechenkin.travelmoney.speech.recognition.CostCreator;
import com.pechenkin.travelmoney.speech.recognition.SpeechRecognitionHelper;
import com.pechenkin.travelmoney.utils.RunWithProgressBar;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private RefreshActon refreshActon = null;


    public void setRefreshActon(RefreshActon refreshActon) {
        this.refreshActon = refreshActon;
    }

    public void refresh() {
        if (refreshActon != null) {
            refreshActon.refresh();
        }
    }

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




        new RunWithProgressBar<>(
                () -> {
                    dbHelper = new DBHelper(getApplicationContext());

                    if (TableSettings.INSTANCE.active(NamespaceSettings.NEED_MIGRATION)) {
                        Migrate.costToTransaction();
                        TableSettings.INSTANCE.setActive(NamespaceSettings.NEED_MIGRATION, false);
                    }

                    if (TableSettings.INSTANCE.active(NamespaceSettings.NEED_ADD_TRIPS_UUID)) {
                        TripsUpdate.updateUUID();
                        TableSettings.INSTANCE.setActive(NamespaceSettings.NEED_ADD_TRIPS_UUID, false);
                    }

                    return null;
                },
                o -> {


                    Intent intent = getIntent();
                    Uri data = intent.getData();

                    if (data == null){
                        PageOpener.INSTANCE.open(MainPage.class);
                    }
                    else {
                        PageOpener.INSTANCE.open(AddTripPage.class, new PageParam().setUri(data));
                    }

                    /*
                    //CostCreator c = new CostCreator("Я за всех 100 магазин", "");
                    CostCreator c = new CostCreator("Я Пете 100 магазин", "");
                    PageParam param = new PageParam().setDraftTransaction(c.getDraftTransaction()).setBackPage(MainPage.class);
                    PageOpener.INSTANCE.open(MasterWhom.class, param);
                    */
                });
    }

    @Override
    public void onBackPressed() {
        PageOpener.INSTANCE.getCurrentPage().clickBackButton();
    }


    public static final int TAKE_COST_PHOTO = 2;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 3;

    private PhotoComplete photoComplete = null;

    public void setPhotoComplete(PhotoComplete photoComplete) {
        this.photoComplete = photoComplete;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // если это результаты отправки на получение фото
        if (requestCode == TAKE_COST_PHOTO) {
            if (resultCode == RESULT_OK) {
                if (photoComplete != null) {
                    photoComplete.run();
                }
            }
            photoComplete = null;
        }

        // если это результаты распознавания речи
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // получаем список текстовых строк - результат распознавания
                // строк может быть несколько, так как не всегда удается точно распознать речь
                // более релевантные результаты идут в начале списка
                final ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                // все, в массиве matches мы получили результаты...
                if (matches != null && matches.size() > 0) {
                    CostCreator cc = null;

                    for (int i = 0; i < matches.size(); i++) {
                        cc = new CostCreator(matches.get(i), "");
                        if (cc.getDraftTransaction().getCreditItems().size() > 0)
                            break;
                    }

                    if (cc.getDraftTransaction().getCreditItems().size() > 0) {
                        PageParam param = new PageParam().setDraftTransaction(cc.getDraftTransaction()).setBackPage(MainPage.class);
                        PageOpener.INSTANCE.open(MasterWhom.class, param);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.INSTANCE);
                        builder.setTitle("")
                                .setMessage(String.format("Не удалось разобрать\n%s", matches.get(0)))
                                .setCancelable(false)

                                .setPositiveButton("Повторить", (dialog, which) -> {
                                    dialog.cancel();
                                    SpeechRecognitionHelper.run(MainActivity.INSTANCE);
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


    public interface RefreshActon {
        void refresh();
    }

    public interface PhotoComplete {
        void run();
    }
}
