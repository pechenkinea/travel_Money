package com.pechenkin.travelmoney.metering;

import android.util.Log;

import java.util.Date;


/**
 * Created by pechenkin on 09.04.2018.
 */

public class TimeMeter {

    private String name;
    private long startTime;
    public TimeMeter(String name)
    {
        this.name = name;
        startTime = System.nanoTime();
    }

    public void stop()
    {
        Log.i("TimeMeter " + name, "" + (System.nanoTime() - startTime) / 1_000L + "mks") ;
    }



}
