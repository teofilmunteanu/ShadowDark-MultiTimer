package com.tm.shadowdarktimer.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tm.shadowdarktimer.R;
import com.tm.shadowdarktimer.models.TorchModel;
import com.tm.shadowdarktimer.decorated_widgets.AutoformattingTorchTime;

import java.time.DateTimeException;
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
        //added 1 for the Add-Button
        return torchList.size() + 1;
    }

    @SuppressLint("ClickableViewAccessibility")
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

                    if(torchHolder.totalTimeInput.isValidTimeString()){
                        //!!!!!!!!!!!! START TIMER
                    }
                    else{
                        torch.resetTime();
                        //!!!!!!!! shouldn't it be set automatically it's binded?
                        torchHolder.totalTimeInput.setText(torch.getTimeString());
                    }

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
        // sets item views based on the list item at the given position

        if(holder.getItemViewType() == TORCH_TYPE){
            TorchModel torch  = torchList.get(position);
            TorchViewHolder torchHolder = (TorchViewHolder)holder;
            Log.d("time:",torch.getTimeString());
            torchHolder.totalTimeInput.setText(torch.getTimeString());
            torchHolder.play_pauseButton.setText(torch.isPaused() ? R.string.play_label : R.string.pause_label);
        }
    }


    public static class TorchViewHolder extends RecyclerView.ViewHolder{
        public AutoformattingTorchTime totalTimeInput;
        public Button play_pauseButton;
        public Button backwardTimeButton;
        public EditText timeChangeInput;
        public Button forwardTimeButton;

        public TorchViewHolder(@NonNull View itemView) {
            super(itemView);

            //used decorator pattern to decorate the EditText totalTimeInput
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
