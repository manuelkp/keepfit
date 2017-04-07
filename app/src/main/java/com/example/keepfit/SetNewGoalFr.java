package com.example.keepfit;


import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class SetNewGoalFr extends Fragment implements View.OnClickListener {
    Spinner unitSpinner;
    Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_new_goal_fr, container, false);

        btn = (Button) view.findViewById(R.id.set_button);
        btn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        unitSpinner = (Spinner) getActivity().findViewById(R.id.unitspinner);

        ContentValues values = new ContentValues();

        EditText ev = (EditText) getActivity().findViewById(R.id.setstepsnew_et);
        String stepsStr = ev.getText().toString();

        double steps = 0;
        if (!Converter.isNumeric(stepsStr)) {
            Toast.makeText(getActivity(), "Put a number for Distance!", Toast.LENGTH_SHORT).show();
        } else {
            steps = Double.parseDouble(stepsStr);
            if (steps < 0 || steps >= Integer.MAX_VALUE / 1000) {
                Toast.makeText(getActivity(), "Put a proper number for Distance!", Toast.LENGTH_SHORT).show();
            } else {

                values.put(ContentProviderDB.G_STEPS, stepsStr);
                values.put(ContentProviderDB.G_NAME, ((EditText) getActivity().findViewById(R.id.setgnamesnew_et)).getText().toString());
                values.put(ContentProviderDB.G_SELECTED, "no");
                values.put(ContentProviderDB.G_UNITS, String.valueOf(unitSpinner.getSelectedItem().toString()));

                getActivity().getContentResolver().insert(ContentProviderDB.CONTENT_URI_G, values);


                Fragment fragment = new Nav_SeeSavedGoalsFr();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.cmfragm, fragment);
                ft.commit();

            }
        }

    }


}
