package com.example.keepfit;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Statistics_Goals extends Fragment implements View.OnClickListener {

    Context context;
    static int achGoalsRec = 0;
    static int allRecs = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.statistics_goals, container, false);
        context = getContext();

        Cursor cursorAll = null;
        Cursor cursor = null;
        cursorAll = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_R, null, null, null, "_id");
        cursor = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_R, null, "successful like '%V%'", null, "_id");

        achGoalsRec = cursor.getCount();
        allRecs = cursorAll.getCount();

        TextView tvPrcg = (TextView) view.findViewById(R.id.title_prcg);
        tvPrcg.setText((achGoalsRec*100/allRecs) + "%");

        final ListView listViewGoals = (ListView) view.findViewById(R.id.list_rdb);

        String[] arrayColumns = new String[]{"date", "steps", "recunit", "successful"};
        int[] arrayViewIDs = new int[]{R.id.record_date, R.id.record_steps, R.id.gd_chosen_units, R.id.record_succ};


        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.listview_each_item_history, cursor,
                arrayColumns, arrayViewIDs, 1);

        listViewGoals.setAdapter(adapter);

        Button statBtn = (Button) view.findViewById(R.id.see_more_stats_g);
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

