package com.hanmajid.android.seed.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

class RegisterForm {
    val username = MutableLiveData<String>("")
    val email = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    val passwordConfirmation = MutableLiveData<String>("")

    val usernameTouched = MutableLiveData<Boolean>(false)
    val errorUsername: LiveData<String> = Transformations.map(username) {
        if (it.isEmpty()) {
            "Please fill in the username"
        } else {
            ""
        }
    }

    val emailTouched = MutableLiveData<Boolean>(false)
    val errorEmail: LiveData<String> = Transformations.map(email) {
        if (it.isEmpty()) {
            "Please fill in the email"
        } else if (!isEmailValid(it)) {
            "Please fill in a valid email address"
        } else {
            ""
        }
    }

    private fun isEmailValid(email: String) =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    val passwordTouched = MutableLiveData<Boolean>(false)
    val errorPassword: LiveData<String> = Transformations.map(password) {
        if (it.isEmpty()) {
            "Please fill in the password"
        } else {
            ""
        }
    }

    val passwordConTouched = MutableLiveData<Boolean>(false)
    val errorPasswordCon: LiveData<String> = Transformations.map(passwordConfirmation) {
        if (it.isEmpty()) {
            "Please fill in the password"
        } else if (it != password.value) {
            "Password confirmation must match"
        } else {
            ""
        }
    }

    fun touchAll() {
        usernameTouched.value = true
        emailTouched.value = true
        passwordTouched.value = true
        passwordConTouched.value = true
    }

    fun isValid(): Boolean {
        return username.value!!.isNotEmpty() &&
                email.value!!.isNotEmpty() && isEmailValid(email.value!!) &&
                password.value!!.isNotEmpty() &&
                passwordConfirmation.value!!.isNotEmpty() && passwordConfirmation.value!! == password.value!!
    }
}