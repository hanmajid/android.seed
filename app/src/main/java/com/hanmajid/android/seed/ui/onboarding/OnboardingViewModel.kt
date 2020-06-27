package com.hanmajid.android.seed.ui.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OnboardingViewModel : ViewModel() {

    val pagerPosition = MutableLiveData<Int>(0)

    val maxPagerPosition = 2

    fun changePagerPosition(newPosition: Int) {
        pagerPosition.value = newPosition
    }
}