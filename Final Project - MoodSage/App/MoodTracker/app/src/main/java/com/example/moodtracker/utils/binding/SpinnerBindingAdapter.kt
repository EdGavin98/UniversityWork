package com.example.moodtracker.utils.binding

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

// Get as string

@BindingAdapter("selectedFilterValue")
fun Spinner.setSelectedValue(selectedValue: String?) {
    if (adapter != null) {
        val position = (adapter as ArrayAdapter<Any>).getPosition(selectedValue)
        setSelection(position, false)
        tag = position
    }
}

@BindingAdapter("selectedFilterValueAttrChanged")
fun Spinner.setInverseBindingListener(inverseBindingListener: InverseBindingListener?) {
    onItemSelectedListener = if (inverseBindingListener == null) {
        null
    } else {
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (tag != position) {
                    inverseBindingListener.onChange()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}

@InverseBindingAdapter(attribute = "selectedFilterValue", event = "selectedFilterValueAttrChanged")
fun Spinner.getSelectedValue(): String? {
    return this.selectedItem as String
}

//Get as int

@BindingAdapter("selectedIntValue")
fun Spinner.setIntSelectedValue(selectedValue: Int?) {
    var value = selectedValue
    if (adapter != null) {
        if (value == 0) {
            value = 1
        }
        val position = (adapter as ArrayAdapter<Any>).getPosition(value.toString())
        setSelection(position, false)
        tag = position
    }
}

@BindingAdapter("selectedIntValueAttrChanged")
fun Spinner.setIntInverseBindingListener(inverseBindingListener: InverseBindingListener?) {
    onItemSelectedListener = if (inverseBindingListener == null) {
        null
    } else {
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (tag != position) {
                    inverseBindingListener.onChange()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}

@InverseBindingAdapter(attribute = "selectedIntValue", event = "selectedIntValueAttrChanged")
fun Spinner.getIntSelectedValue(): Int? {
    return (this.selectedItem as String).toInt()
}
