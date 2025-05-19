package com.tamim.appointment.Model;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.tamim.appointment.MainActivity;
import com.tamim.appointment.R;

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
