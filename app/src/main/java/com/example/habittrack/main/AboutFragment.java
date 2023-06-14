package com.example.habittrack.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.habittrack.R;
import com.example.habittrack.RateUs;
import com.example.habittrack.UserActivity;
import com.example.habittrack.data.model.LoggedInUser;
import com.example.habittrack.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link PlayerInfoFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class AboutFragment extends Fragment {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public PlayerInfoFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment PlayerInfoFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static PlayerInfoFragment newInstance(String param1, String param2) {
//        PlayerInfoFragment fragment = new PlayerInfoFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        setHasOptionsMenu(true);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_about, container, false);
//        Button bt=root.findViewById(R.id.btUserInfo);
//        bt.set
        root.findViewById(R.id.btUserInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference refUsers=database.getReference("Users");

                refUsers.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){

                            LoggedInUser localuser=task.getResult().getValue(LoggedInUser.class);
                            localuser.setExp(task.getResult().child("exp").getValue(Long.class));
//                            Log.d("EXP",localuser.getExp()+"");
                            long exp=localuser.getExp();
                            String userName= localuser.getUserName();
                            String email=localuser.getUserEmail();
                            String uid=localuser.getUserId();
                            SharedPreferences prefs=getActivity().getSharedPreferences("Tasks", Context.MODE_PRIVATE);
                            String todoHave=prefs.getString("todoHave","");

                            Intent in=new Intent(getContext(), UserActivity.class);
                            Bundle bdl=new Bundle();
                            bdl.putLong("exp",exp);
                            bdl.putString("userName",userName);
                            bdl.putString("email",email);
                            bdl.putString("uid",uid);
                            bdl.putString("todoHave",todoHave);
                            in.putExtras(bdl);
                            startActivity(in);


                        }
                    }
                });
            }
        });


        return root;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater){
        inflater.inflate(R.menu.menu_main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.sign_out){
            FirebaseAuth.getInstance().signOut();
            Intent in =new Intent(getContext(), LoginActivity.class);
            startActivity(in);
        }else if(item.getItemId()==R.id.rate_us){
            Intent in=new Intent(getContext(), RateUs.class);
            startActivity(in);
        }else if(item.getItemId()==R.id.UserInfo){
            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

            FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference refUsers=database.getReference("Users");

            refUsers.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){

                        LoggedInUser localuser=task.getResult().getValue(LoggedInUser.class);
                        localuser.setExp(task.getResult().child("exp").getValue(Long.class));
                        long exp=localuser.getExp();
                        String userName= localuser.getUserName();
                        String email=localuser.getUserEmail();
                        String uid=localuser.getUserId();
                        SharedPreferences prefs=getActivity().getSharedPreferences("Tasks", Context.MODE_PRIVATE);
                        String todoHave=prefs.getString("todoHave","");

                        Intent in=new Intent(getContext(), UserActivity.class);
                        Bundle bdl=new Bundle();
                        bdl.putLong("exp",exp);
                        bdl.putString("userName",userName);
                        bdl.putString("email",email);
                        bdl.putString("uid",uid);
                        bdl.putString("todoHave",todoHave);
                        in.putExtras(bdl);
                        startActivity(in);


                    }
                }
            });
        }else if(item.getItemId()==R.id.Share){
            Intent sendIntent=new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,"Hello, please consider using HabitTrack it is a great app for tracking your habits and tasks!!");
            sendIntent.setType("text/plain");

            Intent shareIntent =Intent.createChooser(sendIntent,null);
            startActivity(shareIntent);
        }


        return super.onOptionsItemSelected(item);
    }


}