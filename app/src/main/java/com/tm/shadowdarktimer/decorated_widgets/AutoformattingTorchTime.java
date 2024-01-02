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
import java.time.format.DateTimeParseException;

public class AutoformattingTorchTime extends androidx.appcompat.widget.AppCompatEditText {
    public static final int MAX_TIMER_COLONS = 2;
    public static final int TIMER_COLON1_POS = 2;
    public static final int TIMER_COLON2_POS = 5;
    public static final int TIME_ELEMENT_LENGTH = 2;

    public static boolean programaticallyAddedZero = false;

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

        //!!!!!!!!! One solution for the colon skip input problems ---- not that clean
        /*InputFilter colonsBeforeColonsFilter = (charSequence, start, end, dest, dstart, dend) -> {
            if(start<end) {
                if (charSequence.charAt(start) == ':') {
                    //if the colon is not added at the end of the input
                    if (dend < dest.length()){
                        if (dest.charAt(dstart) == ':'){
                            return "";
                        }
                    }
                }
            }
            return null;
        };*/

        //filter out invalid characters
        InputFilter digitFilter = (charSequence, start, end, dest, dstart, dend) -> {
            //if character is changed/added, not removed
            if(start < end){
                char c = charSequence.charAt(start);

                //filter out letters and symbols other than colons
                if(!Character.isDigit(c) && c != ':') {
                    return "";
                }

                //if you add a character after a colon, or after the second digit of a time element,(it's the 10s digit of min/sec),
                // doesn't allow the digit to be bigger than 5
                if (dest.length()>=dstart && dstart>=2){
                    if (dest.charAt(dstart-1)==':' || Character.isDigit(dest.charAt(dstart-2))) {
                        if (Character.getNumericValue(c) > 5) {
                            return "";
                        }
                    }
                }
            }

            return null;
        };

        InputFilter lengthFilter = new InputFilter.LengthFilter(8);

        // Set the filters on the EditText
        this.setFilters(new InputFilter[]{/*colonsBeforeColonsFilter,*/extraColonFilter,digitFilter,lengthFilter});
    }

    @SuppressLint("ClickableViewAccessibility")
    public void addTorchTimeEvents(){
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("beftextch",s+" "+start+" "+after+" "+count);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("ontextch",s+" "+start+" "+before+" "+count);
            }

            //when the user inputs :, and the hours or mins are single digit, leading 0s are added
            @Override
            public void afterTextChanged(Editable text) {
                if (!programaticallyAddedZero){
                    colonFormatTorchTime();
                    colonSkipInput();
                }
            }
        });

        this.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP){
                EditText input = (EditText) view;

                if(input.getText().length() > 1){
                    insertTimeLeadingZeros();
                }
            }

            return false;
        });
    }

    //inserts leading 0s until each time element has length 2
    public void insertTimeLeadingZeros(int firstDigitIndex){
        Editable inputText = this.getText();

        if (inputText!=null){
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
                // for the "ss" time element, or "mm" is there is no "ss"
                else{
                    int prevColonPos = inputText.toString().lastIndexOf(':');

                    if (prevColonPos != -1 && inputText.length() >= prevColonPos+1){
                        while(inputText.subSequence(prevColonPos+1,inputText.length()).length() < TIME_ELEMENT_LENGTH){
                            inputText.insert(prevColonPos+1, "0");
                        }
                    }
                }
            }
        }
    }

    public void insertTimeLeadingZeros(){
        insertTimeLeadingZeros(0);
        insertTimeLeadingZeros(TIMER_COLON1_POS + 1);
        insertTimeLeadingZeros(TIMER_COLON2_POS + 1);
    }

    //!!!!!!!! when going from 00:00, to 0:00, it becomes 0::00, when clicking once 00::00, then 00::0000
    // inserts colons automatically when inputting time digits
    public void colonFormatTorchTime(){
        Editable inputText = this.getText();

        if (inputText !=null){
            // can't check if the length is just equal, since the colon would be deletable when there are digits after it
            if (inputText.length() >= TIMER_COLON1_POS + 1){
                if (inputText.charAt(TIMER_COLON1_POS)!=':' && Character.isDigit(inputText.charAt(0)) && Character.isDigit(inputText.charAt(1))){
                    inputText.insert(TIMER_COLON1_POS,":");
                }
            }

            if (inputText.length() >= TIMER_COLON2_POS + 1){
                if (inputText.charAt(TIMER_COLON2_POS)!=':' && Character.isDigit(inputText.charAt(3)) && Character.isDigit(inputText.charAt(4))){
                    inputText.insert(TIMER_COLON2_POS,":");
                }
            }
        }
    }

    // inserts leading 0s when inputting colons, for faster inputs
    public void colonSkipInput(){
        Editable inputText = this.getText();

        if (inputText!=null){
            int lastColonPos = inputText.toString().lastIndexOf(':');

            if (lastColonPos != -1){
                programaticallyAddedZero = true;

                if (lastColonPos < TIMER_COLON1_POS && inputText.length() <= TIMER_COLON1_POS){
                    insertTimeLeadingZeros(0);
                }
                else if (lastColonPos < TIMER_COLON2_POS && inputText.length() <= TIMER_COLON2_POS){
                    insertTimeLeadingZeros(TIMER_COLON1_POS+1);
                }

                programaticallyAddedZero = false;
            }
        }
    }

    public boolean isValidTimeString(){
        try {
            String timeString = this.getText()+"";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime.parse(timeString, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
