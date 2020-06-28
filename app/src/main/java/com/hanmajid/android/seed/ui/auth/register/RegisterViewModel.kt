package com.hanmajid.android.seed.ui.auth.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    private val TAG = "RegisterViewModel"

    enum class RegisterState {
        FORM_INVALID,
        FORM_VALID
    }

    val registerForm = RegisterForm()
    val registerState = MutableLiveData<RegisterState>(RegisterState.FORM_INVALID)

    fun doRegister() {
        if (!registerForm.isValid()) {
            registerState.value = RegisterState.FORM_INVALID
            registerForm.touchAll()
        } else {
            registerState.value = RegisterState.FORM_VALID
        }
    }

    fun refuseRegister() {
        registerState.value = RegisterState.FORM_INVALID
    }
}