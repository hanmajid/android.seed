package com.hanmajid.android.seed.ui.auth.register

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class RegisterViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
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