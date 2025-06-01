package com.asdf.appointment.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import com.asdf.appointment.DataRetrievalClass.PatientAppointmentRequest;
import com.asdf.appointment.PatientFragments.PendingAppointmentFragment;
import com.asdf.appointment.PatientMainActivity;
import com.asdf.appointment.R;
import com.asdf.appointment.ReusableFunctionsAndObjects;

public class PendingAppointmentAdapter extends RecyclerView.Adapter<PendingAppointmentAdapter.ViewHolder> {

    private Activity activity;
    private List<PatientAppointmentRequest> appointmentRequestList;
    private ProgressDialog progressDialog;
    public PendingAppointmentAdapter(Activity activity, List<PatientAppointmentRequest> appointmentRequestList) {
        this.activity = activity;
        this.appointmentRequestList = appointmentRequestList;
        progressDialog= new ProgressDialog(activity);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.item_patient_apt,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PatientAppointmentRequest request=appointmentRequestList.get(position);
        holder.doc_name.setText(request.getName());
        holder.spl.setText("Specialization: "+request.getSpecialization());
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity).setCancelable(false).setMessage("Are you sure you want to cancel the appointment of Dr. "+request.getName()+" for "+request.getDateAndTime()+"?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.setMessage("Cancelling...");
                                progressDialog.show();
                                FirebaseDatabase.getInstance().getReference().child("PendingDocAppointments").child(request.getDocID()).child(request.getDoctorAppointKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            FirebaseDatabase.getInstance().getReference().child("PendingPatientAppointments").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(request.getPatientAppointKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        progressDialog.dismiss();
                                                        Toast.makeText(activity, "Cancelled", Toast.LENGTH_SHORT).show();
                                                        ((PatientMainActivity) activity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_Container, new PendingAppointmentFragment(),"Pending Appointments").addToBackStack(null).commit();
                                                    }else {
                                                        progressDialog.dismiss();
                                                        ReusableFunctionsAndObjects.showMessageAlert(activity, "Network Error", "Make sure you are connected to internet.", "OK",(byte)0);
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    ReusableFunctionsAndObjects.showMessageAlert(activity, "Network Error", "Make sure you are connected to internet.", "OK",(byte)0);
                                                }
                                            });
                                        }else {
                                            progressDialog.dismiss();
                                            ReusableFunctionsAndObjects.showMessageAlert(activity, "Network Error", "Make sure you are connected to internet.", "OK",(byte)0);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        ReusableFunctionsAndObjects.showMessageAlert(activity, "Network Error", "Make sure you are connected to internet.", "OK",(byte)0);
                                    }
                                });
                            }
                        }).setNegativeButton("No",null).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentRequestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView doc_name,spl;
        AppCompatButton cancel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doc_name=itemView.findViewById(R.id.doc_name);
            cancel=itemView.findViewById(R.id.cancel);
            spl=itemView.findViewById(R.id.spl);
        }
    }
}
