package com.hanmajid.android.seed.databinding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.hanmajid.android.seed.di.GlideApp

@BindingAdapter(value = ["backgroundIf", "backgroundIfColor"])
fun setBackgroundIf(view: View?, condition: Boolean?, backgroundIfColor: Int) {
    view?.apply {
        setBackgroundColor(
            if (condition == true) backgroundIfColor
            else ContextCompat.getColor(context, android.R.color.transparent)
        )
    }
}

@BindingAdapter("hidden")
fun setHidden(view: View?, hide: Boolean?) {
    view?.isVisible = hide != true
}

@BindingAdapter("navController", "onClickNavDirections")
fun onClickNavigate(
    view: View?,
    navController: NavController?,
    navDirections: NavDirections?
) {
    view?.apply {
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

@BindingAdapter("dividerItemDecorationVertical")
fun dividerItemDecorationVertical(recyclerView: RecyclerView?, enabled: Boolean) {
    recyclerView?.apply {
        if (enabled) {
            val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
        }
    }
}

@BindingAdapter("imgSrc")
fun imgSrc(imageView: ImageView?, url: String?) {
    imageView?.apply {
        if (url != null) {
            GlideApp
                .with(context)
                .load(url)
                .into(this)
        }
    }
}