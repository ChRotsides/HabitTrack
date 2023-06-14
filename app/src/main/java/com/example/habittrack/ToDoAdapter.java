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
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ToDoAdapter extends ArrayAdapter<ToDo> {

    public ToDoAdapter(Context context, ArrayList<ToDo> todos){
        super(context,0,todos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ToDo todo = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvNameTodoList);
        TextView tvEndDate = (TextView) convertView.findViewById(R.id.tvEndDateTodoList);
        TextView tvExp=(TextView) convertView.findViewById(R.id.tvExp);
        Button btEdit=(Button) convertView.findViewById(R.id.btEditToDo);
        Button btDelete=(Button) convertView.findViewById(R.id.btDeleteToDo);
        Button btComplete=(Button) convertView.findViewById(R.id.btCompleteToDo);

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(getContext(),todoForm.class);
                in.putExtra("TodoItem",new Gson().toJson(todo));
                getContext().startActivity(in);

            }
        });
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
                dialog.setTitle("Delete?");
                dialog.setMessage("Are you sure you want to delete " + todo.getName());
                dialog.setNegativeButton("Cancel", null);
                dialog.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        DatabaseReference ref=database.getReference("Todos");
                        Log.d("UID: ",todo.getUID());
                        ref.child(user.getUid()).child(todo.getUID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d("Deleted","Success");
                                }else{
                                    Log.d("Deleted","Fail");
                                }
                            }
                        });

                        remove(todo);

                        notifyDataSetChanged();
                    }});
                dialog.show();



            }
        });
        btComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int exp=todo.getDifficulty()*20;

                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference ref=database.getReference("Users");
                        ref.child(user.getUid()).child("exp").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                int old_exp=0;
                                if(task.getResult().getValue()!=null){
                                    old_exp=Integer.parseInt(task.getResult().getValue().toString());
                                }
                                ref.child(user.getUid()).child("exp").setValue(old_exp+exp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            database.getReference("Todos").child(user.getUid()).child(todo.getUID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Snackbar.make(view, "Removed Todo Successfully and added Exp", Snackbar.LENGTH_LONG)
                                                                .setAction("Action", null).show();
                                                        remove(todo);
                                                        notifyDataSetChanged();
                                                    }else{
                                                        Snackbar.make(view, "Unable to remove Todo", Snackbar.LENGTH_LONG)
                                                                .setAction("Action", null).show();

                                                    }
                                                }
                                            });
                                        }else{
                                            Snackbar.make(view, "Failed to Add Exp", Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                        }
                                    }
                                });
                            }
                        });


            }
        });



        // Populate the data into the template view using the data object
        tvName.setText(todo.getName());
        SimpleDateFormat sf=new SimpleDateFormat("E M/dd/yyy hh:mm");
        tvEndDate.setText(sf.format(todo.getEnd_date()));
        tvExp.setText(""+todo.getDifficulty()*20);




        // Return the completed view to render on screen
        return convertView;
    }

}
