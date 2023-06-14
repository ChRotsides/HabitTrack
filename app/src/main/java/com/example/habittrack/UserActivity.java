package com.example.habittrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Intent in=getIntent();
        Bundle bdl=in.getExtras();

        System.out.println(bdl.getLong("exp",0));

        UserInfoFragment frag=new UserInfoFragment();
        frag.setArguments(bdl);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView3,frag,"tag").commit();


        FaceNameFragment frag2=new FaceNameFragment();
        frag2.setArguments(bdl);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,frag2,"tag").commit();

    }
}