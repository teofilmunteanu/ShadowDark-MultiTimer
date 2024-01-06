package com.tm.shadowdarktimer.decorated_widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;

public class AutoformattingTimeChange extends androidx.appcompat.widget.AppCompatEditText {
    public static boolean deletionAllowed = false;
    public static boolean programmaticChange = false;


    public AutoformattingTimeChange(Context context, AttributeSet attrs){
        super(context, attrs);
        addTorchTimeFilters();
        addTorchTimeEvents();
    }

    public void addTorchTimeFilters(){

        InputFilter noDeletionFilter = (charSequence, start, end, dest, dstart, dend) ->
            (charSequence.length() == 0 && dstart < dend && !deletionAllowed) ? dest.subSequence(dstart, dend) : null;

        InputFilter digitFilter = (charSequence, start, end, dest, dstart, dend) ->
                (dend > dstart || Character.isDigit(charSequence.charAt(0))) ? null : "";

        InputFilter maxDigitFilter = (charSequence, start, end, dest, dstart, dend) -> {
            if (dend <= dstart && dstart > 2 &&
                dest.charAt(dstart-2)==':' &&
                charSequence.charAt(start) > '5'){
                return "0";
            }
            else{
                return null;
            }
        };

        // Set the filters on the EditText
        this.setFilters(new InputFilter[]{maxDigitFilter,noDeletionFilter,digitFilter});
    }

    @SuppressLint("ClickableViewAccessibility")
    public void addTorchTimeEvents(){
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable text) {
                // replace the last digit with the new input, and move the old digit to the left
                //00:00 -> 00:005 -> 00:05 -> 00:053 -> 00:53
                //if a digit is "pushed into the colon", delete it

                // if the last digit was bigger than 5 (meaning it would go to the 10s position), change it to 0
                //00:00 -> 00:007 -> 00:07 -> 00:073 -> 00:03

                if (!programmaticChange){
                    formatNewInput();
                }
            }
        });
    }

    void formatNewInput(){
        Editable inputText = this.getText();
        int colonPos = inputText.toString().indexOf(":");

        programmaticChange = true;

        //if the ss element has more than 2 digits, "push" them towards the colon
        if (inputText.subSequence(colonPos + 1, inputText.length()).length() > 2){
            deletionAllowed = true;

           //delete digit after colon
            inputText.delete(colonPos+1,colonPos+2);

            char charAfterColon = inputText.charAt(colonPos + 1);
            if (Character.isDigit(charAfterColon) && charAfterColon > '5') {
               // Replace the digit after colon with '0' if > 5
               inputText.replace(colonPos + 1, colonPos + 2, "0");
            }

            deletionAllowed = false;
        }

        if (inputText.subSequence(0, colonPos).length() > 2){
            deletionAllowed = true;

            //delete 1st digit
            inputText.delete(0,1);

            char firstChar = inputText.charAt(0);
            if (Character.isDigit(firstChar) && firstChar > '5') {
                // Replace the first digit with '0' if > 5
                inputText.replace(0, 1, "0");
            }

            deletionAllowed = false;
        }

        programmaticChange = false;
    }
}
