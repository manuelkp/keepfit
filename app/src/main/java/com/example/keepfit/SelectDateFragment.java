package com.example.keepfit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;


public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm + 1, dd);
    }

    public void populateSetDate(int year, int month, int day) {

        if( MainActivity.choice.equals("History")){
            if (Dlg_SelectDaysOfHistory.dateChoice == 0) {

                Nav_ChooseHistory.fromDate=year+"-";
                if (month<10)
                    Nav_ChooseHistory.fromDate+="0"+month+"-";
                else
                    Nav_ChooseHistory.fromDate+=month+"-";

                if (day<10)
                    Nav_ChooseHistory.fromDate+="0"+day;
                else
                    Nav_ChooseHistory.fromDate+=day;

                Dlg_SelectDaysOfHistory.tvFrom.setText(Nav_ChooseHistory.fromDate);
            }
            else if (Dlg_SelectDaysOfHistory.dateChoice == 1) {
                Nav_ChooseHistory.toDate=year+"-";
                if (month<10)
                    Nav_ChooseHistory.toDate+="0"+month+"-";
                else
                    Nav_ChooseHistory.toDate+=month+"-";

                if (day<10)
                    Nav_ChooseHistory.toDate+="0"+day;
                else
                    Nav_ChooseHistory.toDate+=day;
                Dlg_SelectDaysOfHistory.tvTo.setText(Nav_ChooseHistory.toDate);
            }
        }
        else if( MainActivity.choice.equals("Statistics")){
            if (Dlg_SelectDaysOfStats.dateChoice == 0) {

                Nav_Statistics.fromDate=year+"-";
                if (month<10)
                    Nav_Statistics.fromDate+="0"+month+"-";
                else
                    Nav_Statistics.fromDate+=month+"-";

                if (day<10)
                    Nav_Statistics.fromDate+="0"+day;
                else
                    Nav_Statistics.fromDate+=day;


                Dlg_SelectDaysOfStats.tvFrom.setText(Nav_Statistics.fromDate);
            }
            else if (Dlg_SelectDaysOfStats.dateChoice == 1) {

                Nav_Statistics.toDate=year+"-";
                if (month<10)
                    Nav_Statistics.toDate+="0"+month+"-";
                else
                    Nav_Statistics.toDate+=month+"-";

                if (day<10)
                    Nav_Statistics.toDate+="0"+day;
                else
                    Nav_Statistics.toDate+=day;


                Dlg_SelectDaysOfStats.tvTo.setText(Nav_Statistics.toDate);
            }
        }

    }
}