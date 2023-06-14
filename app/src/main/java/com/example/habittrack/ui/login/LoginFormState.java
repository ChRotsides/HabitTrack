package com.example.habittrack.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class LoginFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer repeatPassError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;

    LoginFormState(@Nullable Integer emailError, @Nullable Integer passwordError,@Nullable Integer repeatPassError,@Nullable Integer usernameError) {
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.repeatPassError=repeatPassError;
        this.usernameError = usernameError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.emailError=null;
        this.repeatPassError=null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getEmailError() {
        return emailError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }
    @Nullable
    Integer getRepeatPassError() {
        return repeatPassError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}