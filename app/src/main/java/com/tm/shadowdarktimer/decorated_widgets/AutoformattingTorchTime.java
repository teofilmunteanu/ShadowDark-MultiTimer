package com.tm.shadowdarktimer.decorated_widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AutoformattingTorchTime extends androidx.appcompat.widget.AppCompatEditText {
    public static final int MAX_TIMER_COLONS = 2;
    public static final int TIMER_COLON1_POS = 2;
    public static final int TIMER_COLON2_POS = 5;
    public static final int TIME_ELEMENT_LENGTH = 2;

    public AutoformattingTorchTime(Context context, AttributeSet attrs){
        super(context, attrs);
        addTorchTimeFilters();
        addTorchTimeEvents();
    }

    public void addTorchTimeFilters(){
        //filter out colons after the first two
        InputFilter extraColonFilter = (charSequence, start, end, dest, dstart, dend) -> {
            long nrOfColons = dest.chars().filter(ch -> ch == ':').count();
            if (charSequence.equals(":") && nrOfColons == AutoformattingTorchTime.MAX_TIMER_COLONS){
                return "";
            }

            return null;
        };

        //filter out invalid characters
        InputFilter digitFilter = (charSequence, start, end, dest, dstart, dend) -> {
            //if character is changed/added, not removed
            if(start < end){
                char c = charSequence.charAt(start);

                //filter out letters and symbols other than colons
                if(!Character.isDigit(c) && c != ':') {
                    return "";
                }

                //if you add a character after the 3rd or 6th (which are colons), meaning it's the 10s digit of a minute or second
                //don't allow the digit to be bigger than 5

                //!!!!!! if the colon is added automatically, it doesn't work
                Log.d("digit-filter start finish",dest+" "+dstart);
                if (dstart == 3 || dstart == 6) {
                    if (Character.getNumericValue(c) > 5) {
                        return "";
                    }
                }
            }

            return null;
        };

        InputFilter lengthFilter = new InputFilter.LengthFilter(8);

        // Set the filters on the EditText
        this.setFilters(new InputFilter[]{extraColonFilter,digitFilter,lengthFilter});
    }

    @SuppressLint("ClickableViewAccessibility")
    public void addTorchTimeEvents(){
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            //when the user inputs :, and the hours or mins are single digit, leading 0s are added
            @Override
            public void afterTextChanged(Editable text) {
                colonFormatTorchTime(text);
                colonSkipInput(text);
            }
        });

        this.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP){
                EditText input = (EditText) view;

                if(input.getText().length() > 1){
                    int oldCursorPos = this.getSelectionStart();

                    Editable inputText = input.getText();
                    if (oldCursorPos <= TIMER_COLON1_POS){
                        insertTimeLeadingZeros(inputText,0);
                    }
                    else if(oldCursorPos <= TIMER_COLON2_POS){
                        insertTimeLeadingZeros(inputText, TIMER_COLON1_POS + 1);
                    }
                    else{
                        insertTimeLeadingZeros(inputText, TIMER_COLON2_POS + 1);
                    }
                    input.setText(inputText);
                }
            }
            return false;
        });
    }

    public static String timeToString(LocalTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return time.format(formatter);
    }

    //inserts leading 0s until each time element has length 2
    public static void insertTimeLeadingZeros(Editable inputText, int firstDigitIndex){
        if(firstDigitIndex >= 0){
            // for "HH" and "mm" elements
            if (firstDigitIndex < TIMER_COLON2_POS){
                int nextColonPos = inputText.toString().indexOf(':',firstDigitIndex);

                if (nextColonPos != -1){
                    while(inputText.subSequence(firstDigitIndex,nextColonPos).length() < TIME_ELEMENT_LENGTH){
                        inputText.insert(firstDigitIndex, "0");
                        nextColonPos++;
                    }
                }
            }
            // for the "ss" time element
            else{
                while(inputText.subSequence(firstDigitIndex,inputText.length()).length() < TIME_ELEMENT_LENGTH){
                    inputText.insert(firstDigitIndex, "0");
                }
            }
        }
    }

    // inserts colons automatically when inputting time digits
    public static void colonFormatTorchTime(Editable inputText){
        if (inputText.length() >= TIMER_COLON1_POS + 1){
            if (inputText.charAt(TIMER_COLON1_POS)!=':'){
                inputText.insert(TIMER_COLON1_POS,":");
            }
        }

        if (inputText.length() >= TIMER_COLON2_POS + 1){
            if (inputText.charAt(TIMER_COLON2_POS)!=':'){
                inputText.insert(TIMER_COLON2_POS,":");
            }
        }
    }


    // inserts leading 0s when inputting colons, for faster inputs
    public static void colonSkipInput(Editable inputText){
        int lastColonPos = inputText.toString().lastIndexOf(':');
        if (lastColonPos != -1){
            if (lastColonPos < TIMER_COLON1_POS && inputText.length() <= TIMER_COLON1_POS){
                insertTimeLeadingZeros(inputText, 0);
            }
            else if (lastColonPos < TIMER_COLON2_POS && inputText.length() <= TIMER_COLON2_POS){
                insertTimeLeadingZeros(inputText, TIMER_COLON1_POS+1);
            }
        }
    }
}
