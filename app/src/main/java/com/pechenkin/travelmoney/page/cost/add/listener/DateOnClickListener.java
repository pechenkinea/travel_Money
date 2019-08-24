package com.pechenkin.travelmoney.page.cost.add.listener;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.TextView;

import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;

import java.util.Calendar;
import java.util.Date;

public class DateOnClickListener implements View.OnClickListener {

    private Date selectDate;
    private DateSetFunction dateSetFunction;

    public DateOnClickListener(Date selectDate, DateSetFunction dateSetFunction){
        this.selectDate = selectDate;
        this.dateSetFunction = dateSetFunction;
    }

    @Override
    public void onClick(View view) {

        Calendar c = Calendar.getInstance();
        c.setTime(selectDate);


        DatePickerDialog.OnDateSetListener onDateSetListener = (datePicker, year, monthOfYear, dayOfMonth) -> {
            Calendar c1 = Calendar.getInstance();

            TimePickerDialog.OnTimeSetListener timeCallBack = (timePicker, hourOfDay, minute) -> {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);

                ((TextView) MainActivity.INSTANCE.findViewById(R.id.textDate))
                        .setText(Help.dateToDateTimeStr(cal.getTime()));

                selectDate = cal.getTime();
                dateSetFunction.setDate(selectDate);
            };

            TimePickerDialog timeDialog = new TimePickerDialog(MainActivity.INSTANCE, timeCallBack, c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE), true);

            timeDialog.show();
        };

        DatePickerDialog dateDialog = new DatePickerDialog(MainActivity.INSTANCE, onDateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        Calendar cMax = Calendar.getInstance();
        cMax.set(Calendar.HOUR_OF_DAY, 23);
        cMax.set(Calendar.MINUTE, 59);

        dateDialog.getDatePicker().setMaxDate(cMax.getTime().getTime());
        dateDialog.show();

    }


    public interface DateSetFunction{
        void setDate(Date selectDate);
    }
}
