package com.tm.shadowdarktimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tm.shadowdarktimer.adapters.TorchAdapter;
import com.tm.shadowdarktimer.models.TorchModel;
import com.tm.shadowdarktimer.services.TimerManager;

import java.time.LocalTime;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<TorchModel> torchList = new ArrayList<>();
    TorchAdapter torchAdapter;
    boolean globalPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout mainContainer = findViewById(R.id.linearLayout);
        RecyclerView recyclerView = mainContainer.findViewById(R.id.torchesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        torchAdapter = new TorchAdapter(torchList);
        recyclerView.setAdapter(torchAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, TimerManager.class);
        startService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onAddTorchClicked(View view){
        torchList.add(new TorchModel());
        torchAdapter.notifyItemInserted(torchList.size()-1);
    }

    public void onGlobalPauseClicked(View view){
        globalPaused = !globalPaused;
        String playLabel = getString(R.string.play_label);
        String pauseLabel = getString(R.string.pause_label);
        ((Button)view).setText(globalPaused ? playLabel : pauseLabel);

        for(TorchModel torch : torchList){
            if (torch.getPaused() != globalPaused){
                torch.pauseUnpause();
            }
        }
    }

    public void onGlobalForwardClicked(View view){
        String globalChangeString = ((EditText)findViewById(R.id.globalTimeChangeInput)).getText().toString();

        for (TorchModel torch : torchList){
            if (!torch.getPaused()){
                if(TorchModel.isValidTimeChangeString(globalChangeString)){
                    torch.fastForward(globalChangeString);
                }
                else{
                    torch.resetTimeChange();
                }
            }
        }
    }

    public void onGlobalBackwardClicked(View view){
        String globalChangeString = ((EditText)findViewById(R.id.globalTimeChangeInput)).getText().toString();

        for (TorchModel torch : torchList){
            if (!torch.getPaused()){
                if(TorchModel.isValidTimeChangeString(globalChangeString)){
                    torch.fastBackward(globalChangeString);
                }
                else{
                    torch.resetTimeChange();
                }
            }
        }
    }
}