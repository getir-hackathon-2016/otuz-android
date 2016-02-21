package com.otuz.util;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import com.otuz.listener.DatePickedListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by AhmetOguzhanBasar on 21.02.2016.
 */
@SuppressLint("ValidFragment")
public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private Context context;
    private DatePickedListener datePickedListener;

    private SimpleDateFormat formatWithDayName  = new SimpleDateFormat("MMMM, dd EEEE", new Locale("tr"));

    public DatePickerDialogFragment(){}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        context = getActivity();

        final Calendar c    = Calendar.getInstance();
        int year            = c.get(Calendar.YEAR);
        int month           = c.get(Calendar.MONTH);
        int day             = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(context, this, year, month, day);

    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String deliveryDate;
        Calendar calender = Calendar.getInstance();
        calender.set(year, monthOfYear, dayOfMonth);
        deliveryDate = formatWithDayName.format(calender.getTime());
        datePickedListener.onDatePicked(deliveryDate);
    }

    public void setDatePickedListener(DatePickedListener datePickedListener){
        this.datePickedListener = datePickedListener;
    }

    public DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;

    }

}
