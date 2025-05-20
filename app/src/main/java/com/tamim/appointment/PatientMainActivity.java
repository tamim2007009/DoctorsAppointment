package com.tamim.appointment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
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
import com.tamim.appointment.PatientFragments.MyAppointmentFragment;
import com.tamim.appointment.PatientFragments.PatientSearchDiseaseFragment;
import com.tamim.appointment.PatientFragments.PatientSearchDoctorsFragment;
import com.tamim.appointment.PatientFragments.PendingAppointmentFragment;

public class PatientMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_patient);

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
                            final UserDetails userDetails = snapshot.getValue(UserDetails.class);
                            if (userDetails.getUserType().trim().equalsIgnoreCase("Patient")) {
                                setupUI(userDetails);
                            } else {
                                logout();
                            }
                        } else {
                            logout();
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
        TextView initials = findViewById(R.id.iniTv);

        name.setText(userDetails.getFirstName() + " " + userDetails.getLastName());
        initials.setText(userDetails.getFirstName().charAt(0) + "" + userDetails.getLastName().charAt(0));

        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        loadFragment(new PatientSearchDiseaseFragment(), "Search disease", R.id.search_disease);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        String title = "";
        int id = item.getItemId();

        if (id == R.id.search_disease) {
            fragment = new PatientSearchDiseaseFragment();
            title = "Search disease";
        } else if (id == R.id.search_doctor) {
            fragment = new PatientSearchDoctorsFragment();
            title = "Search doctors";
        } else if (id == R.id.pending_apt) {
            fragment = new PendingAppointmentFragment();
            title = "Pending Appointments";
        } else if (id == R.id.apt) {
            fragment = new MyAppointmentFragment();
            title = "My Appointments";
        } else if (id == R.id.logout) {
            showLogoutDialog();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return loadFragment(fragment, title, id);
    }

    private boolean loadFragment(Fragment fragment, String title, int itemId) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_Container, fragment)
                    .commit();
            getSupportActionBar().setTitle(Html.fromHtml("<font>" + title + "</font>"));
            navigationView.setCheckedItem(itemId);
            return true;
        }
        return false;
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> logout())
                .setNegativeButton("No", null)
                .show();
    }

    private void logout() {
        getSharedPreferences("STORAGE", MODE_PRIVATE).edit()
                .putBoolean("IS_DARKMODE_ENABLED", false)
                .putString("USER_TYPE", "NON")
                .apply();

        FirebaseAuth.getInstance().signOut();

        startActivity(new Intent(PatientMainActivity.this, AskDoctorPatient.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
