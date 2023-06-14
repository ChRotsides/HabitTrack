package com.example.habittrack.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.habittrack.MainActivity;
import com.example.habittrack.R;
import com.example.habittrack.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    public static final String EMAIL="";
    private FirebaseAuth mAuth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText emailEditText = binding.email;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;
        final EditText repeatPassEditText= binding.repeatPass;
        final EditText username= binding.username1;
        ToggleButton toggleLogin=findViewById(R.id.toggleButton);
        toggleLogin.setText("Register");
        SharedPreferences file= getSharedPreferences("userData",MODE_PRIVATE);
        emailEditText.setText(file.getString("email",""));

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());

                if (loginFormState.getEmailError() != null) {
//                    Log.d("Status Email: ",getString(loginFormState.getEmailError()));
                    emailEditText.setError(getString(loginFormState.getEmailError()));
                }
                if (loginFormState.getPasswordError() != null) {
//                    Log.d("Status Password: ",getString(loginFormState.getPasswordError()));
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
                if(loginFormState.getRepeatPassError()!=null){
                    repeatPassEditText.setError(getString(loginFormState.getRepeatPassError()));
                }
                if(loginFormState.getUsernameError()!=null){
                    username.setError(getString(loginFormState.getUsernameError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                    loadingProgressBar.setVisibility(View.GONE);
                }
                if (loginResult.getSuccess() != null) {

                    goToUserPage(emailEditText.getText().toString());

                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
//                finish();
            }


        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(toggleLogin.isChecked()){
                    loginViewModel.loginDataChanged(emailEditText.getText().toString(),
                            passwordEditText.getText().toString(),null,null);
                }else if(!toggleLogin.isChecked()){
                    loginViewModel.loginDataChanged(emailEditText.getText().toString(),
                            passwordEditText.getText().toString(),repeatPassEditText.getText().toString(),username.getText().toString());
                }
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        repeatPassEditText.addTextChangedListener(afterTextChangedListener);
        username.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(toggleLogin.isChecked()){
                    loginViewModel.login(emailEditText.getText().toString(),
                            passwordEditText.getText().toString(),null,toggleLogin.isChecked());
                    }else if(!toggleLogin.isChecked()){
                        loginViewModel.login(emailEditText.getText().toString(),
                                passwordEditText.getText().toString(),username.getText().toString(),toggleLogin.isChecked());
                    }
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences file= getSharedPreferences("userData",MODE_PRIVATE);
                SharedPreferences.Editor editor = file.edit();
                String email=emailEditText.getText().toString();
                editor.putString("email",email);
                editor.commit();

                    loadingProgressBar.setVisibility(View.VISIBLE);
                    if(toggleLogin.isChecked()){
                    loginViewModel.login(emailEditText.getText().toString(),
                            passwordEditText.getText().toString(),null,toggleLogin.isChecked());
                    }else if(!toggleLogin.isChecked()){
                        loginViewModel.login(emailEditText.getText().toString(),
                                passwordEditText.getText().toString(),username.getText().toString(),toggleLogin.isChecked());
                    }

            }
        });

        toggleLogin.setOnClickListener(new View.OnClickListener(){


            public void onClick(View v){
                EditText repeatPass=findViewById(R.id.repeatPass);
                EditText username=findViewById(R.id.username1);
                if(toggleLogin.isChecked()){

                    repeatPass.setVisibility(View.GONE);
                    username.setVisibility(View.GONE);
                    toggleLogin.setText("Login");
                }else {
                    repeatPass.setVisibility(View.VISIBLE);
                    username.setVisibility(View.VISIBLE);
                    toggleLogin.setText("Register");
                }
            }


        });



    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent in=new Intent(this, MainActivity.class);
            startActivity(in);
        }

    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
    public void goToUserPage(String email){

        Intent in=new Intent(this, MainActivity.class);
        in.putExtra(EMAIL,email);
        startActivity(in);
    }
}