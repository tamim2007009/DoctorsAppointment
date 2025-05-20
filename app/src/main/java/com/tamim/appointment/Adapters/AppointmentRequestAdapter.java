package com.tamim.appointment.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import com.tamim.appointment.DataRetrievalClass.AppointmentRequest;
import com.tamim.appointment.DataRetrievalClass.PatientAppointmentRequest;
import com.tamim.appointment.DoctorFragments.AppointmentRequestFragment;
import com.tamim.appointment.DoctorMainActivity;
import com.tamim.appointment.R;
import com.tamim.appointment.ReusableFunctionsAndObjects;

public class AppointmentRequestAdapter extends RecyclerView.Adapter<AppointmentRequestAdapter.ViewHolder> {

    private Context context;
    private List<AppointmentRequest> appointmentRequestList;
    private ProgressDialog progressDialog;

    public AppointmentRequestAdapter(Context context, List<AppointmentRequest> appointmentRequestList) {
        this.context = context;
        this.appointmentRequestList = appointmentRequestList;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_apt_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppointmentRequest appointmentRequest = appointmentRequestList.get(position);
        holder.name.setText(appointmentRequest.getName());
        holder.email.setText(appointmentRequest.getPatientEmail());
        holder.phone.setText(appointmentRequest.getPatientPhone());
        holder.datetime.setText(appointmentRequest.getDateAndTime());

        holder.reject.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage("Are you sure you want to reject the appointment request of " + appointmentRequest.getName() + " for " + appointmentRequest.getDateAndTime() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    progressDialog.setMessage("Rejecting...");
                    progressDialog.show();
                    FirebaseDatabase.getInstance().getReference().child("PendingPatientAppointments")
                            .child(appointmentRequest.getPatientID())
                            .child(appointmentRequest.getPatientAppointKey())
                            .removeValue().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase.getInstance().getReference().child("PendingDocAppointments")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(appointmentRequest.getDoctorAppointKey())
                                            .removeValue().addOnCompleteListener(task2 -> {
                                                progressDialog.dismiss();
                                                if (task2.isSuccessful()) {
                                                    Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
                                                    ((DoctorMainActivity) context).getSupportFragmentManager().beginTransaction()
                                                            .replace(R.id.fragment_Container, new AppointmentRequestFragment(), "Appointment Requests")
                                                            .addToBackStack(null).commit();
                                                } else {
                                                    ReusableFunctionsAndObjects.showMessageAlert(context, "Network Error", "Make sure you are connected to internet.", "OK", (byte) 0);
                                                }
                                            }).addOnFailureListener(e -> {
                                                progressDialog.dismiss();
                                                ReusableFunctionsAndObjects.showMessageAlert(context, "Network Error", "Make sure you are connected to internet.", "OK", (byte) 0);
                                            });
                                } else {
                                    progressDialog.dismiss();
                                    ReusableFunctionsAndObjects.showMessageAlert(context, "Network Error", "Make sure you are connected to internet.", "OK", (byte) 0);
                                }
                            }).addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                ReusableFunctionsAndObjects.showMessageAlert(context, "Network Error", "Make sure you are connected to internet.", "OK", (byte) 0);
                            });
                })
                .setNegativeButton("No", null)
                .show());

        holder.confirm.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage("Are you sure you want to confirm the appointment request of " + appointmentRequest.getName() + " for " + appointmentRequest.getDateAndTime() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    progressDialog.setMessage("Confirming...");
                    progressDialog.show();
                    FirebaseDatabase.getInstance().getReference().child("ConfirmedDocAppointments")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(appointmentRequest.getDoctorAppointKey())
                            .setValue(appointmentRequest).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase.getInstance().getReference().child("PendingPatientAppointments")
                                            .child(appointmentRequest.getPatientID())
                                            .child(appointmentRequest.getPatientAppointKey())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    PatientAppointmentRequest patientAppointmentRequest = snapshot.getValue(PatientAppointmentRequest.class);
                                                    FirebaseDatabase.getInstance().getReference().child("ConfirmedPatientAppointments")
                                                            .child(appointmentRequest.getPatientID())
                                                            .child(patientAppointmentRequest.getPatientAppointKey())
                                                            .setValue(patientAppointmentRequest).addOnCompleteListener(task1 -> {
                                                                if (task1.isSuccessful()) {
                                                                    FirebaseDatabase.getInstance().getReference().child("PendingPatientAppointments")
                                                                            .child(appointmentRequest.getPatientID())
                                                                            .child(appointmentRequest.getPatientAppointKey())
                                                                            .removeValue().addOnCompleteListener(task2 -> {
                                                                                if (task2.isSuccessful()) {
                                                                                    FirebaseDatabase.getInstance().getReference().child("PendingDocAppointments")
                                                                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                                            .child(appointmentRequest.getDoctorAppointKey())
                                                                                            .removeValue().addOnCompleteListener(task3 -> {
                                                                                                progressDialog.dismiss();
                                                                                                if (task3.isSuccessful()) {
                                                                                                    Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT).show();
                                                                                                    ((DoctorMainActivity) context).getSupportFragmentManager().beginTransaction()
                                                                                                            .replace(R.id.fragment_Container, new AppointmentRequestFragment(), "Appointment Requests")
                                                                                                            .addToBackStack(null).commit();
                                                                                                } else {
                                                                                                    ReusableFunctionsAndObjects.showMessageAlert(context, "Network Error", "Make sure you are connected to internet.", "OK", (byte) 0);
                                                                                                }
                                                                                            }).addOnFailureListener(e -> {
                                                                                                progressDialog.dismiss();
                                                                                                ReusableFunctionsAndObjects.showMessageAlert(context, "Network Error", "Make sure you are connected to internet.", "OK", (byte) 0);
                                                                                            });
                                                                                } else {
                                                                                    progressDialog.dismiss();
                                                                                    ReusableFunctionsAndObjects.showMessageAlert(context, "Network Error", "Make sure you are connected to internet.", "OK", (byte) 0);
                                                                                }
                                                                            }).addOnFailureListener(e -> {
                                                                                progressDialog.dismiss();
                                                                                ReusableFunctionsAndObjects.showMessageAlert(context, "Network Error", "Make sure you are connected to internet.", "OK", (byte) 0);
                                                                            });
                                                                } else {
                                                                    progressDialog.dismiss();
                                                                    ReusableFunctionsAndObjects.showMessageAlert(context, "Network Error", "Make sure you are connected to internet.", "OK", (byte) 0);
                                                                }
                                                            }).addOnFailureListener(e -> {
                                                                progressDialog.dismiss();
                                                                ReusableFunctionsAndObjects.showMessageAlert(context, "Network Error", "Make sure you are connected to internet.", "OK", (byte) 0);
                                                            });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    progressDialog.dismiss();
                                                    ReusableFunctionsAndObjects.showMessageAlert(context, "Network Error", "Make sure you are connected to internet.", "OK", (byte) 0);
                                                }
                                            });
                                } else {
                                    progressDialog.dismiss();
                                    ReusableFunctionsAndObjects.showMessageAlert(context, "Network Error", "Make sure you are connected to internet.", "OK", (byte) 0);
                                }
                            }).addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                ReusableFunctionsAndObjects.showMessageAlert(context, "Network Error", "Make sure you are connected to internet.", "OK", (byte) 0);
                            });
                })
                .setNegativeButton("No", null)
                .show());
    }

    @Override
    public int getItemCount() {
        return appointmentRequestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, phone, datetime;
        AppCompatButton confirm, reject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.patient_name);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.phone);
            datetime = itemView.findViewById(R.id.date_time);
            confirm = itemView.findViewById(R.id.confirm);
            reject = itemView.findViewById(R.id.reject);
        }
    }
}
