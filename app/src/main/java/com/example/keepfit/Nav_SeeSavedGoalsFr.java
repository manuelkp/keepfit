package com.example.keepfit;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import static com.example.keepfit.Nav_ChooseHome.idGoalSelected;

public class Nav_SeeSavedGoalsFr extends DialogFragment implements View.OnClickListener {

    Button  btn_set;
    Context context;
    static long editGoalId;
    static int editGoalPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.nav_see_saved_goals, container, false);
        context=getContext();

        Cursor cursor = getActivity().getContentResolver().query(ContentProviderDB.CONTENT_URI_G, null, null, null, "_id");

        final ListView listViewGoals = (ListView) view.findViewById(R.id.listdb);
        listViewGoals.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        String[] arrayColumns = new String[]{ "name","steps", "units"};
        int[] arrayViewIDs = new int[]{ R.id.goal_name,R.id.goal_steps, R.id.chosen_units};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.listview_each_item_goals,cursor,
                arrayColumns ,arrayViewIDs, 1);

        listViewGoals.setAdapter(adapter);


        Toast.makeText(getActivity(), "Press Long to Edit", Toast.LENGTH_SHORT).show();


        listViewGoals.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {

                editGoalId = id;
                editGoalPosition = pos;
                Fragment fragment = new EditGoalFr();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.cmfragm, fragment);
                ft.addToBackStack(null);
                ft.commit();

                return true;
            }
        });


        listViewGoals.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {
                DialogFragment newFragment = new Dlg_SelectGoalQuest();
                newFragment.show(getFragmentManager(), "GoalPicker");
                idGoalSelected = arg3;
            }
        });


        btn_set = (Button) view.findViewById(R.id.setnewgoal);
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Fragment fragment = new SetNewGoalFr();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
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














/////////////////////////








