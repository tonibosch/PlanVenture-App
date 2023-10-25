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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_each_trip,parent,false)
        return TripViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.name.text = "        ${mList[position].getName()}"
        holder.dates.text = "        From " + convertDate(mList[position].getStartDate())+" to " + convertDate(mList[position].getEndDate())
        holder.location.text = "        Location: ${mList[position].getLocation()}"
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