package com.example.habittrack.ui.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.habittrack.data.LoginRepository;
import com.example.habittrack.data.model.LoggedInUser;
import com.example.habittrack.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    public MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password,@Nullable String username,Boolean login) {
        // can be launched in a separate asynchronous job
        Task<AuthResult> result = loginRepository.login(email, password,login);
        LoginViewModel cal=this;
        if(!login){
            result.addOnCompleteListener(new OnCompleteListener<AuthResult>(){

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        DatabaseReference myRef =database.getReference("Users");
                        FirebaseAuth mAuth=FirebaseAuth.getInstance();
                        FirebaseUser user=mAuth.getCurrentUser();
                        LoggedInUser data =new LoggedInUser(user.getUid(),user.getEmail(),username);
                        data.setExp(0);
                        myRef.child(user.getUid()).setValue(data);


                        cal.loginResult.setValue(new LoginResult(new LoggedInUserView(data.getUserName())));
                    }else{
                        cal.loginResult.setValue(new LoginResult(R.string.login_failed));
                    }
                }
            });
        }else{
            result.addOnCompleteListener(new OnCompleteListener<AuthResult>(){

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
//                        Log.d("User","User: "+FirebaseAuth.getInstance().getCurrentUser().getEmail()+"\nAdditional Info: "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                       FirebaseAuth mAuth=FirebaseAuth.getInstance();
                        FirebaseUser user=mAuth.getCurrentUser();
                        LoggedInUser data =new LoggedInUser(user.getUid(),user.getEmail(),null);
                        cal.loginResult.setValue(new LoginResult(new LoggedInUserView(data.getUserName())));
                    }else{
                        cal.loginResult.setValue(new LoginResult(R.string.login_failed));
                    }
                }
            });
        }

    }

    public void loginDataChanged(String email, String password,@Nullable String repeat_password,@Nullable String username) {

        if(repeat_password==null && username==null) {//Login
//            Log.d("LoginDatChanged: ","Email: "+email +"Valid: "+isEmailValid(email)+" pass:"+password);
            if (!isEmailValid(email)) {
                loginFormState.setValue(new LoginFormState(R.string.invalid_email, null,null,null));
            } else if (!isPasswordValid(password)) {
                loginFormState.setValue(new LoginFormState(null, R.string.invalid_password,null,null));
            } else {
                loginFormState.setValue(new LoginFormState(true));
            }
        }else{//Register
//            Check if username repeat password password and email
            if (!isEmailValid(email)) {
                loginFormState.setValue(new LoginFormState(R.string.invalid_email, null,null,null));
            } else if (!isPasswordValid(password)) {
                loginFormState.setValue(new LoginFormState(null, R.string.invalid_password,null,null));
            } else if(!isRepeatPasswordValid(password,repeat_password)){
                loginFormState.setValue(new LoginFormState(null,null,R.string.invalid_repeatPass,null));
            }else if(!isUsernameValid(username)){
                loginFormState.setValue(new LoginFormState(null,null,null,R.string.invalid_username));
            }else{
                loginFormState.setValue(new LoginFormState(true));
            }

        }
    }

    public final Pattern textPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
    public final Pattern emailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    // A placeholder username validation check
    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        else if (emailPattern.matcher(email).matches()){
            return true;
        }
        return false;
    }
    public boolean loginFailed(){
        return true;
    }
    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
//        return password != null && password.trim().length() > 5;

        if(password!=null && password.trim().length()>8 && textPattern.matcher(password).matches()){
            return true;
        }
        return false;

    }
    private boolean isRepeatPasswordValid(String password,String repeatPass) {
//        return password != null && password.trim().length() > 5;

        if(password.trim().equals(repeatPass.trim())){
            return true;
        }
        return false;

    }

    private boolean isUsernameValid(String username){
        if(username.matches("^\\d*[a-zA-Z][a-zA-Z\\d]*$")){
            return true;
        }
        return false;
    }

}