package com.asdf.appointment.Model;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.asdf.appointment.MainActivity;
import com.asdf.appointment.R;

public class FrontPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(FrontPage.this, MainActivity.class));
        },3000); // 5s


    }

}
