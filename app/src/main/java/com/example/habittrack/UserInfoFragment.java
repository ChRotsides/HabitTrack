package com.example.habittrack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.habittrack.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link UserInfoFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class UserInfoFragment extends Fragment {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public UserInfoFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment UserInfoFragment.
//     */
    // TODO: Rename and change types and number of parameters
//    public static UserInfoFragment newInstance(String param1, String param2) {
//        UserInfoFragment fragment = new UserInfoFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
    private long exp;
    private String userName;
    private String email;
    private String todoHave;
    private String uid;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            exp=getArguments().getLong("exp");
            userName=getArguments().getString("userName");
            todoHave=getArguments().getString("todoHave");
            email=getArguments().getString("email");
            uid=getArguments().getString("uid");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_user_info, container, false);
        TextView emailTv=root.findViewById(R.id.tvEmail);
        TextView levelTv=root.findViewById(R.id.tvLevel);
        TextView todosLeftTv=root.findViewById(R.id.tvTodosLeft);
        TextView uidTv=root.findViewById(R.id.tvUID);
        ProgressBar progressBar=root.findViewById(R.id.progressBar2);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();


        int level=0;
        double leftxp=(double) this.exp;
        Double requiredXp=0.0;
        while (leftxp>=0){
            requiredXp=5*Math.pow(2,level);
            leftxp=leftxp-requiredXp;
            if(leftxp>=0){
                level+=1;
            }
        }
        Double prog=(leftxp+requiredXp)/requiredXp;
        Log.d("Progress",""+prog*100);
        progressBar.setProgress((int) Math.round(prog*100));

        TextView currentLvl=root.findViewById(R.id.tvCurrentLevel);
        TextView nextLevel=root.findViewById(R.id.tvNextLevel);
        currentLvl.setText(""+level);
        nextLevel.setText(""+(level+1));


        emailTv.setText(this.email);
        levelTv.setText(""+level);



        todosLeftTv.setText(todoHave);


        uidTv.setText(this.uid);
        TextView tv=root.findViewById(R.id.etRating);
        SharedPreferences prefs= getActivity().getSharedPreferences("Rating", Context.MODE_PRIVATE);
        tv.setClickable(true);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(getContext(),RateUs.class);
                startActivity(in);
            }
        });

        if(prefs.getString(user.getUid()+"rating","").equals("")){
            tv.setText("You did not rate the App Click Here to do that");
        }else{
            tv.setText("Your Rating: "+prefs.getString(user.getUid()+"rating","")+
                        "\nRecommend to Friends: "+prefs.getString(user.getUid()+"recommendFriend","")+
                        "\nRecommend to Enemies: "+prefs.getString(user.getUid()+"recommendEnemies","")+
                        "\nRecommend to Family: "+prefs.getString(user.getUid()+"recommendFamily","")+
                        "\nContinue using: "+prefs.getString(user.getUid()+"contUsing","")+
                    "\nYour Comments: "+prefs.getString(user.getUid()+"extraComments",""));

        }

        Button goBack=root.findViewById(R.id.btBack);
        Button signOut=root.findViewById(R.id.btSignOut);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


        return root;
    }
}