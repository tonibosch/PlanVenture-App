package com.example.planventure.utility

import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatePicker(
    private val supportFragmentManager: FragmentManager,
    private val tvStartDate: TextView,
    private val tvEndDate: TextView
) {

    fun showDateRangePicker() {
        val dateRangerPicker =
            MaterialDatePicker.Builder.dateRangePicker().setTitleText("Select Date").build()
        dateRangerPicker.show(supportFragmentManager, "date_range_picker")
        dateRangerPicker.addOnPositiveButtonClickListener { datePicked ->
            val firstDate = datePicked.first
            val secondDate = datePicked.second

            if (firstDate != null && secondDate != null) {
                tvStartDate.text = convertLongToDate(firstDate)
                tvEndDate.text = convertLongToDate(secondDate)
            }
        }
    }

    private fun convertLongToDate(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }


}