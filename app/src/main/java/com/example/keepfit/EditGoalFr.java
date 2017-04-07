package com.example.keepfit;


import android.content.ContentValues;
import android.database.Cursor;
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


public class EditGoalFr extends Fragment implements View.OnClickListener {
    Spinner unitSpinner;
    Button btn_edit, btn_del;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_goals_fr, container, false);

        final Cursor cursor = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_G, null, "_id = " + Nav_SeeSavedGoalsFr.editGoalId, null, "_id");

        EditText gName_et = (EditText) view.findViewById(R.id.goalname_et);
        if (cursor.moveToFirst()) {
            do {
                gName_et.setText(cursor.getString(cursor.getColumnIndex(ContentProviderDB.G_NAME)));
            } while (cursor.moveToNext());
        }

        EditText step_et = (EditText) view.findViewById(R.id.setsteps_et);
        if (cursor.moveToFirst()) {
            do {
                step_et.setText(cursor.getString(cursor.getColumnIndex(ContentProviderDB.G_STEPS)));
            } while (cursor.moveToNext());
        }
        unitSpinner = (Spinner) getActivity().findViewById(R.id.unitspinner);


        /** EDIT */
        btn_edit = (Button) view.findViewById(R.id.set_button);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (MainActivity.editGoals.equals("Off")) {
                    Toast.makeText(getActivity(), "To Edit a Goal, go to Settings first", Toast.LENGTH_SHORT).show();
                } else {

                    unitSpinner = (Spinner) getActivity().findViewById(R.id.unitspinner);

                    String unitsStr = String.valueOf(unitSpinner.getSelectedItem());

                    ContentValues values = new ContentValues();


                    String stepsStr = ((EditText) getActivity().findViewById(R.id.setsteps_et)).getText().toString();
                    if (!Converter.isNumeric(stepsStr)) {
                        Toast.makeText(getActivity(), "Put a number for Distance!", Toast.LENGTH_SHORT).show();
                    } else if (Double.parseDouble(stepsStr) < 0 || Double.parseDouble(stepsStr) >= Integer.MAX_VALUE / 1000) {
                        Toast.makeText(getActivity(), "Put a proper number for Distance!", Toast.LENGTH_SHORT).show();
                    } else {
                        values.put(ContentProviderDB.G_STEPS, stepsStr);
                        values.put(ContentProviderDB.G_UNITS, unitsStr);
                        values.put(ContentProviderDB.G_NAME,
                                ((EditText) getActivity().findViewById(R.id.goalname_et)).getText().toString());
                        values.put(ContentProviderDB.G_SELECTED, "no");
                        getActivity().getContentResolver().update(ContentProviderDB.CONTENT_URI_G, values, "_id = " + Nav_SeeSavedGoalsFr.editGoalId, null);


                        Fragment fragment = new Nav_SeeSavedGoalsFr();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.cmfragm, fragment);
                        Toast.makeText(getActivity(), "New Goal Set!", Toast.LENGTH_LONG).show();
                        ft.commit();
                    }
                }
            }
        });

        /** DELETE */
        btn_del = (Button) view.findViewById(R.id.del_button);
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                getActivity().getContentResolver().delete(ContentProviderDB.CONTENT_URI_G, "_id=" + Nav_SeeSavedGoalsFr.editGoalId, null);
                Toast.makeText(getActivity(), "DELETE", Toast.LENGTH_SHORT).show();

                MainActivity.goalName = "";
                MainActivity.goalStepCounter = 0;
                MainActivity.units = "Steps";
                Nav_ChooseHome.idGoalSelected = 0;

                Fragment fragment = new Nav_SeeSavedGoalsFr();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.cmfragm, fragment);
                ft.commit();
            }
        });

        return view;
    }


    @Override
    public void onClick(View v) {
    }

}
