package com.example.habittrack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.habittrack.databinding.ActivityMainBinding;
import com.example.habittrack.main.HabitsListFragment;
import com.example.habittrack.main.AboutFragment;
import com.example.habittrack.main.TodoListFragment;
import com.example.habittrack.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import com.example.habittrack.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        ViewPager viewPager = binding.viewPager;

        sectionsPagerAdapter.addFragment(new TodoListFragment(),"Todo");
        sectionsPagerAdapter.addFragment(new HabitsListFragment(),"Habits");
        sectionsPagerAdapter.addFragment(new AboutFragment(),"About");

        Intent incoming=getIntent();
        if(incoming!=null){
            String inComingEmail=incoming.getStringExtra(LoginActivity.EMAIL);
            if(inComingEmail!=null){
                Snackbar.make(viewPager.getRootView(), "Welcome "+inComingEmail, Snackbar.LENGTH_LONG)
                        .setAction("Have fun", null).show();
            }


        }

        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        ExtendedFloatingActionButton fab = binding.fab;
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        SharedPreferences prefs=getSharedPreferences("Rating",MODE_PRIVATE);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        if(prefs.getString(user.getUid()+"rating","").equals("")){
            Intent in=new Intent(this,RateUsService.class);
            startService(in);
        }

        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==0){
                    fab.setText("ADD TODO");
                    fab.setVisibility(View.VISIBLE);
                }else if(position==1){
                    fab.setText("ADD HABIT");
                    fab.setVisibility(View.VISIBLE);
                }else if(position==2){
                    fab.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if(tabs.getSelectedTabPosition()==0){
                    Intent in=new Intent(getApplicationContext(),todoForm.class);
                    startActivity(in);
                }else if(tabs.getSelectedTabPosition()==1){
                    Intent in=new Intent(getApplicationContext(),habitForm.class);
                    startActivity(in);
                }


            }
        });
    }




}