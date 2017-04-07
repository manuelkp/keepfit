package com.example.keepfit;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringDef;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class Dlg_SelectUnits extends DialogFragment  {
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dlg_select_stat_units, container, false);
        getDialog().setTitle("Choose Activity Period");

        context=getContext();

        Fragment fragment = new Nav_Statistics();
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.cmfragm, fragment);


        final Spinner uniSp = (Spinner) rootView.findViewById(R.id.unitstatspinner);

        Button btn_done = (Button) rootView.findViewById(R.id.done_su_button);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Nav_Statistics.prefUnits = String.valueOf(uniSp.getSelectedItem());
                dismiss();
                ft.commit();
            }
        });




        return rootView;
    }

}

