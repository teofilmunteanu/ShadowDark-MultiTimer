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

import com.tm.shadowdarktimer.adapters.TorchObserver;
import com.tm.shadowdarktimer.models.TorchModel;

public class AutoformattingTorchTime extends androidx.appcompat.widget.AppCompatEditText implements TorchObserver {
    public static final int MAX_TIMER_COLONS = 2;
    public static final int TIMER_COLON1_POS = 2;
    public static final int TIMER_COLON2_POS = 5;
    public static final int TIME_ELEMENT_LENGTH = 2;

    public static boolean programmaticallyAddedZero = false;

    private TorchModel torchModel;

    private TextWatcher textChangeListener;

    public AutoformattingTorchTime(Context context, AttributeSet attrs){
        super(context, attrs);
        addTorchTimeFilters();
        addTorchTimeEvents();
    }

    public void setModel(TorchModel model){
        torchModel = model;
        torchModel.addPauseObserver(this);
    }

    @Override
    public void onPausedChanged(boolean paused) {
        if (paused) {
            addTorchTimeFilters();
            addTorchTimeEvents();
        } else {
            setFilters(new InputFilter[0]);
            setOnTouchListener(null);
            removeTextChangedListener(textChangeListener);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        torchModel.removePauseObserver(this);
    }

    //One way to do it - not clean -> use observers
    /*@Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (enabled) {
            addTorchTimeFilters();
            addTorchTimeEvents();
        } else {
            setFilters(new InputFilter[0]);
            setOnTouchListener(null);
            removeTextChangedListener(textChangeListener);
        }
    }*/

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

                //if you add a character after a colon, or after the second digit of a time element,(it's the 10s digit of min/sec),
                // doesn't allow the digit to be bigger than 5 (only digits, so c <= 9)
                if (dest.length()>=dstart && dstart>=2){
                    if (dest.charAt(dstart-1)==':' || Character.isDigit(dest.charAt(dstart-2))) {
                        if (c > '5' && c <= '9') {
                            return "";
                        }
                    }
                }

                if (dest.length()>=1 && Character.isDigit(dest.charAt(0))){
                    if ((dstart == 1 && Integer.parseInt(dest.charAt(0) + "" + c) > 24 ||
                            dstart == 0 && Integer.parseInt(c + "" + dest.charAt(0)) > 24)){
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

        textChangeListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            //when the user inputs :, and the hours or mins are single digit, leading 0s are added
            @Override
            public void afterTextChanged(Editable text) {
                if (!programmaticallyAddedZero){
                    colonFormatTorchTime();
                    colonSkipInput();
                }
            }
        };
        this.addTextChangedListener(textChangeListener);


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

    //inserts leading 0s until the time element has length 2
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

    //insert leading 0s everywhere
    public void insertTimeLeadingZeros(){
        insertTimeLeadingZeros(0);
        insertTimeLeadingZeros(TIMER_COLON1_POS + 1);
        insertTimeLeadingZeros(TIMER_COLON2_POS + 1);
    }

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
                programmaticallyAddedZero = true;

                if (lastColonPos < TIMER_COLON1_POS && inputText.length() <= TIMER_COLON1_POS){
                    insertTimeLeadingZeros(0);
                }
                else if (lastColonPos < TIMER_COLON2_POS && inputText.length() <= TIMER_COLON2_POS){
                    insertTimeLeadingZeros(TIMER_COLON1_POS+1);
                }

                programmaticallyAddedZero = false;
            }
        }
    }
}
