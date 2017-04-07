package com.example.keepfit;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class Nav_TestMode_1Pick extends Fragment implements View.OnClickListener {

    Context context;
    static long tm_ID=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** Inflate the layout for this fragment */
        View view = inflater.inflate( R.layout.nav_test_mode, container, false);
        context=getContext();


        Cursor cursor = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_R, null, null, null, "_id");

        final ListView listViewGoals = (ListView) view.findViewById(R.id.list_rdb);

        String[] arrayColumns = new String[]{ "date","steps", "successful"};
        int[] arrayViewIDs = new int[]{ R.id.record_date,R.id.record_steps, R.id.record_succ};


        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.listview_each_item_history,cursor,
                arrayColumns ,arrayViewIDs, 1);

        listViewGoals.setAdapter(adapter);

        listViewGoals.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {
                tm_ID = arg3;
                Fragment fragment = new TestMode_2Home();
                android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.cmfragm, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });


        return view;
    }

    @Override
    public void onClick(View v) {
    }
}

