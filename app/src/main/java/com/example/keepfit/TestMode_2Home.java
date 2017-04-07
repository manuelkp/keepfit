package com.example.keepfit;

import android.database.Cursor;
import android.os.Bundle;
import java.text.DecimalFormat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class TestMode_2Home extends Fragment {

    Button goalButton, refrButton;
    static TextView stepsCounterTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.nav_content_main, container, false);


        /******************************************* GOAL *****************************************/

        TextView tv = (TextView) view.findViewById(R.id.text_goal);
        TextView tv_u = (TextView) view.findViewById(R.id.text_units);

        Cursor cursor1 = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_R, null, "_id=" + Nav_TestMode_1Pick.tm_ID, null, "_id");

        stepsCounterTV = (TextView) view.findViewById(R.id.textView1b);


        double steps = 0;
        int goalId = 0;
        String gUnits = "";
        String gName = "";
        if (cursor1.moveToFirst()) {
            do {
                steps = Double.parseDouble(cursor1.getString(cursor1.getColumnIndex(ContentProviderDB.R_STEPS)));
                goalId = Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(ContentProviderDB.R_GOAL_OTD)));
                gUnits = cursor1.getString(cursor1.getColumnIndex(ContentProviderDB.R_REC_UNIT));
            } while (cursor1.moveToNext());
        }

        if(gUnits.equals("Steps")){
            stepsCounterTV.setText((int) steps + " (" + gUnits + ")");
        }
        else
            stepsCounterTV.setText( Double.parseDouble(new DecimalFormat("##.##").format(steps)) + " (" + MainActivity.units + ")");


        int goalSteps = 0;
        Cursor cursor2 = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_G, null, "_id=" + goalId, null, "_id");

        String selGoal = null;

        if (cursor2.moveToFirst()) {
            do {
                selGoal = "";
                selGoal = cursor2.getString(cursor2.getColumnIndex(ContentProviderDB.G_NAME));
                goalSteps += Double.parseDouble(cursor2.getString(cursor2.getColumnIndex(ContentProviderDB.G_STEPS)));
            } while (cursor2.moveToNext());
        }

        String string1 = "Goal deleted.";
        if (selGoal != null)
            tv.setText(selGoal);
        else tv.setText(string1);


/************************************** PROGRESS BAR **************************************/
        ProgressBar pb = (ProgressBar) view.findViewById(R.id.progressBar);
        pb.setMax(goalSteps);
        if (steps <= pb.getMax()) {
            //Set first progress bar value
            pb.setProgress((int) steps);
            //Set the second progress bar value
            pb.setSecondaryProgress((int) (steps) + (int) (steps * 0.2));
        } else if (steps >= pb.getMax()) {
            pb.setProgress(pb.getMax());
        }


        goalButton = (Button) view.findViewById(R.id.change_goal);
        goalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(getActivity(), "Unclickable in Test Mode", Toast.LENGTH_SHORT).show();
            }
        });

        refrButton = (Button) view.findViewById(R.id.refresh);
        refrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(getActivity(), "Unclickable in Test Mode", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
