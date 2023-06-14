package com.example.habittrack.data;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.habittrack.data.model.LoggedInUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Task<AuthResult> login(String email, String password,Boolean login) {
            // TODO: handle loggedInUser authentication
            FirebaseAuth mAuth=FirebaseAuth.getInstance();
            Task<AuthResult> task;
            if(!login){
                task=mAuth.createUserWithEmailAndPassword(email,password);
            }else{
                task=mAuth.signInWithEmailAndPassword(email,password);
            }

//            LoggedInUser fakeUser=new LoggedInUser(java.util.UUID.randomUUID().toString(), s);
            return task;

//            String s = "jane Doe";
//            LoggedInUser fakeUser=new LoggedInUser(java.util.UUID.randomUUID().toString(), s);
//            return new Result.Success<>(fakeUser);

    }

    private boolean loginFailed() {
        return true;
    }

    public void logout() {
        // TODO: revoke authentication
    }
}