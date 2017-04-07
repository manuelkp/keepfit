package com.example.keepfit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TurnOnTriggers extends JobService {
    private JobParameters params;
    int time = 5000;
    boolean goalFlag = true;

    double todayDistance;
    double todayGoalDistance;

    double yesterdayDistance;

    static int currentTime;
    static String currentDate;

    String selectedUnits;  //gets units from selected goal


    static final String PROVIDER_NAME = "com.example.keepfit.ContentProvider";
    static final String URL_R = "content://" + PROVIDER_NAME + "/records";
    static final Uri CONTENT_URI_REC = Uri.parse(URL_R);


    @Override
    public boolean onStartJob(JobParameters params) {
        this.params = params;
        Toast.makeText(getApplicationContext(), "Checking Progress...", Toast.LENGTH_SHORT).show();
        new CheckTriggers().execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private class CheckTriggers extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            jobFinished(params, false);
            Toast.makeText(getApplicationContext(), "Finish job", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // find hours of the day and do the check.
            SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
            currentTime = Integer.parseInt(sdfTime.format(new Date()));

            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            currentDate = sdfDate.format(new Date());
            String units = "";


                if (currentTime >= 80000 && currentTime <= 230000) {
                    Cursor cursorR = getApplicationContext().getContentResolver().query(CONTENT_URI_REC, null, "date like '%" + currentDate + "%'", null, "_id");

                    if (cursorR.moveToFirst()) {
                        do {
                            todayDistance = Double.parseDouble(cursorR.getString(cursorR.getColumnIndex(ContentProviderDB.R_STEPS)));
                            units = cursorR.getString(cursorR.getColumnIndex(ContentProviderDB.R_REC_UNIT));
                        } while (cursorR.moveToNext());
                    }

                    if (todayDistance <= todayGoalDistance / 2) {



                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), intent, 0);

                        Notification notif = new Notification.Builder(getApplicationContext())
                                .setContentTitle("Progress:")
                                .setContentText("Low Activity! Why don't you take a walk?").setSmallIcon(R.drawable.runningicon)
                                .setContentIntent(pIntent).build();
                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        // hide the notification after its selected
                        notif.flags |= Notification.FLAG_AUTO_CANCEL;

                        notificationManager.notify(0, notif);
                    }
                }


                if (currentTime >= 80000 && currentTime <= 140000) {
                    if (goalFlag) {
                        goalFlag = false; // so that it appears only once per day
                        String previousDay = previousDay(currentDate);
                        Cursor cursorR = getApplicationContext().getContentResolver().query(CONTENT_URI_REC, null, "date like '%" + previousDay + "%'", null, "_id");

                        if (cursorR.moveToFirst()) {
                            do {
                                yesterdayDistance = Double.parseDouble(cursorR.getString(cursorR.getColumnIndex(ContentProviderDB.R_STEPS)));
                                units = cursorR.getString(cursorR.getColumnIndex(ContentProviderDB.R_REC_UNIT));
                            } while (cursorR.moveToNext());
                        }

                        if (!units.equals(MainActivity.units)) {
                            yesterdayDistance = ConvertUnits(units, selectedUnits, yesterdayDistance);
                        }
                        if (todayGoalDistance <= yesterdayDistance) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), intent, 0);

                            Notification notif = new Notification.Builder(getApplicationContext())
                                    .setContentTitle("Set a new goal:")
                                    .setContentText("Your activities seem to be higher! Why don't you set a new goal?").setSmallIcon(R.drawable.runningicon)
                                    .setContentIntent(pIntent).build();
                            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            // hide the notification after its selected
                            notif.flags |= Notification.FLAG_AUTO_CANCEL;

                            notificationManager.notify(0, notif);
                        }

                    }
                }

            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }


        public String previousDay(String currentDate) {

            String[] dateArray = currentDate.split("-");


            int thisDay =  Integer.parseInt(dateArray[0]);
            int thisMonth = Integer.parseInt(dateArray[1]);
            int thisYear = Integer.parseInt(dateArray[2]);
            int prevDay, prevMonth, prevYear;

            if(thisDay>1){
                prevDay = thisDay-1;
                prevMonth = thisMonth;
                prevYear = thisYear;
            }
            else{
                if(thisMonth==1){
                    prevDay = 31-1+thisDay;
                    prevMonth = 12;
                    prevYear = thisYear-1;
                }
                if(thisMonth==3){
                    prevDay = 28-1+thisDay;
                    prevMonth = 2;
                    prevYear = thisYear;
                }
                else if(thisMonth==2 ||thisMonth==4 || thisMonth==6 || thisMonth==7 || thisMonth==9 || thisMonth==11 ){
                    prevDay = 31-1+thisDay;
                    prevMonth = thisMonth-1;
                    prevYear = thisYear;
                }
                else{
                    prevDay = 30-1+thisDay;
                    prevMonth = thisMonth-1;
                    prevYear = thisYear;
                }
            }

            return prevYear+"-"+prevMonth+"-"+prevDay;


        }


        public double ConvertUnits(String oldUn, String NewUn, double rec) {

            double record = rec;
            if (!oldUn.equals(NewUn)) {
                if (oldUn.equals("Steps")) {
                    if (NewUn.equals("Kilometres")) record /= 1312.3;
                    else if (NewUn.equals("Miles")) record *= 0.00047;
                    else if (NewUn.equals("Yards")) record /= 1.2;

                } else if (oldUn.equals("Kilometres")) {
                    if (NewUn.equals("Steps"))      record *= 1312.3;
                    else if (NewUn.equals("Miles")) record /= 1.61;
                    else if (NewUn.equals("Yards")) record *= 1093.6;

                } else if (oldUn.equals("Miles")) {
                    if (NewUn.equals("Kilometres")) record *= 1.61;
                    else if (NewUn.equals("Steps")) record /= 0.00047;
                    else if (NewUn.equals("Yards")) record *= 1760;

                } else if (oldUn.equals("Yards")) {
                    if (NewUn.equals("Kilometres")) record /= 1093.6;
                    else if (NewUn.equals("Miles")) record /= 1760;
                    else if (NewUn.equals("Steps")) record *= 1.2;
                }
            }

            return record;
        }
    }

}
