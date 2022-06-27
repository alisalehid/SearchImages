package com.payback.ui.utilities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.payback.ui.R

fun Fragment.setToolbar(toolbar: Toolbar) {
    (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
    (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    (requireActivity() as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
}

fun Activity.changeStatusBar(shouldBeLight: Boolean) {
    WindowInsetsControllerCompat(window, window.decorView.rootView).isAppearanceLightStatusBars =
        shouldBeLight
    window.statusBarColor = if (shouldBeLight) getColor(R.color.white) else getColor(R.color.black)
}
