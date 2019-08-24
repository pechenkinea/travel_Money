package com.pechenkin.travelmoney.speech.recognition;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.MainActivity;

import java.util.List;

/**
 * Класс-помощник для распознавания речи
 */
public class SpeechRecognitionHelper {

    /**
     * Запускает процесс распознавания. Проверяет наличие Activity для распознавания речи.
     * Если Activity нет, отправляет пользователя в маркет установить Голосовой Поиск
     * Google. Если активи для распознавания есть, то отправляет Intent для ее запуска.
     *
     * @param ownerActivity Activity, которая инициировала процесс распознавания
     */
    public static void run(Activity ownerActivity) {
        // проверяем есть ли Activity для распознавания
        if (isSpeechRecognitionActivityPresented(ownerActivity)) {
            // если есть - запускаем распознавание
            startRecognitionActivity(ownerActivity);
        } else {
            // если нет, то показываем уведомление что надо установить Голосовой Поиск
            Toast.makeText(ownerActivity, "Чтобы активировать голосовой поиск необходимо установить \"Голосовой поиск Google\"", Toast.LENGTH_LONG).show();
            // начинаем процесс установки
            installGoogleVoiceSearch(ownerActivity);
        }
    }

    /**
     * Отправляет Intent с запросом на распознавание речи
     * @param ownerActivity инициировавшая запрос Activity
     */
    private static void startRecognitionActivity(Activity ownerActivity) {

        // создаем Intent с действием RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // добавляем дополнительные параметры:
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Говорите \"Кто кому сколько\"");	// текстовая подсказка пользователю
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);	// модель распознавания оптимальная для распознавания коротких фраз-поисковых запросов
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);	// количество результатов, которое мы хотим получить, в данном случае хотим только первый - самый релевантный

        // стартуем Activity и ждем от нее результата
        ownerActivity.startActivityForResult(intent, MainActivity.VOICE_RECOGNITION_REQUEST_CODE);
    }

    /**
     * Проверяет наличие Activity способной выполнить распознавание речи
     *
     * @param ownerActivity Activity, которая запросила проверку
     * @return true - если есть, false - если такой Activity нет
     */
    private static boolean isSpeechRecognitionActivityPresented(Activity ownerActivity) {
        try {
            // получаем экземпляр менеджера пакетов
            PackageManager pm = ownerActivity.getPackageManager();
            // получаем список Activity способных обработать запрос на распознавание
            List activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

            if (activities.size() != 0) {	// если список не пустой
                return true;				// то умеем распознавать речь
            }
        } catch (Exception e) {
            e.printStackTrace();
            Help.alert(e.getMessage());
        }

        return false; // не умеем распознавать речь
    }


    /**
     * Запрашивает разрешение на установку Голосового Поиска Google, отображая диалог. Если разрешение
     * получено - направляет пользователя в маркет.
     * @param ownerActivity Activity инициировавшая установку
     */
    private static void installGoogleVoiceSearch(final Activity ownerActivity) {

        // создаем диалог, который спросит у пользователя хочет ли он
        // установить Голосовой Поиск
        // положительная кнопка
// обработчик нажатия на кнопку Установить
        Dialog dialog = new AlertDialog.Builder(ownerActivity)
                .setMessage("Для распознавания речи необходимо установить \"Голосовой поиск Google\"")	// сообщение
                .setTitle("Внимание")	// заголовок диалога
                .setPositiveButton("Установить", (dialog1, which) -> {
                    try {
                        // создаем Intent для открытия в маркете странички с приложением
                        // Голосовой Поиск имя пакета: com.google.android.voicesearch
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.voicesearch"));
                        // настраиваем флаги, чтобы маркет не попал к в историю нашего приложения (стек Activity)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        // отправляем Intent
                        ownerActivity.startActivity(intent);
                    } catch (Exception ex) {
                        // не удалось открыть маркет
                        // например из-за того что он не установлен
                        // ничего не поделаешь
                    }
                })

                .setNegativeButton("Отмена", null)	// негативная кнопка
                .create();

        dialog.show();	// показываем диалог
    }


}
