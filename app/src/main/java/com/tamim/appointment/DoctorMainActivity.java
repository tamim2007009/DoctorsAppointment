package com.tamim.appointment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.tamim.appointment.DataRetrievalClass.UserDetails;
import com.tamim.appointment.DoctorFragments.AppointmentFragment;
import com.tamim.appointment.DoctorFragments.AppointmentRequestFragment;

public class DoctorMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseDatabase.getInstance().getReference()
                .child("UserDetails")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressDialog.dismiss();
                        if (snapshot.exists()) {
                            UserDetails userDetails = snapshot.getValue(UserDetails.class);
                            if (userDetails.getUserType().equalsIgnoreCase("Doctor")) {
                                setupUI(userDetails);
                            } else {
                                redirectToLogin();
                            }
                        } else {
                            redirectToLogin();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                    }
                });
    }

    private void setupUI(UserDetails userDetails) {
        ReusableFunctionsAndObjects.setValues(
                userDetails.getFirstName() + " " + userDetails.getLastName(),
                userDetails.getEmail(),
                userDetails.getMobileNo()
        );

        TextView name = findViewById(R.id.name);
        name.setText(userDetails.getFirstName() + " " + userDetails.getLastName());

        TextView initials = findViewById(R.id.iniTv);
        initials.setText(userDetails.getFirstName().charAt(0) + "" + userDetails.getLastName().charAt(0));

        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        SwitchCompat switchCompat = (SwitchCompat) navigationView.getMenu().findItem(R.id.nav_switch).getActionView();
        boolean isDarkMode = getSharedPreferences("STORAGE", MODE_PRIVATE).getBoolean("IS_DARKMODE_ENABLED", false);
        switchCompat.setChecked(isDarkMode);

        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            getSharedPreferences("STORAGE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("IS_DARKMODE_ENABLED", isChecked)
                    .apply();

            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Load default fragment
        loadFragment(new AppointmentRequestFragment(), "Appointment Requests", R.id.appointment_req);
    }

    private void redirectToLogin() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, AskDoctorPatient.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finishAffinity();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        String title = "";
        int ID = item.getItemId();

        if (ID == R.id.appointment_req) {
            fragment = new AppointmentRequestFragment();
            title = "Appointment Requests";
        } else if (ID == R.id.appointments) {
            fragment = new AppointmentFragment();
            title = "Appointments";
        } else if (ID == R.id.logout) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> logout())
                    .setNegativeButton("No", null)
                    .show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return loadFragment(fragment, title, ID);
    }

    private boolean loadFragment(Fragment fragment, String title, int ID) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_Container, fragment)
                    .commit();

            getSupportActionBar().setTitle(Html.fromHtml("<font>" + title + "</font>"));
            navigationView.setCheckedItem(ID);
            return true;
        }
        return false;
    }

    private void logout() {
        getSharedPreferences("STORAGE", MODE_PRIVATE)
                .edit()
                .putBoolean("IS_DARKMODE_ENABLED", false)
                .putString("USER_TYPE", "NON")
                .apply();

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, AskDoctorPatient.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
