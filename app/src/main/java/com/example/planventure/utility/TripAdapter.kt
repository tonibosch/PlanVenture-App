package com.example.planventure.utility

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planventure.R
import com.example.planventure.entity.Trip
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TripAdapter(var mList: List<Trip>): RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    inner class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.titleTv)
        val dates: TextView = itemView.findViewById(R.id.datesTv)
        val location: TextView = itemView.findViewById(R.id.locationTv)
    }

    var onItemClick : ((Trip) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_each_trip,parent,false)
        return TripViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = mList[position]
        holder.name.text = "        ${trip.getName()}"
        holder.dates.text = "        From " + convertDate(trip.getStartDate())+" to " + convertDate(trip.getEndDate())
        holder.location.text = "        Location: ${trip.getLocation()}"

        holder.itemView.setOnClickListener {
            println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ")
            println(trip.getId().toString())
            onItemClick?.invoke(trip)
        }
    }

    private fun convertDate(date: Date): String? {
        return try {
            val desiredFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            desiredFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            "Error during Date conversion"
        }
    }

}