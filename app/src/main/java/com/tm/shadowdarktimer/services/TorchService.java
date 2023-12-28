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

    public static void insertTimeLeadingZeros(Editable inputText, int firstDigitIndex){
        // !!!!!!!!!!!!!!! instead of checking each digit (which might not exist if the colon is first), check if there are digits before the colon (maybe between colons)

        if(firstDigitIndex >= 0){
            if (inputText.length()>firstDigitIndex+1){
                while(!Character.isDigit(inputText.charAt(firstDigitIndex)) || !Character.isDigit(inputText.charAt(firstDigitIndex+1))){
                    inputText.insert(firstDigitIndex,"0");
                }
            }
            else if (inputText.length() >= 6){
                while(inputText.length()<8){
                    inputText.append('0');
                }
            }
        }
    }

    public static void colonFormatTorchTime(Editable inputText){
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


    // !!!! doesn't work with the current leading zero insert method
    public static void colonSkipInput(Editable inputText){
        int lastColonPos = inputText.toString().lastIndexOf(':');
        if (lastColonPos < 2){
            insertTimeLeadingZeros(inputText, 0);
        }
        else if (lastColonPos < 5){
            insertTimeLeadingZeros(inputText, 3);
        }
    }
}
