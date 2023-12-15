package com.tm.shadowdarktimer.adapters;

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

public class TorchAdapter extends RecyclerView.Adapter<TorchAdapter.ViewHolder> {
    private ArrayList<TorchModel> torchList;

    public TorchAdapter(ArrayList<TorchModel> torchList) {
        this.torchList = torchList;
    }

    @NonNull
    @Override
    public TorchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View torchView = inflater.inflate(R.layout.layout_torch, parent, false);

        return new ViewHolder(torchView);
    }

    @Override
    public void onBindViewHolder(@NonNull TorchAdapter.ViewHolder viewHolder, int position) {
        // set item views based on the list item at the given position
        TorchModel torch  = torchList.get(position);

        viewHolder.totalTimeInput.setText((CharSequence)torch.getTimeString());
    }

    @Override
    public int getItemCount() {
        return torchList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public EditText totalTimeInput;
        public Button play_pauseButton;
        public Button backwardTimeButton;
        public EditText timeChangeInput;
        public Button forwardTimeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            totalTimeInput = (EditText) itemView.findViewById(R.id.totalTime);
            play_pauseButton = (Button) itemView.findViewById(R.id.play_pause);
            backwardTimeButton = (Button) itemView.findViewById(R.id.backward);
            timeChangeInput = (EditText) itemView.findViewById(R.id.timeChange);
            forwardTimeButton = (Button) itemView.findViewById(R.id.forward);
        }
    }
}
