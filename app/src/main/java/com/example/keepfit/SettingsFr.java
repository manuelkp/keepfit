package com.example.keepfit;


import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;


public class SettingsFr extends Fragment implements View.OnClickListener {

    Button btn_edit;

    Switch notifSwitch;
    Switch modeSwitch;
    Switch editGSwitch;
    Switch trigGSwitch;

    String modeStr;
    String ntfStr = "";
    String unitsStr = "";
    String editGStr = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, container, false);

        modeSwitch = (Switch) view.findViewById(R.id.switchmode);
        notifSwitch = (Switch) view.findViewById(R.id.switchnot);
        editGSwitch = (Switch) view.findViewById(R.id.switchgoal);
        trigGSwitch = (Switch) view.findViewById(R.id.switchtriggers);

        String cur_mode = "";
        String cur_notif = "";
        String cur_goal = "";
        Toast.makeText(getActivity(),MainActivity.mode+" | "+MainActivity.notifications+" | "+MainActivity.editGoals, Toast.LENGTH_SHORT).show();



        Cursor cursor = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_S, null, null, null, "_id");

        if (cursor.moveToFirst()) {
            do {
                cur_mode = cursor.getString(cursor.getColumnIndex(ContentProviderDB.S_MODE));
                cur_notif = cursor.getString(cursor.getColumnIndex(ContentProviderDB.S_NOTIFICATIONS));
                cur_goal = cursor.getString(cursor.getColumnIndex(ContentProviderDB.S_EDITGOAL));
                MainActivity.settings_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContentProviderDB.S_ID)));
            } while (cursor.moveToNext());
        }

        modeSwitch.setChecked(cur_mode.equals("Auto"));
        notifSwitch.setChecked(cur_notif.equals("On"));
        editGSwitch.setChecked(cur_goal.equals("On"));
        if (MainActivity.triggers.equals("On"))
            trigGSwitch.setChecked(true);
        else
            trigGSwitch.setChecked(false);




        /** EDIT */
        btn_edit = (Button) view.findViewById(R.id.settings_button);
        btn_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Cursor cursor_set = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_S, null, null, null, "_id");

                /** Units stay the same, they change based on the selected goal*/
                if (cursor_set.moveToFirst()) {
                    do {
                        unitsStr = cursor_set.getString(cursor_set.getColumnIndex(ContentProviderDB.S_UNITS));
                    } while (cursor_set.moveToNext());
                }

                modeStr=(modeSwitch.isChecked())?"Auto":"Manual";
                ntfStr=(notifSwitch.isChecked())?"On":"Off";
                editGStr=(editGSwitch.isChecked())?"On":"Off";
                MainActivity.triggers=(trigGSwitch.isChecked())?"On":"Off";

                ContentValues values = new ContentValues();
                values.put(ContentProviderDB.S_UNITS, unitsStr);
                values.put(ContentProviderDB.S_MODE, modeStr);
                values.put(ContentProviderDB.S_NOTIFICATIONS, ntfStr);
                values.put(ContentProviderDB.S_EDITGOAL, editGStr);
                getActivity().getContentResolver().update(ContentProviderDB.CONTENT_URI_S, values, "_id = " + MainActivity.settings_id, null);

                MainActivity.mode = modeStr;
                MainActivity.units = unitsStr;
                MainActivity.notifications = ntfStr;
                MainActivity.editGoals = editGStr;

//                MainActivity.jobScheduler = (JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
//                if(MainActivity.triggers.equals("Off")){
//                    MainActivity.jobScheduler.cancelAll();
//                    Toast.makeText(getActivity(), "No Triggers!", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                   MainActivity.jobScheduler = (JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
//                    JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(getActivity().getPackageName(),
//                            TurnOnTriggers.class.getName()));
//
//                    builder.setPeriodic(10000);
//                    MainActivity.jobScheduler.schedule(builder.build());
//                }


                Fragment fragment = new Nav_ChooseHome();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.cmfragm, fragment);
                Toast.makeText(getActivity(), "Settings Changed", Toast.LENGTH_SHORT).show();
                ft.commit();

            }
        });

        return view;
    }


    @Override
    public void onClick(View v) {
    }

}
