package com.example.ex4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void connectClick(View view)
    {
        TextView appCompatAutoCompleteTextView = (TextView) findViewById(R.id.ipText);
        String ip =  appCompatAutoCompleteTextView.getText().toString();
        appCompatAutoCompleteTextView = (TextView) findViewById(R.id.portText);
        String port =  appCompatAutoCompleteTextView.getText().toString();

        Intent intent = new Intent( this, Joystick.class);

        intent.putExtra("ip",ip);
        intent.putExtra("port",port);
        startActivity(intent);

    }
}
