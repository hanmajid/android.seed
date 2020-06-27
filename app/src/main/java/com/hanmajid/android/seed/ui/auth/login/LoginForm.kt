package com.hanmajid.android.seed.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

class LoginForm {
    val username = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")

    val usernameTouched = MutableLiveData<Boolean>(false)
    val errorUsername: LiveData<String> = Transformations.map(username) {
        if (it.isEmpty()) {
            "Please fill in the username"
        } else {
            ""
        }
    }

    val passwordTouched = MutableLiveData<Boolean>(false)
    val errorPassword: LiveData<String> = Transformations.map(password) {
        if (it.isEmpty()) {
            "Please fill in the password"
        } else {
            ""
        }
    }

    fun touchAll() {
        usernameTouched.value = true
        passwordTouched.value = true
    }

    fun isValid(): Boolean {
        return username.value!!.isNotEmpty() && password.value!!.isNotEmpty()
    }
}