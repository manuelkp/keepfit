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
import android.widget.TextView;


public class Dlg_SelectDaysOfHistory extends DialogFragment {
    Context context;

    Button btn_from;
    Button btn_to;
    Button btn_done;
    static int dateChoice;
    static TextView tvFrom;
    static TextView tvTo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dlg_select_days_hist, container, false);
        getDialog().setTitle("Choose Activity Period");

        context = getContext();

        MainActivity.choice = "History";
        Fragment fragment = new Nav_ChooseHistory();
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.cmfragm, fragment);


        btn_from = (Button) rootView.findViewById(R.id.datefrom_btn);
        btn_to = (Button) rootView.findViewById(R.id.dateto_btn);
        btn_done = (Button) rootView.findViewById(R.id.done_btn);

        tvFrom = (TextView) rootView.findViewById(R.id.datefrom);
        tvTo = (TextView) rootView.findViewById(R.id.dateto);


        btn_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dateChoice = 0;
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });


        btn_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dateChoice = 1;
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dismiss();
                ft.commit();
            }
        });


        return rootView;
    }

}