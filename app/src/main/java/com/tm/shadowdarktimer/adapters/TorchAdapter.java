package com.tm.shadowdarktimer.adapters;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tm.shadowdarktimer.R;
import com.tm.shadowdarktimer.models.TorchModel;
import com.tm.shadowdarktimer.services.TorchService;

import java.util.ArrayList;

public class TorchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<TorchModel> torchList;
    private static final int TORCH_TYPE = 0;
    private static final int ADD_BUTTON_TYPE = 1;


    public TorchAdapter(ArrayList<TorchModel> torchList) {
        this.torchList = torchList;
    }

    @Override
    public int getItemViewType(int position){
        return (position == getItemCount() - 1) ? ADD_BUTTON_TYPE : TORCH_TYPE ;
    }

    @Override
    public int getItemCount() {
        //added one for the Add-Button
        return torchList.size() + 1;
    }

    public static String stringInsert(String originalString, String insertedString, int index)
    {
        StringBuilder newString
                = new StringBuilder(originalString);

        newString.insert(index, insertedString);

        return newString.toString();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case TORCH_TYPE:
                View torchView = inflater.inflate(R.layout.layout_torch, parent, false);

                TorchViewHolder torchHolder = new TorchViewHolder(torchView);

                //click event listener for play/pause button
                torchHolder.play_pauseButton.setOnClickListener(view -> {
                    int position = torchHolder.getBindingAdapterPosition();
                    TorchModel torch  = torchList.get(position);
                    torch.pauseUnpause();
                    torchHolder.play_pauseButton.setText(torch.isPaused() ? R.string.play_label : R.string.pause_label);
                });

                //doesn't allow colon after the first two
                InputFilter extraColonFilter = (charSequence, start, end, dest, dstart, dend) -> {
                    if (charSequence.equals(":") && dstart > 5) {
                        return "";
                    }

                    return null;
                };
                InputFilter digitFilter = (charSequence, start, end, dest, dstart, dend) -> {
                    //if character is changed/added, not removed
                    if(start < end){
                        char c = charSequence.charAt(start);
                        if(Character.isDigit(c) || c == ':') {
                            //if you add a character after the 3rd or 6th (which are colons), meaning it's the 10s digit of a minute or second
                            //don't allow the digit to be bigger than 5
                            if (dstart == 3 || dstart == 6) {
                                if (Character.getNumericValue(c) > 5) {
                                    return "";
                                }
                            }
                        }
                        else{
                            return "";
                        }
                    }

                    return null;
                };
                InputFilter lengthFilter = new InputFilter.LengthFilter(8);

                // Set the InputFilter to the EditText
                torchHolder.totalTimeInput.setFilters(new InputFilter[]{extraColonFilter,digitFilter,lengthFilter});

                //make it so, when user inputs :, and the hours or mins are single digit, to include a 0 at the beginning
                torchHolder.totalTimeInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable text) {
                        String s = text.toString();
                        //TorchService.formatTorchTime(text);

                        /*int cursorPos = torchHolder.totalTimeInput.getSelectionStart();

                        if (cursorPos>5){
                            if (s.charAt(5) != ':'){
                                text.insert(3,"0");
                            }
                        }*/
                        //Log.d("dsad", "cursor pos " + cursorPos);
                    }
                });

                torchHolder.totalTimeInput.setOnTouchListener((view, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_UP){
                        EditText input = (EditText) view;

                        if(input.getText().length() > 1){
                            int oldCursorPos = torchHolder.totalTimeInput.getSelectionStart();

                            if (oldCursorPos < 3){
                                while(!Character.isDigit(input.getText().charAt(0)) || !Character.isDigit(input.getText().charAt(1))){
                                    input.setText(stringInsert(input.getText()+"", "0",0));
                                }
                            }
                            else if(oldCursorPos < 6){
                                while(!Character.isDigit(input.getText().charAt(3))||!Character.isDigit(input.getText().charAt(4))){
                                    input.setText(stringInsert(input.getText()+"", "0",3));
                                }
                            }
                        }
                    }
                    return false;
                });

                return torchHolder;

            case ADD_BUTTON_TYPE:
                View add_buttonView = inflater.inflate(R.layout.layout_add_button, parent, false);
                return new AddButtonViewHolder(add_buttonView);

            default:
                throw new IllegalArgumentException("Invalid view type");
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // set item views based on the list item at the given position

        if(holder.getItemViewType() == TORCH_TYPE){
            TorchModel torch  = torchList.get(position);
            TorchViewHolder torchHolder = (TorchViewHolder)holder;
            torchHolder.totalTimeInput.setText(TorchService.timeToString(torch.getTime()));
            torchHolder.play_pauseButton.setText(torch.isPaused() ? R.string.play_label : R.string.pause_label);
        }
    }


    public static class TorchViewHolder extends RecyclerView.ViewHolder{
        public EditText totalTimeInput;
        public Button play_pauseButton;
        public Button backwardTimeButton;
        public EditText timeChangeInput;
        public Button forwardTimeButton;

        public TorchViewHolder(@NonNull View itemView) {
            super(itemView);

            totalTimeInput = itemView.findViewById(R.id.totalTime);
            play_pauseButton = itemView.findViewById(R.id.play_pause);
            backwardTimeButton = itemView.findViewById(R.id.backward);
            timeChangeInput = itemView.findViewById(R.id.timeChange);
            forwardTimeButton = itemView.findViewById(R.id.forward);
        }
    }

    public static class AddButtonViewHolder extends RecyclerView.ViewHolder{
        public Button addButton;

        public AddButtonViewHolder(@NonNull View itemView) {
            super(itemView);

            addButton = itemView.findViewById(R.id.add_torchButton);
        }
    }
}
