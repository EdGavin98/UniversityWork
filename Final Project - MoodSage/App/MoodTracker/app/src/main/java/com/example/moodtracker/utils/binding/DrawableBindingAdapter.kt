package com.example.moodtracker.utils.binding

import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.moodtracker.R

@BindingAdapter("sideDrawableColour")
fun ImageView.setDrawableColour(severity: Int) {
    Log.d("Hello", "$severity")
    this.setColorFilter(
        when (severity) {
            1, 2 -> Color.parseColor("#00FF00")
            3, 4 -> Color.parseColor("#88FF00")
            5, 6 -> Color.parseColor("#FFFF00")
            7, 8 -> Color.parseColor("#FF8800")
            9, 10 -> Color.parseColor("#FF0000")
            else -> R.color.colorPrimary
        }
    )
}