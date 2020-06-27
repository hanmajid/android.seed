package com.hanmajid.android.seed.databinding

import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.navigation.NavController
import androidx.navigation.NavDirections

@BindingAdapter("navController", "onClickNavDirections")
fun onClickNavigate(button: Button?, navController: NavController?, navDirections: NavDirections?) {
    button?.apply {
        if (navController != null && navDirections != null) {
            setOnClickListener {
                navController.navigate(navDirections)
            }
        }
    }
}

@BindingAdapter("viewPagerPosition", "viewPagerMaxPosition")
fun viewPagerPositionText(textView: TextView?, position: Int?, maxPosition: Int?) {
    textView?.apply {
        if (position != null && maxPosition != null) {
            text = "${position + 1} of ${maxPosition + 1}"
        }
    }
}