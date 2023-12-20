package com.tm.shadowdarktimer.adapters;

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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case TORCH_TYPE:
                View torchView = inflater.inflate(R.layout.layout_torch, parent, false);
                return new TorchViewHolder(torchView);

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
            torchHolder.totalTimeInput.setText(torch.getTime());
            torchHolder.play_pauseButton.setText(torch.isPaused() ? R.string.pause_label : R.string.play_label);

            torchHolder.play_pauseButton.setOnClickListener(view -> {
                torch.pauseUnpause();
                torchHolder.play_pauseButton.setText(torch.isPaused() ? R.string.pause_label : R.string.play_label);

                //notifyDataSetChanged();
                notifyItemChanged(torchHolder.getAbsoluteAdapterPosition());
            });
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
