package com.example.habittrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.habittrack.main.SetReminderToDo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class todoForm extends AppCompatActivity{
    private ToDo todo;
    private Calendar classCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_form);

        EditText etName=findViewById(R.id.etTodoName);
        EditText etDescription=findViewById(R.id.etTodoDescription);
        EditText etEndDate=findViewById(R.id.etTodoEndDate);
        EditText etEndTime=findViewById(R.id.etTodoEndTime);
        CheckBox cbReminder=findViewById(R.id.cbReminderTodo);
        EditText reminder_date=findViewById(R.id.etReminderTodoDate);
        EditText reminder_time=findViewById(R.id.etReminderTodoTime);
        RadioGroup rdGroup=findViewById(R.id.rgDifficultyHabit);
        Button btSubmit=findViewById(R.id.btSubmitHabit);
        Button btCancel=findViewById(R.id.btCancelHabit);
        Bundle bdl=getIntent().getExtras();
        ((RadioButton)rdGroup.getChildAt(0)).setChecked(true);
        if(bdl!=null){
            String objectString = bdl.getString("TodoItem");
            ToDo item=new Gson().fromJson(objectString,ToDo.class);
            etName.setText(item.getName());
            etDescription.setText(item.getDescription());

            SimpleDateFormat sf=new SimpleDateFormat("dd/MM/yyyy");
            etEndDate.setText(sf.format(item.getEnd_date()));
            sf=new SimpleDateFormat("hh:mm");
            etEndTime.setText(sf.format(item.getEnd_date()));

            if(item.getReminder()!=null){
                cbReminder.setChecked(true);
                sf=new SimpleDateFormat("dd/MM/yyyy");
                reminder_date.setVisibility(View.VISIBLE);
                reminder_date.setText(sf.format(item.getReminder()));
                sf=new SimpleDateFormat("hh:mm");
                reminder_time.setVisibility(View.VISIBLE);
                reminder_time.setText(sf.format(item.getReminder()));
            }
            switch (item.getDifficulty()){
                case 1:
                    ((RadioButton)rdGroup.getChildAt(0)).setChecked(true);
                    break;
                case 2:
                    ((RadioButton)rdGroup.getChildAt(1)).setChecked(true);
                    break;
                case 3:
                    ((RadioButton)rdGroup.getChildAt(2)).setChecked(true);
                    break;
            }
            this.todo=item;

        }

        if(classCalendar==null){
            classCalendar=Calendar.getInstance();
        }

        DatePickerDialog.OnDateSetListener dateListenerEndDate=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                classCalendar.set(Calendar.YEAR,i);
                classCalendar.set(Calendar.MONTH,i1);
                classCalendar.set(Calendar.DAY_OF_MONTH,i2);
                SimpleDateFormat simpleDf=new SimpleDateFormat("dd/MM/yyyy");
                etEndDate.setText(simpleDf.format(classCalendar.getTime()));
            }
        };

        TimePickerDialog.OnTimeSetListener timeListenerEndTime=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                classCalendar.set(Calendar.HOUR,i);
                classCalendar.set(Calendar.MINUTE,i1);
                SimpleDateFormat simpleDf=new SimpleDateFormat("hh:mm");
                etEndTime.setText(simpleDf.format(classCalendar.getTime()));
            }
        };
        DatePickerDialog.OnDateSetListener dateListenerReminderDate=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                classCalendar.set(Calendar.YEAR,i);
                classCalendar.set(Calendar.MONTH,i1);
                classCalendar.set(Calendar.DAY_OF_MONTH,i2);
                SimpleDateFormat simpleDf=new SimpleDateFormat("dd/MM/yyyy");
                reminder_date.setText(simpleDf.format(classCalendar.getTime()));
            }
        };

        TimePickerDialog.OnTimeSetListener timeListenerReminderTime=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                classCalendar.set(Calendar.HOUR,i);
                classCalendar.set(Calendar.MINUTE,i1);
                SimpleDateFormat simpleDf=new SimpleDateFormat("hh:mm");
                reminder_time.setText(simpleDf.format(classCalendar.getTime()));
            }
        };
        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(todoForm.this,R.style.TimePickerTheme,dateListenerEndDate,classCalendar.get(Calendar.YEAR),classCalendar.get(Calendar.MONTH),classCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        etEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(todoForm.this,R.style.TimePickerTheme,timeListenerEndTime,classCalendar.get(Calendar.HOUR),classCalendar.get(Calendar.MINUTE),true).show();
            }
        });

        reminder_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(todoForm.this,R.style.TimePickerTheme,dateListenerReminderDate,classCalendar.get(Calendar.YEAR),classCalendar.get(Calendar.MONTH),classCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        reminder_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(todoForm.this,R.style.TimePickerTheme,timeListenerReminderTime,classCalendar.get(Calendar.HOUR),classCalendar.get(Calendar.MINUTE),true).show();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);
            }
        });





        cbReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reminder_date.setVisibility(cbReminder.isChecked()?View.VISIBLE:View.GONE);
                    reminder_time.setVisibility(cbReminder.isChecked()?View.VISIBLE:View.GONE);
            }
        });


        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat parser= new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Log.d("Format", etEndDate.getText()+" "+etEndTime.getText());
                Date dateTimeEnd= null;
                try {
                    dateTimeEnd = parser.parse(etEndDate.getText().toString()+" "+etEndTime.getText().toString());
                } catch (ParseException e) {
                    Toast.makeText(todoForm.this, "Wrong End DateTime Format dd/MM/yyyy hh:mm", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                RadioButton selectedRB=findViewById(rdGroup.getCheckedRadioButtonId());
                int difficulty;
                difficulty=Integer.parseInt(selectedRB.getText().toString());


//                Log.d("Datetime",dateTimeEnd.toString());

                FirebaseAuth mAuth=FirebaseAuth.getInstance();
                FirebaseUser user=mAuth.getCurrentUser();
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference todos=database.getReference("Todos");
                ProgressBar progressBar=findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
                AlarmManager alarmManager=(AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                Intent in=new Intent(getApplicationContext(), SetReminderToDo.class);
                if(todo==null){
                    if(!cbReminder.isChecked()){
                        todo=new ToDo(etName.getText().toString(),etDescription.getText().toString(),dateTimeEnd,difficulty);
                    }else{
                        Date dateTimeReminder = null;
                        try {
                            dateTimeReminder = parser.parse(reminder_date.getText().toString()+" "+reminder_time.getText().toString());
                        } catch (ParseException e) {
                            Toast.makeText(todoForm.this, "Wrong Reminder DateTime Format dd/MM/yyyy hh:mm", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        todo=new ToDo(etName.getText().toString(),etDescription.getText().toString(),dateTimeEnd,dateTimeReminder,difficulty);
                    }

                    todo.setUID(UUID.randomUUID().toString()+todo.getName());
                    todos.child(user.getUid()).child(todo.getUID()).setValue(todo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()){

                                if(todo.getReminder()!=null) {
                                    Bundle bdl=new Bundle();
                                    bdl.putString("TaskName", todo.getName());
                                    bdl.putString("Description", todo.getDescription());
                                    bdl.putString("uid",todo.getUID());
                                    in.putExtras(bdl);
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),todo.getUID().hashCode(), in, PendingIntent.FLAG_CANCEL_CURRENT);
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, todo.getReminder().getTime(), pendingIntent);
                                }

                                Intent in=new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(in);
                            }else{
                                Toast.makeText(todoForm.this, "Something Went Wrong try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                }else{

                    todo.setName(etName.getText().toString());
                    todo.setDescription(etDescription.getText().toString());
                    todo.setDifficulty(difficulty);
                    todo.setEnd_date(dateTimeEnd);
                    if(cbReminder.isChecked()) {
                        Date dateTimeReminder = null;
                        try {
                            dateTimeReminder = parser.parse(reminder_date.getText().toString()+" "+reminder_time.getText().toString());
                        } catch (ParseException e) {
                            Toast.makeText(todoForm.this, "Wrong Reminder DateTime Format dd/MM/yyyy hh:mm", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        todo.setReminder(dateTimeReminder);
                    }

                    todos.child(user.getUid()).child(todo.getUID()).setValue(todo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()){
                                if(todo.getReminder()!=null) {
                                    Bundle bdl=new Bundle();
                                    bdl.putString("TaskName", todo.getName());
                                    bdl.putString("Description", todo.getDescription());
                                    bdl.putString("uid",todo.getUID());
                                    in.putExtras(bdl);
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),todo.getUID().hashCode(), in, PendingIntent.FLAG_CANCEL_CURRENT);
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, todo.getReminder().getTime(), pendingIntent);
                                }
                                Intent in=new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(in);
                            }else{
                                Toast.makeText(todoForm.this, "Something Went Wrong try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                }
            }
        });





    }






}