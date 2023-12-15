package com.tm.shadowdarktimer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
     LinearLayout torchesContainer;
    //ArrayList<String> torchesList;
    //ArrayAdapter<String> arrayAdapter;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout mainContainer = findViewById(R.id.linearLayout);
       /* ScrollView torchesScrollView = mainContainer.findViewById(R.id.torchesScrollView);
        torchesContainer = torchesScrollView.findViewById(R.id.scrollViewItemsContainer);*/
    }

    public void onAddTorchClicked(View view) {
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


    }
}