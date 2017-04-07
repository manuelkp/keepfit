package com.example.keepfit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Dlg_SelectHistory extends DialogFragment  {
    Context context;

    Button btn_all;
    Button btn_week;
    Button btn_month;
    Button btn_period;
    Button btn_goal;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dlg_select_history, container, false);
        getDialog().setTitle("Choose Activity Period");

        context=getContext();

        Fragment fragment = new Nav_ChooseHistory();
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.cmfragm, fragment);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currDate = sdf.format(new Date());
        String[] currDateParts = currDate.split("-");
        int day = Integer.parseInt(currDateParts[2]);
        int month = Integer.parseInt(currDateParts[1]);
        int year = Integer.parseInt(currDateParts[0]);

        Nav_ChooseHistory.weekEarlier = Converter.LastWeek(day, month, year );
        Nav_ChooseHistory.monthEarlier= Converter.LastMonth(day, month, year );



        btn_all = (Button) rootView.findViewById(R.id.all_activities);
        btn_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Nav_ChooseHistory.selection = 0;
                dismiss();
                ft.commit();
            }
        });

        btn_week = (Button) rootView.findViewById(R.id.past_week);
        btn_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Nav_ChooseHistory.selection = 1;
                dismiss();
                ft.commit();
            }
        });

        btn_month = (Button) rootView.findViewById(R.id.past_month);
        btn_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Nav_ChooseHistory.selection = 2;
                dismiss();
                ft.commit();
            }
        });

        btn_period = (Button) rootView.findViewById(R.id.select_days);
        btn_period.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Nav_ChooseHistory.selection = 3;
                DialogFragment newFragment = new Dlg_SelectDaysOfHistory();
                newFragment.show(getFragmentManager(), "ActivityPicker");

                dismiss();
            }
        });

        btn_goal = (Button) rootView.findViewById(R.id.goal_achieved);
        btn_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Nav_ChooseHistory.selection = 4;
                dismiss();
                ft.commit();
            }
        });


        return rootView;
    }

}