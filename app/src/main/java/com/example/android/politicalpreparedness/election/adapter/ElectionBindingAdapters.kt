package com.example.android.politicalpreparedness.election.adapter

import android.text.format.DateUtils
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.util.*


@BindingAdapter("textDate")
fun dateFormat(textView: TextView, value: Date?) {
    val localizedDate = DateUtils.formatDateTime(
            textView.context,
            value?.time!!,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_WEEKDAY or DateUtils.FORMAT_SHOW_YEAR
    )
    textView.text = localizedDate
}