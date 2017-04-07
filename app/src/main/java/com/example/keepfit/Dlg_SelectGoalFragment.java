package com.example.keepfit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.keepfit.Nav_ChooseHome.idGoalSelected;


public class Dlg_SelectGoalFragment extends DialogFragment {
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dlg_select_goal, container, false);
        getDialog().setTitle("Pick a Goal");

        context = getContext();

        Cursor cursor = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_G, null, null, null, "_id");

        if (cursor.getCount() == 0) {
            SetNewGoalFr fragment = new SetNewGoalFr();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.cmfragm, fragment);
            ft.commit();
        }


        final ListView listViewGoals = (ListView) rootView.findViewById(R.id.gdlistdb);
        listViewGoals.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        String[] arrayColumns = new String[]{"name", "steps", "units"};
        int[] arrayViewIDs = new int[]{R.id.gd_goal_name, R.id.gd_goal_steps, R.id.gd_chosen_units};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.listview_each_item_selgoal, cursor,
                arrayColumns, arrayViewIDs, 1);

        listViewGoals.setAdapter(adapter);

        listViewGoals.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                /** when user clicks on ListView Item , onItemClick is called
                 we can use the position parameter to get index of clicked item */


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                MainActivity.currentDate = sdf.format(new Date());

                idGoalSelected = arg3;
                Cursor cursor1 = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_G, null, "selected like '%yes%'", null, "_id");
                Cursor cursor2 = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_G, null, "_id = " + idGoalSelected, null, "_id");
                Cursor cursor3 = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_R, null, "date like '%" + MainActivity.currentDate + "%'", null, "_id");


                ContentValues values = new ContentValues();
                ContentValues values_R = new ContentValues();
                ContentValues values_Set = new ContentValues();

                String tempUnits = MainActivity.units;

                if (cursor1.moveToFirst()) { /** Update goal selection */
                    do {
                        values.put(ContentProviderDB.G_SELECTED, "no");
                        getActivity().getContentResolver().update(ContentProviderDB.CONTENT_URI_G, values, null, null);
                    } while (cursor1.moveToNext());
                }

                if (cursor2.moveToFirst()) { /** Update Current Goal */
                    do {
                        Toast.makeText(getActivity(), "Goal Selected", Toast.LENGTH_SHORT).show();
                        MainActivity.units = cursor2.getString(cursor2.getColumnIndex(ContentProviderDB.S_UNITS));
                        values.put(ContentProviderDB.G_SELECTED, "yes");
                        values_Set.put(ContentProviderDB.S_MODE, MainActivity.mode);
                        values_Set.put(ContentProviderDB.S_UNITS, MainActivity.units);
                        getActivity().getContentResolver().update(ContentProviderDB.CONTENT_URI_G, values, "_id = " + idGoalSelected, null);
                        getActivity().getContentResolver().update(ContentProviderDB.CONTENT_URI_S, values_Set, "_id = " + MainActivity.settings_id, null);
                    } while (cursor2.moveToNext());
                }

                if (cursor3.moveToFirst()) { /** Update Current Record */
                    do {
                        values_R.put(ContentProviderDB.DATE, cursor3.getString(cursor3.getColumnIndex(ContentProviderDB.DATE)));
                        values_R.put(ContentProviderDB.DATENUM, cursor3.getInt(cursor3.getColumnIndex(ContentProviderDB.DATENUM)));
                        values_R.put(ContentProviderDB.SUCCESSFUL, cursor3.getString(cursor3.getColumnIndex(ContentProviderDB.SUCCESSFUL)));
                        values_R.put(ContentProviderDB.R_GOAL_OTD, "" + Nav_ChooseHome.idGoalSelected);

                        String rec_Steps = cursor3.getString(cursor3.getColumnIndex(ContentProviderDB.R_STEPS));
                        double rec_St_dbl = Double.parseDouble(rec_Steps);
                        rec_St_dbl = Double.parseDouble(new DecimalFormat("##.##").format(Converter.ConvertUnits(tempUnits, MainActivity.units, rec_St_dbl)));
                        rec_Steps = String.valueOf(rec_St_dbl);
                        MainActivity.stepCounter = rec_St_dbl;

                        values_R.put(ContentProviderDB.R_STEPS, rec_Steps);
                        values_R.put(ContentProviderDB.R_REC_UNIT, "" + MainActivity.units);

                        getActivity().getContentResolver().update(ContentProviderDB.CONTENT_URI_R, values_R, null, null);
                    } while (cursor3.moveToNext());
                }


                dismiss();
                Nav_ChooseHome fragment = new Nav_ChooseHome();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.cmfragm, fragment);
                ft.commit();

            }
        });


        return rootView;
    }

}