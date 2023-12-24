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
    TorchAdapter torchAdapter;

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

    public void onAddTorchClicked(View view){
        torchList.add(new TorchModel(1,0,0));
        torchAdapter.notifyItemInserted(torchList.size());
    }
}