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

import java.text.SimpleDateFormat;
import java.util.Date;


public class Dlg_SelectStats extends DialogFragment  {
    Context context;

    Button btn_all;
    Button btn_week;
    Button btn_month;
    Button btn_period;
    Button btn_goal;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dlg_select_stats, container, false);
        getDialog().setTitle("Choose Activity Period");

        context=getContext();

        Fragment fragment = new Nav_Statistics();
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.cmfragm, fragment);








        btn_week = (Button) rootView.findViewById(R.id.st_past_week);
        btn_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Nav_Statistics.selection = 1;
                dismiss();
                ft.commit();
            }
        });

        btn_month = (Button) rootView.findViewById(R.id.st_past_month);
        btn_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Nav_Statistics.selection = 2;
                dismiss();
                ft.commit();
            }
        });

        btn_period = (Button) rootView.findViewById(R.id.st_select_days);
        btn_period.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Nav_Statistics.selection = 3;
                DialogFragment newFragment = new Dlg_SelectDaysOfStats();
                newFragment.show(getFragmentManager(), "ActivityPicker");

                dismiss();
            }
        });

        btn_goal = (Button) rootView.findViewById(R.id.st_goal_achieved);
        btn_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Nav_Statistics.selection = 4;

                Fragment fr = new Statistics_Goals();
                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.cmfragm, fr);

                dismiss();
                ft.commit();
            }
        });


        return rootView;
    }

}