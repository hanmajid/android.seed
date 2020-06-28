package com.hanmajid.android.seed.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hanmajid.android.seed.ui.auth.login.LoginForm
import com.hanmajid.android.seed.ui.auth.register.RegisterForm

class AuthViewModel : ViewModel() {

    private val TAG = "AuthViewModel"

    enum class AuthenticationState {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        AUTHENTICATED,          // The user has authenticated successfully
        INVALID_AUTHENTICATION  // Authentication failed
    }

    val isFinishedOnboarding = MutableLiveData<Boolean>(true)

    val authenticationState =
        MutableLiveData<AuthenticationState>(AuthenticationState.AUTHENTICATED)

    fun finishOnboarding() {
        Log.wtf(TAG, "finished")
        isFinishedOnboarding.value = true
    }

    fun refuseAuthentication() {
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }

    fun authLogin(loginForm: LoginForm) {
        // TODO: Authenticate with API.
        Log.wtf(TAG, "Authenticate Login")
        if (loginForm.username.value == "hanmajid") {
            authenticationState.value = AuthenticationState.AUTHENTICATED
        } else {
            authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
        }
    }

    fun authRegister(registerForm: RegisterForm) {
        // TODO: Authenticate with API.
        Log.wtf(TAG, "Authenticate Register")
        if (registerForm.username.value == "hanmajid") {
            authenticationState.value = AuthenticationState.AUTHENTICATED
        } else {
            authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
        }
    }
}