package com.example.habittrack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Switch;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.habittrack.databinding.ActivityRateUsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RateUs extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityRateUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRateUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent=new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,"Hello, please consider using HabitTrack it is a great app for tracking your habits and tasks!!");
                sendIntent.setType("text/plain");

                Intent shareIntent =Intent.createChooser(sendIntent,null);
                startActivity(shareIntent);
            }
        });



    }

    public void Submit(View v){
        RatingBar ratingBar=findViewById(R.id.ratingBar);
        float rating=ratingBar.getRating();
        Map<String,Boolean> recommend=new HashMap<String,Boolean>();
        recommend.clear();
        recommend.put("Friends",((CheckBox)findViewById(R.id.cbFriends)).isChecked());
        recommend.put("Family",((CheckBox)findViewById(R.id.cbFamily)).isChecked());
        recommend.put("Enemies",((CheckBox)findViewById(R.id.cbEnemies)).isChecked());

        Switch contUsing=findViewById(R.id.switch1);
        Boolean continueUsing= contUsing.isChecked();

        EditText etExtraComments=findViewById(R.id.etExtraComments);
        String extraComments=etExtraComments.getText().toString();

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference ref= database.getReference("Ratings");
        DatabaseReference userRef= ref.child(user.getUid());
        userRef.child("rating").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().getValue()!=null){
                    Log.d("Dialog","new Dialog Here");
                    AlertDialog.Builder dialog=new AlertDialog.Builder(v.getContext());
                    dialog.setTitle("You have already rated us!");
                    dialog.setMessage("Do you wish to update your rating?");
                    dialog.setNegativeButton("Cancel", null);
                    dialog.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            updateRating(v,userRef,rating,recommend,continueUsing,extraComments);

                            Intent in=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(in);
                        }
                    });
                    dialog.show();
                }else{
                    updateRating(v,userRef,rating,recommend,continueUsing,extraComments);
                    Intent in=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(in);
                }

            }
        });

    }
    public void Cancel(View v){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);


    }
    public void updateRating(View v,DatabaseReference ref,float rating,Map<String,Boolean> recommend,Boolean continueUsing,String extraComments){

        ref.child("rating").setValue(rating);
        ref.child("recommend").setValue(recommend);
        ref.child("ContinueUsing").setValue(continueUsing);
        ref.child("ExtraComments").setValue(extraComments);
        SharedPreferences prefs=getSharedPreferences("Rating",MODE_PRIVATE);
        SharedPreferences.Editor editor=prefs.edit();

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();


        editor.putString(user.getUid()+"rating",""+rating);
        editor.putString(user.getUid()+"recommendFriend",recommend.get("Friends").toString());
        editor.putString(user.getUid()+"recommendEnemies",recommend.get("Enemies").toString());
        editor.putString(user.getUid()+"recommendFamily",recommend.get("Family").toString());
        editor.putString(user.getUid()+"contUsing",continueUsing.toString());
        editor.putString(user.getUid()+"extraComments",extraComments);
        editor.commit();




    }


}