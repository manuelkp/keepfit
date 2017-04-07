package com.example.keepfit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;


public class Nav_ChooseHome extends Fragment {

    static long idGoalSelected = 0;
    Button goalButton, refrButton;
    static TextView stepsCounterTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.nav_content_main, container, false);


        /****************************************** STEPS *****************************************/
        stepsCounterTV = (TextView) view.findViewById(R.id.textView1b);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        MainActivity.currentDate = sdf.format(new Date());


        Cursor cursorR = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_R, null, "date like '%" + MainActivity.currentDate + "%'", null, "_id");

        if(cursorR.getCount() == 0)
            if(MainActivity.mode.equals("Auto"))
                MainActivity.dateFlag = true;


        if (cursorR.moveToFirst()) {
            do {
                MainActivity.stepCounter = Double.parseDouble(cursorR.getString(cursorR.getColumnIndex(ContentProviderDB.R_STEPS)));
                MainActivity.units = cursorR.getString(cursorR.getColumnIndex(ContentProviderDB.R_REC_UNIT));
            } while (cursorR.moveToNext());
        }

        if (MainActivity.stepCounter != 0) {
            if(MainActivity.units.equals("Steps")){
                int tempSteps = (int) MainActivity.stepCounter;
                stepsCounterTV.setText(tempSteps + " (" + MainActivity.units + ")");
            }
            else
                stepsCounterTV.setText( Double.parseDouble(new DecimalFormat("##.##").format(MainActivity.stepCounter)) + " (" + MainActivity.units + ")");
        }
//
        cursorR.close();

        /******************************************* GOAL *****************************************/
        TextView tv = (TextView) view.findViewById(R.id.text_goal);
        TextView tv_u = (TextView) view.findViewById(R.id.text_units);

        String string1 = "No Goal";

        /** Check for goal*/
        Cursor cursor = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_G, null, "selected like '%yes%'", null, "_id");
        if (cursor.moveToFirst()) {
            do {
                MainActivity.goalName = cursor.getString(cursor.getColumnIndex(ContentProviderDB.G_NAME));
                MainActivity.goalStepCounter = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContentProviderDB.G_STEPS)));
                MainActivity.units = cursor.getString(cursor.getColumnIndex(ContentProviderDB.G_UNITS));
                idGoalSelected = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContentProviderDB.G_ID)));

            } while (cursor.moveToNext());
        }
        cursor.close();

        if (MainActivity.goalName != null) {
            tv.setText(MainActivity.goalName);
        } else {
            tv.setText(string1);

            /** Notification */
            if (MainActivity.notifications.equals("On")) {

                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(getActivity(), (int) System.currentTimeMillis(), intent, 0);

                Notification notif = new Notification.Builder(getActivity())
                        .setContentTitle("Set a New Goal")
                        .setContentText("Go to \"See Goals\" to set your Goal for Today").setSmallIcon(R.drawable.runningicon)
                        .setContentIntent(pIntent).build();
                NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

                notif.flags |= Notification.FLAG_AUTO_CANCEL;

                notificationManager.notify(0, notif);
            }
        }
        tv_u.setText(MainActivity.units);


        /************************************** PROGRESS BAR **************************************/
        ProgressBar pb = (ProgressBar) view.findViewById(R.id.progressBar);
        pb.setMax(MainActivity.goalStepCounter);

        if (MainActivity.stepCounter < pb.getMax()) {
            pb.setProgress((int) MainActivity.stepCounter);
            pb.setSecondaryProgress((int) MainActivity.stepCounter + (int) (MainActivity.stepCounter * 0.2));
        } else if (MainActivity.stepCounter >= pb.getMax()) {
            pb.setProgress(pb.getMax());
        }

        /***************************************** BUTTONS ****************************************/
        goalButton = (Button) view.findViewById(R.id.change_goal);
        goalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Cursor cursor = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_G, null, null, null, "_id");

                if (cursor.getCount() == 0) {
                    Toast.makeText(getActivity(), "No Goals yet", Toast.LENGTH_SHORT).show();
                    SetNewGoalFr fragment = new SetNewGoalFr();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.cmfragm, fragment);
                    ft.commit();
                } else {
                    DialogFragment newFragment = new Dlg_SelectGoalFragment();
                    newFragment.show(getFragmentManager(), "GoalPicker");
                }
            }
        });

        refrButton = (Button) view.findViewById(R.id.refresh);
        refrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Nav_ChooseHome fragment = new Nav_ChooseHome();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.cmfragm, fragment);
                ft.commit();
            }
        });

        return view;
    }

}

/**
 * "Units" get their first value when the app first launches in a new device ("steps").
 * When the user adds their first record, the units don't change.
 * They change when the user sets a new goal (which will be the main unit
 * until it changes again by goal change!)
 **/
/** Should they change when the user adds their first activity, if goal doesn't exist?*/

/////Fragment use:
//        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//        Nav_ChooseHome fragment = new Nav_ChooseHome();
//        ft.add(R.id.cmfragm, fragment);
//        ft.addToBackStack(null);
//        ft.commit();