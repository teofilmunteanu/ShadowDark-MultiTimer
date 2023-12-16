package com.tm.shadowdarktimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;

import com.tm.shadowdarktimer.adapters.TorchAdapter;
import com.tm.shadowdarktimer.models.TorchModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<TorchModel> torchList = new ArrayList<>();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout mainContainer = findViewById(R.id.linearLayout);
        RecyclerView recyclerView = mainContainer.findViewById(R.id.torchesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        //testing
        torchList.add(new TorchModel("59:00:00"));
        torchList.add(new TorchModel("00:00:00"));
        //end testing

        TorchAdapter torchAdapter = new TorchAdapter(torchList);
        recyclerView.setAdapter(torchAdapter);

    }

    public void onAddTorchClicked(View view){

    }

    /*public void onAddTorchClicked(View view) {
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout torchLayout = (LinearLayout) inflater.inflate(R.layout.layout_torch, torchesContainer, false);
        torchLayout.setId(torchesContainer.getChildCount());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(16, 16, 16, 16);

        LinearLayout addBtnLayout = (LinearLayout)view.getParent();//torchesContainer.findViewById(R.id.addBtnContainer);

        handler.post(
                ()-> {
                    torchesContainer.removeView(addBtnLayout);
                    torchesContainer.addView(torchLayout, params);
                    torchesContainer.addView(addBtnLayout);
                }
        );


    }*/
}