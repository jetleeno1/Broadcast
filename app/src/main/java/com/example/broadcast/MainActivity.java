package com.example.broadcast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView tv_clock;
    private Button btn,btn_s;

    private Boolean flag = false;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            tv_clock.setText(String.format("%02d:%02d:%02d.%02d",b.getInt("H"),b.getInt("M"),b.getInt("S"),b.getInt("MS")));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_clock = findViewById(R.id.tv_clock);
        btn = findViewById(R.id.btn);
        btn_s = findViewById(R.id.btn_s);

        registerReceiver(receiver,new IntentFilter("MyMessage"));

        flag = MyService.flag;
        if(flag)
            btn.setText("暫停");
        else
            btn.setText("開始");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = !flag;
                if(flag){
                    btn.setText("暫停");
                    Toast.makeText(MainActivity.this,"計時開始",Toast.LENGTH_SHORT).show();
                }else{
                    btn.setText("開始");
                    Toast.makeText(MainActivity.this,"計時暫停",Toast.LENGTH_SHORT).show();
                }
                startService(new Intent(MainActivity.this,MyService.class).putExtra("flag",flag));
            }
        });

        btn_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyService.h=0;
                MyService.m=0;
                MyService.s=0;
                MyService.ms=0;
                tv_clock.setText("00:00:00.00");
                Toast.makeText(MainActivity.this,"重置計時",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}