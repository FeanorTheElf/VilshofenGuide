package com.example.simon.vilshofenguide.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.simon.vilshofenguide.R;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        Exception exception = (Exception) getIntent().getSerializableExtra("exception");
        TextView exceptionView = (TextView)findViewById(R.id.exception_text);
        exceptionView.setText(exception.getMessage());
    }
}
