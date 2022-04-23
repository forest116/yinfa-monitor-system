package com.example.yinfajianhu.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.example.yinfajianhu.R;
import com.example.yinfajianhu.runnable.UnusualDataRunnable;

public class UnusualDataActivity extends AppCompatActivity {

    private static Context context;
    ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unusual_date);
        context = getApplicationContext();

        mList = findViewById(R.id.list_view);


        UnusualDataRunnable unusualDataRunnable = new UnusualDataRunnable(mList);
        Thread unsualDataThread = new Thread(unusualDataRunnable);
        unsualDataThread.start();

    }

    public static Context getContext(){
        return context;
    }
}
