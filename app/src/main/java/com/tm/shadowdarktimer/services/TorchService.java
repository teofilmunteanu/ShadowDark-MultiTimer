package com.tm.shadowdarktimer.services;

import android.text.Editable;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TorchService {
    public static String timeToString(LocalTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return time.format(formatter);
    }

    public static void formatTorchTime(Editable text){
        String s = text.toString();

        //can't delete comma
        //doesn't really work with length when editing characters before the last

        /*if (s.length() < 3 && s.contains(":")){
            text.insert(0,"0");
        }
        else if (s.length() > 3 && s.length()<6 && s.endsWith(":")){
            text.insert(3,"0");
        }

        if (s.length() == 3 && !s.contains(":")) {
            text.insert(2, ":");
        }
        // Insert a colon after the minutes if it's the sixth character
        else if (s.length() == 6 && !s.endsWith(":")) {
            text.insert(5, ":");
        }*/

        /*if (!Character.isDigit(s.charAt(0))||!Character.isDigit(s.charAt(1))){
            text.insert(0,"0");
        }
        if (s.length() >= 3 && s.charAt(2)!=':'){
            text.insert(2,":");
        }
        if (!Character.isDigit(s.charAt(3))||!Character.isDigit(s.charAt(4))){
            text.insert(0,"0");
        }

        if (s.length() >= 6 && s.charAt(5)!=':'){
            text.insert(5,":");
        }*/

    }
}
