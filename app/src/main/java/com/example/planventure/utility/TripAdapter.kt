package com.example.planventure.utility

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.planventure.R
import com.example.planventure.entity.Trip
import com.example.planventure.service.TripService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
/**
 * RecyclerView Adapter for displaying a list of trips.
 *
 * @constructor Creates a TripAdapter with the specified list of trips and application context.
 * @param mList The list of trips to be displayed.
 * @param applicationContext The application context.
 */
@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("NotifyDataSetChanged")
class TripAdapter(
    private var mList: ArrayList<Trip>,
    applicationContext: Context
    ): RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    private val tripService = TripService(applicationContext)

    /**
     * ViewHolder class for holding trip item views.
     *
     * @property name The TextView displaying the trip name.
     * @property dates The TextView displaying the trip start and end dates.
     * @property location The TextView displaying the trip location.
     */
    inner class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.titleTv)
        val dates: TextView = itemView.findViewById(R.id.datesTv)
        val location: TextView = itemView.findViewById(R.id.locationTv)
        val status: TextView = itemView.findViewById(R.id.statusTv)
    }

    var onItemClick : ((Trip) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_each_trip,parent,false)
        return TripViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = mList[position]
        holder.name.text = "        ${trip.getName()}"
        holder.dates.text = "        From " + convertDate(trip.getStartDate())+" to " + convertDate(trip.getEndDate())
        holder.location.text = "        Location: ${trip.getLocation()}"
        holder.status.text = "        Status: ${trip.getState()}"

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(trip)
        }
    }

    /**
     * Converts a Date object to a formatted date string.
     *
     * @param date The Date object to be converted.
     * @return The formatted date string (yyyy-MM-dd).
     */
    private fun convertDate(date: Date): String? {
        return try {
            val desiredFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            desiredFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            "Error during Date conversion"
        }
    }

    /**
     * Deletes a trip at the specified position.
     *
     * @param position The position of the trip to be deleted.
     */
    fun deleteTrip(position: Int){
        val id = mList[position].getId()
        mList.removeAt(position)
        tripService.deleteTripById(id)
        notifyDataSetChanged()
    }

    /**
     * Archives a trip at the specified position with the selected status.
     *
     * @param position The position of the trip to be archived.
     * @param statusSelected The selected status.
     */
    fun archiveTrip(position: Int, statusSelected: String){
        val id = mList[position].getId()
        if(statusSelected == "PLANNING" || statusSelected == "STARTED") mList.removeAt(position)
        tripService.finishTripById(id)
        notifyDataSetChanged()
    }
}