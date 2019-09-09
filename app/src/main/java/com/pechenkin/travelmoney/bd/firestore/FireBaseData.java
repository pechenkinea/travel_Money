package com.pechenkin.travelmoney.bd.firestore;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class FireBaseData {


    public static  <T> T getSync(@NonNull Task<T> task) {

        CountDownLatch done = new CountDownLatch(1);

        AtomicReference<T> result = new AtomicReference<>();

        //Куча костылей, для того, что бы заставить fireBase работать синхронно
        new Thread(() -> {
            try {
                result.set(Tasks.await(task));
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            done.countDown();
        }).start();


        try {
            done.await(); //it will wait till the response is received from firebase.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result.get();

    }

}
