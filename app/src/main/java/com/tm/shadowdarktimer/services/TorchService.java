package com.tm.shadowdarktimer.services;

import android.text.Editable;
import android.util.Log;
import android.widget.EditText;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TorchService {
    public static final int MAX_TIMER_COLONS = 2;
    public static final int TIMER_COLON1_POS = 2;
    public static final int TIMER_COLON2_POS = 5;
    public static final int TIME_ELEMENT_LENGTH = 2;

    public static String timeToString(LocalTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return time.format(formatter);
    }

    //inserts leading 0s until each time element has length 2
    public static void insertTimeLeadingZeros(Editable inputText, int firstDigitIndex){
        // !!!!!!!!!!!!!!! instead of checking each digit (which might not exist if the colon is first), check if there are digits before the colon (maybe between colons)

        if(firstDigitIndex >= 0){
            if (firstDigitIndex < TIMER_COLON2_POS){
                int nextColonPos = inputText.toString().indexOf(':',firstDigitIndex);

                if (nextColonPos != -1){
                    while(inputText.subSequence(firstDigitIndex,nextColonPos).length() < TIME_ELEMENT_LENGTH){
                        inputText.insert(firstDigitIndex, "0");
                        nextColonPos++;
                    }
                }
            }
            else{
                while(inputText.subSequence(firstDigitIndex,inputText.length()).length() < TIME_ELEMENT_LENGTH){
                    inputText.insert(firstDigitIndex, "0");
                }
            }
        }
    }

    //adds colons automatically when inputting time digits
    public static void colonFormatTorchTime(Editable inputText){
        if (inputText.length() >= 3){
            if (inputText.charAt(2)!=':'/* && Character.isDigit(inputText.charAt(0)) && Character.isDigit(inputText.charAt(1))*/){
                inputText.insert(2,":");
            }
        }

        if (inputText.length() >= 6){
            if (inputText.charAt(5)!=':'/* && Character.isDigit(inputText.charAt(3)) && Character.isDigit(inputText.charAt(4))*/){
                inputText.insert(5,":");
            }
        }
    }


    // !!!! doesn't work with the current leading zero insert method
    public static void colonSkipInput(Editable inputText){
        int lastColonPos = inputText.toString().lastIndexOf(':');
        if (lastColonPos != -1){
            if (lastColonPos < 2 && inputText.length() <= 2){
                insertTimeLeadingZeros(inputText, 0);
            }
            else if (lastColonPos < 5 && inputText.length() <= 5){
                insertTimeLeadingZeros(inputText, 3);
            }
        }
    }
}
