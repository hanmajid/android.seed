package com.hanmajid.android.seed.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.hanmajid.android.seed.model.User
import com.hanmajid.android.seed.ui.auth.login.LoginForm
import com.hanmajid.android.seed.ui.auth.register.RegisterForm
import com.hanmajid.android.seed.util.ImageUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Singleton
class AuthViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val TAG = "AuthViewModel"

    enum class AuthenticationState {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        AUTHENTICATED,          // The user has authenticated successfully
        INVALID_AUTHENTICATION  // Authentication failed
    }

    val loggedInUser = MutableLiveData<User>()

    init {
        setUser(
            User(
                1,
                "hanmajid@gmail.com",
                "@hanmajid",
                "Muhammad Farhan Majid",
                "https://api.adorable.io/avatars/285/abott@adorable.png",
                null
            )
        )
    }

    val loggedInUserAvatarPalette = MutableLiveData<Palette?>()

    val isFinishedOnboarding = MutableLiveData<Boolean>(true)

    val authenticationState =
        MutableLiveData<AuthenticationState>(AuthenticationState.AUTHENTICATED)

    fun setUser(user: User) {
        loggedInUser.postValue(user)
        viewModelScope.launch(Dispatchers.IO) {
            val bitmap = ImageUtil.getBitmapFromURL(user.avatar)
            bitmap?.let { it ->
                Palette.from(it).generate { palette ->
                    loggedInUserAvatarPalette.postValue(palette)
                }
            }
        }
    }

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