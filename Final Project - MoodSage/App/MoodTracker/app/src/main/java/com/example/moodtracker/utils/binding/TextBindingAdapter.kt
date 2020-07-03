package com.example.moodtracker.utils.binding

import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.example.moodtracker.R
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

@BindingAdapter("android:dateText")
fun TextView.bindDate(date: LocalDate?) {
    date?.let {
        text = it.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
    }
}

@BindingAdapter("android:dateTimeText")
fun TextView.bindDateTime(date: LocalDateTime?) {
    date?.let {
        text = it.format(DateTimeFormatter.ofPattern("MMM dd, yyyy' at 'HH:mm"))
    }
}

@BindingAdapter("android:dateText")
fun TextView.bindDate(date: LocalDateTime?) {
    date?.let {
        text = it.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
    }
}

@BindingAdapter("showPrivateDrawable")
fun TextView.showDrawable(shouldShow : Boolean?) {
    @DrawableRes
    val drawable =  R.drawable.ic_vpn_key_grey_24dp

    shouldShow?.let { show ->
        if (show) {
            this.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, drawable,0)
        } else {
            this.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, 0,0)
        }
    }
}
