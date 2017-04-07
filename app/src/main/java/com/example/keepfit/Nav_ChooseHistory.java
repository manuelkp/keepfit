package com.example.keepfit;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import static com.example.keepfit.MainActivity.currentDate;


public class Nav_ChooseHistory extends Fragment implements View.OnClickListener {

    Context context;
    Button delbtn;
    Button histbtn;
    static String weekEarlier;
    static String monthEarlier;
    static String fromDate = "2017-01-01";
    static String toDate = "2017-01-01";

    static int selection = 0;
    /********************
     * 0: Whole History
     * 1: Week
     * 2: Month
     * 3: Period
     * 4: Goals
     *******************/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.nav_content_history, container, false);
        context = getContext();
        Cursor cursor = null;

//        String[] curDateNumAr = currentDate.split("-");
//        String curDateN = curDateNumAr[0]+curDateNumAr[1]+curDateNumAr[2];





        if (selection == 0) {   /** All Activities */
            cursor = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_R, null, null, null, "datenum");
        }
        else if (selection == 1) {   /** Week */
            cursor = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_R, null, "datenum>="+ Converter.DaysToInt(weekEarlier), null, "datenum");
        }
        else if (selection == 2) {   /** Month */
            cursor = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_R, null, "datenum>="+ Converter.DaysToInt(monthEarlier), null, "datenum");
        }
        else if (selection == 3) {   /** Period */
            cursor = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_R, null, "datenum>=" + Converter.DaysToInt(fromDate) + " AND " + "datenum<=" + Converter.DaysToInt(toDate) , null, "datenum");

        }
        else if (selection == 4) {   /** Successful Goals */
            cursor = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_R, null, "successful like '%V%'", null, "datenum");
        }

        final ListView listViewGoals = (ListView) view.findViewById(R.id.list_rdb);

        String[] arrayColumns = new String[]{"date", "steps", "recunit", "successful"};
        int[] arrayViewIDs = new int[]{R.id.record_date, R.id.record_steps, R.id.gd_chosen_units, R.id.record_succ};


        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.listview_each_item_history, cursor,
                arrayColumns, arrayViewIDs, 1);

        listViewGoals.setAdapter(adapter);


        histbtn = (Button) view.findViewById(R.id.see_spec_history);
        histbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DialogFragment newFragment = new Dlg_SelectHistory();
                newFragment.show(getFragmentManager(), "ActivityPicker");
            }
        });


        delbtn = (Button) view.findViewById(R.id.clear_history);
        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                getActivity().getContentResolver().delete(ContentProviderDB.CONTENT_URI_R, null, null);
                MainActivity.stepCounter = 0;
                Toast.makeText(getActivity(), "DELETE", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    @Override
    public void onClick(View v) {
    }

}

