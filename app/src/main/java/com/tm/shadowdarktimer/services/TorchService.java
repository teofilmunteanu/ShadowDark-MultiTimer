package com.tm.shadowdarktimer.services;

import android.text.Editable;
import android.util.Log;
import android.widget.EditText;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TorchService {
    public static String timeToString(LocalTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return time.format(formatter);
    }

    /*public static String stringInsert(String originalString, String insertedString, int index)
    {
        StringBuilder newString = new StringBuilder(originalString);

        newString.insert(index, insertedString);

        return newString.toString();
    }*/

    public static void insertTimeLeadingZeros(Editable inputText, int index){
        //String inputStr = inputText.toString();

        while(!Character.isDigit(inputText.charAt(index)) || !Character.isDigit(inputText.charAt(index+1))){
            //inputStr = stringInsert(inputStr, "0",index);
            inputText.insert(index,"0");
        }
    }

    public static void formatTorchTime(Editable inputText){
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

        /*if (inputText.length() >= 3 && inputText.charAt(2)!=':'){
            inputText.insert(2,":");
        }

        if (inputText.length() >= 6 && inputText.charAt(5)!=':'){
            inputText.insert(5,":");
        }*/


        //crashes in some instances ---- what should be the behaviour tho????
        if (inputText.length() >= 3){
            if (inputText.charAt(2)!=':' && Character.isDigit(inputText.charAt(0)) && Character.isDigit(inputText.charAt(1))){
                inputText.insert(2,":");
            }
        }

        if (inputText.length() >= 6){
            if (inputText.charAt(5)!=':' && Character.isDigit(inputText.charAt(3)) && Character.isDigit(inputText.charAt(4))){
                inputText.insert(5,":");
            }
        }
    }
}
