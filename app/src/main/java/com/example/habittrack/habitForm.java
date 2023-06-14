package com.example.habittrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habittrack.main.SetReminderHabit;
import com.example.habittrack.main.SetReminderToDo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.UUID;

public class habitForm extends AppCompatActivity {
    private Habits habit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_form);

        EditText name=findViewById(R.id.etHabitName);
        EditText question=findViewById(R.id.etHabitQuestion);
        SeekBar seekBar=findViewById(R.id.seekBar);
        Switch reminderSwitch=findViewById(R.id.swReminder);
        TextView reminderTextView=findViewById(R.id.tvReminder);
        EditText description=findViewById(R.id.etDescription);
        Switch goalSwitch=findViewById(R.id.swGoal);
        TextView goalTextView=findViewById(R.id.textView2);
        TextView tvUnit=findViewById(R.id.tvUnit);
        EditText etUnit=findViewById(R.id.etUnit);
        TextView tvGoal=findViewById(R.id.tvGoal);
        EditText etGoal=findViewById(R.id.etGoal);
        RadioGroup rgDifficulty=findViewById(R.id.rgDifficultyHabit);
        Button btSubmit = findViewById(R.id.btSubmitHabit);
        Button btCancel= findViewById(R.id.btCancelHabit);

        Bundle bdl=getIntent().getExtras();

        if(bdl!=null){
            String objectString=bdl.getString("habit");
            habit=new Gson().fromJson(objectString,Habits.class);
            name.setText(habit.getName());
            question.setText(habit.getQuestion());
            seekBar.setProgress(habit.getTimes_per_week());
            reminderSwitch.setChecked(habit.getReminder());
            description.setText(habit.getAdittional_info());
            if(habit.isMeasurable()){
                goalSwitch.setChecked(true);
                goalSwitch.callOnClick();
                etUnit.setText(habit.getUnit());
                etGoal.setText(habit.getGoal());
            }
            ((RadioButton)rgDifficulty.getChildAt(habit.getDifficulty()-1)).setChecked(true);

        }

        goalTextView.setClickable(true);
        goalTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goalSwitch.setChecked(!(goalSwitch.isChecked()));
                goalSwitch.callOnClick();
            }
        });
        goalSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!goalSwitch.isChecked()){
                    tvGoal.setVisibility(View.GONE);
                    tvUnit.setVisibility(View.GONE);
                    etGoal.setVisibility(View.GONE);
                    etUnit.setVisibility(View.GONE);
                }else{
                    tvGoal.setVisibility(View.VISIBLE);
                    tvUnit.setVisibility(View.VISIBLE);
                    etGoal.setVisibility(View.VISIBLE);
                    etUnit.setVisibility(View.VISIBLE);
                }
            }
        });

        reminderTextView.setClickable(true);
        reminderTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reminderSwitch.setChecked(!(reminderSwitch.isChecked()));
                reminderSwitch.callOnClick();
            }
        });




        TextView tv18=findViewById(R.id.textView18);
        tv18.setText(seekBar.getProgress()+" time a week");
//        RadioGroup rgDifficulty=findViewById(R.id.rgDifficultyHabit);
//        Button btSubmit = findViewById(R.id.btSubmitHabit);
//        Button btCancel= findViewById(R.id.btCancelHabit);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(seekBar.getProgress()==1){
                    tv18.setText(seekBar.getProgress()+" time a week");
                }else{
                    tv18.setText(seekBar.getProgress()+" times a week");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        long interval=24*60*60*1000;//24 hours //60 minutes// 60 seconds //1000 millis

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth=FirebaseAuth.getInstance();
                FirebaseUser user=mAuth.getCurrentUser();
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference ref=database.getReference("Habits");

                String habitname=name.getText().toString();
                String habitQuestion=question.getText().toString();
                int times_a_week=seekBar.getProgress();
                Boolean reminderBool=reminderSwitch.isChecked();
                String desc=description.getText().toString();
                Boolean goalBool=goalSwitch.isChecked();
                String unit=etUnit.getText().toString();
                String goal=etGoal.getText().toString();
                int difficulty=Integer.parseInt(((RadioButton)findViewById(rgDifficulty.getCheckedRadioButtonId())).getText().toString());

                AlarmManager alarmManager=(AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                Intent in=new Intent(getApplicationContext(), SetReminderHabit.class);

                if(habit==null){
                    habit=new Habits(habitname,habitQuestion,times_a_week,reminderBool,desc,difficulty,goalBool,goal,unit);
                    habit.setUid(UUID.randomUUID().toString()+habit.getName());
                    ref.child(user.getUid()).child(habit.getUid()).setValue(habit).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                if(habit.getReminder()==true) {
                                    Bundle bdl=new Bundle();
                                    bdl.putString("habitName", habit.getName());
                                    bdl.putString("Question", habit.getQuestion());
                                    bdl.putString("uid",habit.getUid());
                                    in.putExtras(bdl);
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),habit.getUid().hashCode(), in, PendingIntent.FLAG_CANCEL_CURRENT);
                                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),interval, pendingIntent);
                                    Toast.makeText(habitForm.this, "Reminder Set", Toast.LENGTH_SHORT).show();
                                }
                                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(), "Failed to add", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    habit.setName(habitname);
                    habit.setQuestion(habitQuestion);
                    habit.setTimes_per_week(times_a_week);
                    habit.setReminder(reminderBool);
                    habit.setAdittional_info(desc);
                    habit.setMeasurable(goalBool);
                    habit.setGoal(goal);
                    habit.setDifficulty(difficulty);
                    habit.setUnit(unit);
                    ref.child(user.getUid()).child(habit.getUid()).setValue(habit).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                if(habit.getReminder()==true) {
                                    Bundle bdl=new Bundle();
                                    bdl.putString("habitName", habit.getName());
                                    bdl.putString("Question", habit.getQuestion());
                                    bdl.putString("uid",habit.getUid());
                                    in.putExtras(bdl);
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),habit.getUid().hashCode(), in, PendingIntent.FLAG_CANCEL_CURRENT);
                                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),interval, pendingIntent);
                                    Toast.makeText(habitForm.this, "Reminder Set", Toast.LENGTH_SHORT).show();
                                }


                                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(), "Failed to add", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);
            }
        });

    }
}