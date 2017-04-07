package com.example.keepfit;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Nav_Statistics extends Fragment implements View.OnClickListener {

    Context context;

    static String weekEarlier;
    static String monthEarlier;
    static String fromDate;
    static String toDate;
    static String prefUnits = "Steps";
    static int selection = 1;


    /********************
     * 1: Week
     * 2: Month
     * 3: Period
     * 4: Goals
     *******************/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.nav_statistics, container, false);
        context = getContext();
        Cursor cursor = null;


        Button uniBtn = (Button) view.findViewById(R.id.see_more_units);
        Button statBtn = (Button) view.findViewById(R.id.see_more_stats);

        double min = Double.parseDouble(new DecimalFormat("##.##").format(Double.MAX_VALUE));
        double max = 0;
        double avg = 0;
        int cnt = 0;
        double tempSteps = 0;
        String unit = "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currDate = sdf.format(new Date());
        String[] currDateParts = currDate.split("-");
        int day = Integer.parseInt(currDateParts[2]);
        int month = Integer.parseInt(currDateParts[1]);
        int year = Integer.parseInt(currDateParts[0]);
        weekEarlier = Converter.LastWeek(day, month, year);
        monthEarlier = Converter.LastMonth(day, month, year);

        TextView tvSubTitle = (TextView) view.findViewById(R.id.title_period);
        TextView tvUnits = (TextView) view.findViewById(R.id.title_units);
        TextView tvMin = (TextView) view.findViewById(R.id.dist_min);
        TextView tvMax = (TextView) view.findViewById(R.id.dist_max);
        TextView tvAvg = (TextView) view.findViewById(R.id.dist_avg);


        if (selection == 1) {   /** Week */

            cursor = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_R, null, "datenum>=" + Converter.DaysToInt(weekEarlier), null, "_id");
            tvSubTitle.setText("Over the Past Week");
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        cnt++;
                        unit = cursor.getString(cursor.getColumnIndex(ContentProviderDB.R_REC_UNIT));
                        tempSteps = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ContentProviderDB.R_STEPS)));
                        if (!unit.equals(Nav_Statistics.prefUnits)) {
                            tempSteps = Double.parseDouble(new DecimalFormat("##.##").format(Converter.ConvertUnits(unit, prefUnits, tempSteps)));
                        }
                        avg += tempSteps;
                        max = (max <= tempSteps) ? tempSteps : max;
                        min = (min >= tempSteps) ? tempSteps : min;
                    } while (cursor.moveToNext());
                }
                avg /= cnt;
                if (Nav_Statistics.prefUnits.equals("Steps")) {
                    tvMin.setText("" + (int) min);
                    tvMax.setText("" + (int) max);
                    tvAvg.setText("" + (int) avg);
                } else {
                    tvMin.setText("" + Double.parseDouble(new DecimalFormat("##.##").format(min)));
                    tvMax.setText("" + Double.parseDouble(new DecimalFormat("##.##").format(max)));
                    tvAvg.setText("" + Double.parseDouble(new DecimalFormat("##.##").format(avg)));
                }
            } else {
                tvMin.setText(" - ");
                tvMax.setText(" - ");
                tvAvg.setText(" - ");
            }

        } else if (selection == 2) {   /** Month */

            cursor = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_R, null, "datenum>=" + Converter.DaysToInt(monthEarlier), null, "_id");
            tvSubTitle.setText("Over the Past Month");
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        cnt++;
                        unit = cursor.getString(cursor.getColumnIndex(ContentProviderDB.R_REC_UNIT));
                        tempSteps = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ContentProviderDB.R_STEPS)));
                        if (!unit.equals(Nav_Statistics.prefUnits)) {
                            tempSteps = Double.parseDouble(new DecimalFormat("##.##").format(Converter.ConvertUnits(unit, prefUnits, tempSteps)));
                        }
                        avg += tempSteps;
                        max = (max <= tempSteps) ? tempSteps : max;
                        min = (min >= tempSteps) ? tempSteps : min;
                    } while (cursor.moveToNext());
                }
                avg /= cnt;
                if (Nav_Statistics.prefUnits.equals("Steps")) {
                    tvMin.setText("" + (int) min);
                    tvMax.setText("" + (int) max);
                    tvAvg.setText("" + (int) avg);
                } else {
                    tvMin.setText("" + Double.parseDouble(new DecimalFormat("##.##").format(min)));
                    tvMax.setText("" + Double.parseDouble(new DecimalFormat("##.##").format(max)));
                    tvAvg.setText("" + Double.parseDouble(new DecimalFormat("##.##").format(avg)));
                }
            } else {
                tvMin.setText(" - ");
                tvMax.setText(" - ");
                tvAvg.setText(" - ");
            }
        } else if (selection == 3) {   /** Period */
            cursor = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_R, null, "datenum>=" + Converter.DaysToInt(fromDate) + " AND " + "datenum<=" + Converter.DaysToInt(toDate), null, "_id");

            tvSubTitle.setText("From: " + fromDate + "      To: " + toDate);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        cnt++;
                        unit = cursor.getString(cursor.getColumnIndex(ContentProviderDB.R_REC_UNIT));
                        tempSteps = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ContentProviderDB.R_STEPS)));
                        if (!unit.equals(Nav_Statistics.prefUnits)) {
                            tempSteps = Double.parseDouble(new DecimalFormat("##.##").format(Converter.ConvertUnits(unit, prefUnits, tempSteps)));
                        }
                        avg += tempSteps;
                        max = (max <= tempSteps) ? tempSteps : max;
                        min = (min >= tempSteps) ? tempSteps : min;
                    } while (cursor.moveToNext());
                }
                avg /= cnt;
                if (Nav_Statistics.prefUnits.equals("Steps")) {
                    tvMin.setText("" + (int) min);
                    tvMax.setText("" + (int) max);
                    tvAvg.setText("" + (int) avg);
                } else {
                    tvMin.setText("" + Double.parseDouble(new DecimalFormat("##.##").format(min)));
                    tvMax.setText("" + Double.parseDouble(new DecimalFormat("##.##").format(max)));
                    tvAvg.setText("" + Double.parseDouble(new DecimalFormat("##.##").format(avg)));
                }
            } else {
                tvMin.setText(" - ");
                tvMax.setText(" - ");
                tvAvg.setText(" - ");
            }

        } else if (selection == 4) {   /** Successful Goals */
            Fragment fr = new Statistics_Goals();
            final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.cmfragm, fr);
            ft.commit();
        }

        tvUnits.setText("~ in " + prefUnits + " ~");


        uniBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DialogFragment newFragment = new Dlg_SelectUnits();
                newFragment.show(getFragmentManager(), "ActivityPicker");
            }
        });

        statBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DialogFragment newFragment = new Dlg_SelectStats();
                newFragment.show(getFragmentManager(), "ActivityPicker");
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
    }

}

