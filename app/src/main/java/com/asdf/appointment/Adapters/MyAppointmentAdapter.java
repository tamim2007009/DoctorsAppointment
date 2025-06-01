package com.asdf.appointment.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import com.asdf.appointment.DataRetrievalClass.PatientAppointmentRequest;
import com.asdf.appointment.PatientFragments.PendingAppointmentFragment;
import com.asdf.appointment.PatientMainActivity;
import com.asdf.appointment.R;
import com.asdf.appointment.ReusableFunctionsAndObjects;

public class MyAppointmentAdapter extends RecyclerView.Adapter<MyAppointmentAdapter.ViewHolder> {

    private Context context;
    private List<PatientAppointmentRequest> appointmentRequestList;
    private ProgressDialog progressDialog;

    public MyAppointmentAdapter(Context context, List<PatientAppointmentRequest> appointmentRequestList) {
        this.context = context;
        this.appointmentRequestList = appointmentRequestList;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_patient_apt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PatientAppointmentRequest request = appointmentRequestList.get(position);
        holder.doc_name.setText(request.getName());
        holder.spl.setText("Specialization: " + request.getSpecialization());

        holder.cancel.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage("Are you sure you want to cancel the appointment of Dr. " +
                        request.getName() + " for " + request.getDateAndTime() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    progressDialog.setMessage("Cancelling...");
                    progressDialog.show();

                    FirebaseDatabase.getInstance().getReference()
                            .child("ConfirmedDocAppointments")
                            .child(request.getDocID())
                            .child(request.getDoctorAppointKey())
                            .removeValue()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("ConfirmedPatientAppointments")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(request.getPatientAppointKey())
                                            .removeValue()
                                            .addOnCompleteListener(task2 -> {
                                                progressDialog.dismiss();
                                                if (task2.isSuccessful()) {
                                                    Toast.makeText(context, "Appointment Cancelled", Toast.LENGTH_SHORT).show();
                                                    ((PatientMainActivity) context).getSupportFragmentManager()
                                                            .beginTransaction()
                                                            .replace(R.id.fragment_Container, new PendingAppointmentFragment(), "Pending Appointments")
                                                            .addToBackStack(null)
                                                            .commit();
                                                } else {
                                                    showNetworkError();
                                                }
                                            }).addOnFailureListener(e -> {
                                                progressDialog.dismiss();
                                                showNetworkError();
                                            });
                                } else {
                                    progressDialog.dismiss();
                                    showNetworkError();
                                }
                            }).addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                showNetworkError();
                            });
                })
                .setNegativeButton("No", null)
                .show()
        );
    }

    @Override
    public int getItemCount() {
        return appointmentRequestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView doc_name, spl;
        AppCompatButton cancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doc_name = itemView.findViewById(R.id.doc_name);
            spl = itemView.findViewById(R.id.spl);
            cancel = itemView.findViewById(R.id.cancel);
        }
    }

    private void showNetworkError() {
        ReusableFunctionsAndObjects.showMessageAlert(
                context, "Network Error", "Make sure you are connected to internet.", "OK", (byte) 0
        );
    }
}
