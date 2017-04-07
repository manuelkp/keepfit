package com.example.keepfit;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.keepfit.MainActivity.currentDate;


public class Fab_AddActivityFr extends Fragment implements View.OnClickListener {

    Spinner unitSpinner;
    Button btn_add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fab_add_activity_fr, container, false);


        unitSpinner = (Spinner) getActivity().findViewById(R.id.unitssetspinner);
        btn_add = (Button) view.findViewById(R.id.add_button);
        btn_add.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View v) {

        ContentValues values = new ContentValues();
        ContentValues valuesS = new ContentValues();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        MainActivity.currentDate = sdf.format(new Date());

        /** Units of Distance*/
        unitSpinner = (Spinner) getActivity().findViewById(R.id.unitssetspinner);
        String unitsStr = String.valueOf(unitSpinner.getSelectedItem());


        EditText ev = (EditText) getActivity().findViewById(R.id.r_editText3);
        String stepsStr = ev.getText().toString();



        double steps = 0;


        if(!Converter.isNumeric(stepsStr)){
            Toast.makeText(getActivity(), "Put a number for Distance!", Toast.LENGTH_SHORT).show();
        }
        else {
            steps = Double.parseDouble(stepsStr);
            if (steps < 0 || steps >= Integer.MAX_VALUE / 1000) {
                Toast.makeText(getActivity(), "Put a proper number for Distance!", Toast.LENGTH_SHORT).show();
            } else {
                steps = Converter.ConvertUnits(unitsStr, MainActivity.units, steps);
                stepsStr = String.valueOf(steps);
                String tempID = "";
                Cursor cursor = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_R, null, "date like '%" + MainActivity.currentDate + "%'", null, "_id");
                if (cursor.moveToFirst()) {
                    do {
                        // stepCounter gets the actual "double" distance
                        MainActivity.stepCounter = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ContentProviderDB.R_STEPS)));
                        tempID = cursor.getString(cursor.getColumnIndex(ContentProviderDB.R_ID));
                    } while (cursor.moveToNext());
                }

                MainActivity.stepCounter += steps;

                /** If current date has recrods -> Update CP
                 *  Otherwise -> Insert in CP */
                values.put(ContentProviderDB.DATE, currentDate);

                String[] curDatenumAr = currentDate.split("-");
                String curDateN = curDatenumAr[0] + curDatenumAr[1] + curDatenumAr[2];
                int curDateInt = Integer.parseInt(curDateN);
                values.put(ContentProviderDB.DATENUM, curDateInt);

                if (MainActivity.stepCounter >= MainActivity.goalStepCounter) {

                    /** Notification */
                    if (MainActivity.notifications.equals("On")) {

                        Intent intent = new Intent(getActivity(), NotificationActivity.class);
                        PendingIntent pIntent = PendingIntent.getActivity(getActivity(), (int) System.currentTimeMillis(), intent, 0);

                        Notification notif = new Notification.Builder(getActivity())
                                .setContentTitle("Goal Reached!")
                                .setContentText("" + MainActivity.stepCounter + " " + MainActivity.units).setSmallIcon(R.drawable.runningicon)
                                .setContentIntent(pIntent).build();
                        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                        // hide the notification after its selected
                        notif.flags |= Notification.FLAG_AUTO_CANCEL;

                        notificationManager.notify(0, notif);
                    }
                    Toast.makeText(getActivity(), "Goal Achieved", Toast.LENGTH_SHORT).show();
                    values.put(ContentProviderDB.SUCCESSFUL, "V");

                } else
                    values.put(ContentProviderDB.SUCCESSFUL, "X");

                values.put(ContentProviderDB.R_GOAL_OTD, "" + Nav_ChooseHome.idGoalSelected);

                values.put(ContentProviderDB.R_REC_UNIT, "" + MainActivity.units);

                if (cursor.getCount() == 0) {   /** INSERT */

                    values.put(ContentProviderDB.R_STEPS, String.valueOf(stepsStr));
                    getActivity().getContentResolver().insert(ContentProviderDB.CONTENT_URI_R, values);
                } else {                        /** UPDATE */
                    values.put(ContentProviderDB.R_STEPS, "" + MainActivity.stepCounter);
                    getActivity().getContentResolver().update(ContentProviderDB.CONTENT_URI_R, values, "_id = " + tempID, null);
                }


                Toast.makeText(getActivity(), "Steps add up", Toast.LENGTH_SHORT).show();

                MainActivity.stepCounter = Double.parseDouble(new DecimalFormat("##.##").format(MainActivity.stepCounter));

                Fragment fragment = new Nav_ChooseHome();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.cmfragm, fragment);
                ft.commit();
            }
        }
    }


}
