package com.example.planventure.utility

import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utility class for handling date range selection using MaterialDatePicker.
 *
 * @property supportFragmentManager The FragmentManager used to show the date range picker.
 * @property tvStartDate The TextView to display the selected start date.
 * @property tvEndDate The TextView to display the selected end date.
 */
class DatePicker(
    private val supportFragmentManager: FragmentManager,
    private val tvStartDate: TextView,
    private val tvEndDate: TextView
) {

    /**
     * Shows the date range picker dialog and updates the specified TextViews
     * with the selected start and end dates.
     */
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

    /**
     * Converts a long timestamp to a formatted date string.
     *
     * @param time The timestamp to be converted.
     * @return A formatted date string (yyyy-MM-dd).
     */
    private fun convertLongToDate(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }


}