package com.example.keepfit;


public class Converter {
    public static double ConvertUnits(String oldUn, String NewUn, double rec) {

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



    public static String LastWeek(int day, int month, int year) {

        String last_week="" ;
        int startingDay;
        int startingMonth;
        int startingYear;

        if(day>7){
            startingDay = day-7;
            startingMonth = month;
            startingYear = year;
        }
        else{
            if(month==1){
                startingDay = 31-7+day;
                startingMonth = 12;
                startingYear = year-1;
            }
            if(month==3){
                startingDay = 28-7+day;
                startingMonth = 2;
                startingYear = year;
            }
            else if(month==2 ||month==4 || month==6 || month==7 || month==9 || month==11 ){
                startingDay = 31-7+day;
                startingMonth = month-1;
                startingYear = year;
            }
            else{
                startingDay = 30-7+day;
                startingMonth = month-1;
                startingYear = year;
            }
        }

        return last_week = startingYear+"-"+startingMonth+"-"+startingDay;
    }

    public static String previousDay(String currentDate) {

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


    public static String LastMonth(int day, int month, int year) {

        String last_month="";
        int startingDay = 1;
        int startingMonth = month;
        int startingYear = year;

        return last_month = startingYear+"-"+startingMonth+"-"+startingDay;
    }

    public static int DaysToInt(String date) {

        String strDays = "";
        String[] dateArray = date.split("-");
        strDays = dateArray[0] + dateArray[1] + dateArray[2];

        return Integer.parseInt(strDays);
    }

    public static int TimeToInt(String time) {

        String strTime = "";
        String[] timeArray = time.split(":");
        strTime = timeArray[0] + timeArray[1] + timeArray[2];

        return Integer.parseInt(strTime);
    }

    public static boolean isNumeric(String number) {

        try{
            double d = Double.parseDouble(number);
        }
        catch(NumberFormatException nfe){
            return false;
        }
        return true;
    }


}
