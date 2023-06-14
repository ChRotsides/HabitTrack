package com.example.habittrack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

public class HabitAdapter extends ArrayAdapter<Habits> {

    public HabitAdapter(Context context, ArrayList<Habits> habit){
        super(context,0,habit);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Habits habit = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.habit_list_item, parent, false);
        }

        TextView tvName=(TextView) convertView.findViewById(R.id.tvHabitName);
        TextView tvQuestion=(TextView) convertView.findViewById(R.id.tvQuestion);
        TextView tvGoal=(TextView) convertView.findViewById(R.id.tvGoal2);
        TextView tvExpPerSession=(TextView) convertView.findViewById(R.id.tvExpPerSession);
        TextView tvSessions=(TextView)convertView.findViewById(R.id.tvSessions);
        TextView tvGoalTitle=(TextView)convertView.findViewById(R.id.tvGoalTitle);
        Button btAddSession=(Button) convertView.findViewById(R.id.btAddSession);
        Button btEdit=(Button) convertView.findViewById(R.id.btEditHabit);
        Button btDelete=(Button) convertView.findViewById(R.id.btDeleteHabit);


        tvName.setText(habit.getName());
        tvQuestion.setText(habit.getQuestion());
        if(!habit.getGoal().equals("")){
            Log.d("Unit",habit.getUnit());
            Log.d("Goal",habit.getGoal());
            tvGoal.setText(habit.getGoal()+" "+habit.getUnit());
            tvGoalTitle.setVisibility(View.VISIBLE);
            tvGoal.setVisibility(View.VISIBLE);
        }else{
            tvGoal.setVisibility(View.GONE);
            tvGoalTitle.setVisibility(View.GONE);
        }
        tvExpPerSession.setText(""+habit.getDifficulty()*10);
        tvSessions.setText(habit.getSessions()+"");

        btAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference habitRef=database.getReference("Habits");
                DatabaseReference userRef=database.getReference("Users");

                habitRef.child(user.getUid()).child(habit.getUid()).child("sessions").setValue(habit.getSessions()+1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            habit.setSessions(habit.getSessions()+1);
                            notifyDataSetChanged();
                            userRef.child(user.getUid()).child("exp").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.isSuccessful()){
                                        int old_exp=0;
                                        if((task.getResult().getValue()!=null)){
                                            old_exp=Integer.parseInt(task.getResult().getValue().toString());
                                        }
                                        userRef.child(user.getUid()).child("exp").setValue(old_exp+habit.getDifficulty()*10).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Snackbar.make(view,"Added Session to " + habit.getName() + " and added "+ habit.getDifficulty()*10 +" exp",Snackbar.LENGTH_LONG ).setAction("Action",null).show();

                                                }else{

                                                }
                                            }
                                        });
                                    }else{

                                    }
                                }
                            });
                        }else{

                        }
                    }
                });





            }
        });
        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(getContext(),habitForm.class);
                in.putExtra("habit",new Gson().toJson(habit));
                getContext().startActivity(in);
            }
        });




        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
                dialog.setTitle("Delete?");
                dialog.setMessage("Are you sure you want to delete " + habit.getName());
                dialog.setNegativeButton("Cancel", null);
                dialog.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        DatabaseReference ref=database.getReference("Habits");
                        Log.d("UID: ",habit.getUid());
                        ref.child(user.getUid()).child(habit.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d("Deleted","Success");
                                }else{
                                    Log.d("Deleted","Fail");
                                }
                            }
                        });

                        remove(habit);

                        notifyDataSetChanged();
                    }});
                dialog.show();

            }
        });


        return convertView;
    }

}
