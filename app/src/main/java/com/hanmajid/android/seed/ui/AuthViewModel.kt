package com.hanmajid.android.seed.ui

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

    val isFinishedOnboarding = MutableLiveData<Boolean>(false)

    val authenticationState =
        MutableLiveData<AuthenticationState>(AuthenticationState.UNAUTHENTICATED)

    fun finishOnboarding() {
        isFinishedOnboarding.value = true
    }

    fun refuseAuthentication() {
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }

    fun authLogin(loginForm: LoginForm) {
        // TODO: Authenticate with API.
        if (loginForm.username.value!!.isNotEmpty()) {
            authenticationState.value = AuthenticationState.AUTHENTICATED
        } else {
            authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
        }
    }

    fun authRegister(registerForm: RegisterForm) {
        // TODO: Authenticate with API.
        if (registerForm.username.value!!.isNotEmpty()) {
            authenticationState.value = AuthenticationState.AUTHENTICATED
        } else {
            authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
        }
    }
}