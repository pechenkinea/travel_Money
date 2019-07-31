package com.pechenkin.travelmoney.metering;

import android.util.Log;


/**
 * Created by pechenkin on 09.04.2018.
 */

class TimeMeter {

    private final String name;
    private final long startTime;
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
