package com.example.keepfit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.widget.Toast;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    static double stepCounter = 0;
    static double sensStepCounter = 0;
    static double firstSensStepCounter = 0;
    static double tempSensStepCounter = 0;

    static int goalStepCounter = Integer.MAX_VALUE;
    static String goalName = null;

    private SensorManager sensorManager;
    boolean activityRunning = false;

    static String currentDate;
    static int settings_id = 1;
    static String choice = null;
    static boolean dateFlag = true;


    static String units = "Steps";
    static String mode = "Manual";
    static String notifications = "On";
    static String editGoals = "On";

    static String triggers = "On";
    static JobScheduler jobScheduler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = sdf.format(new Date());

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        /** Set Default Settings */
        Cursor cursor_set = getContentResolver().query(ContentProviderDB.CONTENT_URI_S, null, null, null, "_id");

        if (cursor_set.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(ContentProviderDB.S_UNITS, units);
            values.put(ContentProviderDB.S_MODE, mode);
            values.put(ContentProviderDB.S_NOTIFICATIONS, notifications);
            values.put(ContentProviderDB.S_EDITGOAL, editGoals);
            getContentResolver().insert(ContentProviderDB.CONTENT_URI_S, values);
            Toast.makeText(this, "Default values!", Toast.LENGTH_SHORT).show();
            cursor_set.close();
        } else {
            if (cursor_set.moveToFirst()) {
                do {
                    units = cursor_set.getString(cursor_set.getColumnIndex(ContentProviderDB.S_UNITS));
                    mode = cursor_set.getString(cursor_set.getColumnIndex(ContentProviderDB.S_MODE));
                    notifications = cursor_set.getString(cursor_set.getColumnIndex(ContentProviderDB.S_NOTIFICATIONS));
                    editGoals = cursor_set.getString(cursor_set.getColumnIndex(ContentProviderDB.S_EDITGOAL));
                } while (cursor_set.moveToNext());
            }
            Toast.makeText(this, "Activities Retrieved!", Toast.LENGTH_SHORT).show();
            cursor_set.close();
        }


        /** This feature tracks total number of steps since the last device reboot and triggers an event on change in the step count */
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


        Fragment fragment = new Nav_ChooseHome();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.cmfragm, fragment);
        ft.commit();


        /****************************************** FAB ******************************************/
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(final View view) {

                                       Fragment fragment;
                                       FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                       fragment = new Fab_AddActivityFr();
                                       ft.replace(R.id.cmfragm, fragment);
                                       ft.addToBackStack(null);
                                       ft.commit();
                                   }
                               }
        );


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        /**************************************** Triggers ****************************************/


        jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(getPackageName(),
                    TurnOnTriggers.class.getName()));

        builder.setPeriodic(5000);
        jobScheduler.schedule(builder.build());

    }





    /****************************************** Sensors ******************************************/
    @Override
    protected void onResume() {
        super.onResume();
        activityRunning = true;

        dateFlag = true;

        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityRunning = false;
        // if you unregister the last listener, the hardware will stop detecting step events
//        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning) {
            if (mode.equals("Auto"))
                sensStepCounter = event.values[0];
            if (dateFlag) {
                dateFlag = false;
                firstSensStepCounter = sensStepCounter;
            }
            tempSensStepCounter = 1+sensStepCounter - firstSensStepCounter;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            MainActivity.currentDate = sdf.format(new Date());

            /**
             * Check if I can have dateFlag=true (firstSensStepCounter = sensStepCounter;) in onResume and just add
             * tempSensStepCounter in MainActivity every time!!
             * */

            if (tempSensStepCounter%10 == 0) {
                dateFlag = true;
                Cursor cursorR = getContentResolver().query(ContentProviderDB.CONTENT_URI_R, null, "date like '%" + MainActivity.currentDate + "%'", null, "_id");
                double tempSteps = 0;
                String tempID = "";
                if(cursorR.moveToFirst()){
                    do{
                        tempSteps =  Double.parseDouble(cursorR.getString(cursorR.getColumnIndex(ContentProviderDB.R_STEPS)));
                        tempID = cursorR.getString(cursorR.getColumnIndex(ContentProviderDB.R_ID));
                    }
                    while (cursorR.moveToNext());
                }

                stepCounter = tempSteps + Converter.ConvertUnits("Steps", units, tempSensStepCounter);

                ContentValues values = new ContentValues();
                values.put(ContentProviderDB.DATE, currentDate);
                String[] curDatenumAr = currentDate.split("-");
                String curDateN = curDatenumAr[0]+curDatenumAr[1]+curDatenumAr[2];
                int curDateInt = Integer.parseInt(curDateN);
                values.put(ContentProviderDB.DATENUM, curDateInt);

                if (stepCounter >= goalStepCounter) {

                    /** Notification */
                    if (notifications.equals("On")) {

                        Intent intent = new Intent(this, NotificationActivity.class);
                        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

                        Notification notif = new Notification.Builder(this)
                                .setContentTitle("Goal Reached!")
                                .setContentText("" + MainActivity.stepCounter + " " + MainActivity.units).setSmallIcon(R.drawable.runningicon)
                                .setContentIntent(pIntent).build();
                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        // hide the notification after its selected
                        notif.flags |= Notification.FLAG_AUTO_CANCEL;

                        notificationManager.notify(0, notif);
                    }
                    Toast.makeText(this, "Goal Achieved", Toast.LENGTH_SHORT).show();
                    values.put(ContentProviderDB.SUCCESSFUL, "V");

                } else
                    values.put(ContentProviderDB.SUCCESSFUL, "X");

                values.put(ContentProviderDB.R_GOAL_OTD, "" + Nav_ChooseHome.idGoalSelected);
                values.put(ContentProviderDB.R_REC_UNIT, "" + units);

                if (cursorR.getCount() == 0) {   //Insert
                    values.put(ContentProviderDB.R_STEPS, String.valueOf(stepCounter));
                    getContentResolver().insert(ContentProviderDB.CONTENT_URI_R, values);
                } else {                           // Update
                    values.put(ContentProviderDB.R_STEPS, "" + stepCounter);
                    getContentResolver().update(ContentProviderDB.CONTENT_URI_R, values, "_id = "+ tempID, null);
                }

                Toast.makeText(this, "Steps add up", Toast.LENGTH_SHORT).show();

                Fragment fragment = new Nav_ChooseHome();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.cmfragm, fragment);
                ft.commit();
            }
        } //date flag changes to true every new day.
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    /**********************************************************************************************/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


    /************************************* THE TOP RIGHT MENU *************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Fragment fragment = null;


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            fragment = new SettingsFr();
            ft.replace(R.id.cmfragm, fragment);
            ft.addToBackStack(null);
            ft.commit();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /************************************** NAVIGATION MENU **************************************/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        Fragment fragment = null;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();


        if (id == R.id.nav_home) {
            fragment = new Nav_ChooseHome();
            ft.replace(R.id.cmfragm, fragment);
//            ft.addToBackStack(null);

        } else if (id == R.id.nav_history) {

            fragment = new Nav_ChooseHistory();
            ft.replace(R.id.cmfragm, fragment);
            ft.addToBackStack(null);

        } else if (id == R.id.nav_see_saved_goal) {
            fragment = new Nav_SeeSavedGoalsFr();
            ft.replace(R.id.cmfragm, fragment);
            ft.addToBackStack(null);

        } else if (id == R.id.nav_statistics) {
            fragment = new Nav_Statistics();
            ft.replace(R.id.cmfragm, fragment);
            ft.addToBackStack(null);
        } else if (id == R.id.nav_manage) {
            Toast.makeText(this, "Under Construction!\nClick on  ⁞  for Settings", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_test) {
            fragment = new Nav_TestMode_1Pick();
            ft.replace(R.id.cmfragm, fragment);
            ft.addToBackStack(null);
        }

        ft.commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
