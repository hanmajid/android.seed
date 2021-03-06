package com.hanmajid.android.seed.ui.auth.login

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class LoginViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val TAG = "LoginViewModel"

    enum class LoginState {
        FORM_INVALID,
        FORM_VALID
    }

    val loginForm = LoginForm()
    val loginState = MutableLiveData<LoginState>(LoginState.FORM_INVALID)

    fun doLogin() {
        if (!loginForm.isValid()) {
            loginState.value = LoginState.FORM_INVALID
            loginForm.touchAll()
        } else {
            loginState.value = LoginState.FORM_VALID
        }
    }

    fun refuseLogin() {
        loginState.value = LoginState.FORM_INVALID
    }
}