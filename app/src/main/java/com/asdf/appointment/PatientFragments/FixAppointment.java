package com.asdf.appointment.PatientFragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

import com.asdf.appointment.R;
import com.asdf.appointment.ReusableFunctionsAndObjects;

public class FixAppointment extends AppCompatActivity {

    private TextView date, time;
    private String docid, name, addr, city, spl;
    private ProgressDialog progressDialog;
    private int d = 0, m = 0, y = 0, min = 0, h = 0;
    private HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_appointment);
        setTitle("Get Appointment");

        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        TextView t = findViewById(R.id.name);
        progressDialog = new ProgressDialog(FixAppointment.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        name = getIntent().getStringExtra("NAME");
        t.setText(name);
        t = findViewById(R.id.spl);
        spl = getIntent().getStringExtra("SPL");
        t.setText("Specialization: " + spl);
        t = findViewById(R.id.city);
        city = getIntent().getStringExtra("CITY");
        t.setText("City: " + city);
        t = findViewById(R.id.addr);
        addr = getIntent().getStringExtra("ADDR");
        t.setText("Address: " + addr);
        docid = getIntent().getStringExtra("DOCID");

        Calendar c = Calendar.getInstance();
        y = c.get(Calendar.YEAR);
        m = c.get(Calendar.MONTH);
        d = c.get(Calendar.DAY_OF_MONTH);
        h = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        date.setText(d + "/" + m + "/" + y);

        if (h == 0) {
            time.setText("12:" + min + " AM");
        } else if (h < 12) {
            time.setText(h + ":" + min + " AM");
        } else if (h == 12) {
            time.setText(h + ":" + min + " PM");
        } else {
            time.setText((h - 12) + ":" + min + " PM");
        }

        AppCompatButton datebtn = findViewById(R.id.setdate);
        AppCompatButton timebtn = findViewById(R.id.settime);
        AppCompatButton setapp = findViewById(R.id.setappoinment);

        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(FixAppointment.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + month + "/" + year);
                            }
                        }, y, m, d);
                datePickerDialog.show();
            }
        });

        timebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(FixAppointment.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if (hourOfDay == 0) {
                                    time.setText("12:" + minute + " AM");
                                } else if (hourOfDay < 12) {
                                    time.setText(hourOfDay + ":" + minute + " AM");
                                } else if (hourOfDay == 12) {
                                    time.setText(hourOfDay + ":" + minute + " PM");
                                } else {
                                    time.setText((hourOfDay - 12) + ":" + minute + " PM");
                                }
                            }
                        }, h, min, false);
                timePickerDialog.show();
            }
        });

        setapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FixAppointment.this);
                builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want to set an appointment with Dr. " + name + " on " + date.getText() + " at " + time.getText() + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.show();
                                hashMap = new HashMap<>();
                                hashMap.put("Address", addr);
                                hashMap.put("City", city);
                                hashMap.put("DocID", docid);
                                hashMap.put("Name", name);
                                hashMap.put("Specialization", spl);
                                hashMap.put("DateAndTime", date.getText() + " " + time.getText());

                                String dockey = FirebaseDatabase.getInstance().getReference()
                                        .child("PendingDocAppointments").child(docid).push().getKey();
                                hashMap.put("DoctorAppointKey", dockey);

                                String key = FirebaseDatabase.getInstance().getReference()
                                        .child("PendingPatientAppointments")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .push().getKey();
                                hashMap.put("PatientAppointKey", key);

                                FirebaseDatabase.getInstance().getReference()
                                        .child("PendingPatientAppointments")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(key)
                                        .setValue(hashMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    HashMap<String, String> docMap = new HashMap<>();
                                                    docMap.put("Name", ReusableFunctionsAndObjects.Name);
                                                    docMap.put("PatientID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                    docMap.put("PatientEmail", ReusableFunctionsAndObjects.Email);
                                                    docMap.put("PatientPhone", ReusableFunctionsAndObjects.MobileNo);
                                                    docMap.put("PatientAppointKey", key);
                                                    docMap.put("DoctorAppointKey", dockey);
                                                    docMap.put("DateAndTime", date.getText() + " " + time.getText());

                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("PendingDocAppointments")
                                                            .child(docid)
                                                            .child(dockey)
                                                            .setValue(docMap)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    progressDialog.dismiss();
                                                                    if (task.isSuccessful()) {
                                                                        ReusableFunctionsAndObjects.showMessageAlert(
                                                                                FixAppointment.this,
                                                                                "Completed",
                                                                                "Appointment has been requested to Dr. " + name + " on " + date.getText() + " at " + time.getText() + ". Check status in pending appointments.",
                                                                                "OK", (byte) 1
                                                                        );
                                                                    } else {
                                                                        ReusableFunctionsAndObjects.showMessageAlert(
                                                                                FixAppointment.this,
                                                                                "Network Error",
                                                                                "Make sure you are connected to internet.",
                                                                                "OK", (byte) 0
                                                                        );
                                                                    }
                                                                }
                                                            });
                                                } else {
                                                    progressDialog.dismiss();
                                                    ReusableFunctionsAndObjects.showMessageAlert(FixAppointment.this, "Network Error", "Make sure you are connected to internet.", "OK", (byte) 0);
                                                }
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", null)
                        .setCancelable(false).show();
            }
        });
    }
}
