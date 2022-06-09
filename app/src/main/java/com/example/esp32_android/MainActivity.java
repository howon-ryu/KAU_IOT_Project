package com.example.esp32_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_first);
        Button button1 = (Button) findViewById(R.id.button1);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button2 = (Button) findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PostDataActivity.class) ;

                startActivity(intent) ;
            }
        });

        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RealTimeActivity.class) ;

                startActivity(intent) ;
            }
        });

        button3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FindChangeActivity.class) ;

                startActivity(intent) ;
            }
        });

    }
}
