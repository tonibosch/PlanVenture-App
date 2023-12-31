package com.example.planventure.utility

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planventure.R
import com.example.planventure.entity.Participant

class ExpenseParticipantAdapter (
    private var participants: ArrayList<Participant>
) : RecyclerView.Adapter<ExpenseParticipantAdapter.ParticipantViewHolder>(){
    class ParticipantViewHolder(view: View) : RecyclerView.ViewHolder(view)
    private var checkedParticipants = ArrayList<Participant>()
    var paidBy: Participant? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        return ParticipantViewHolder((
                LayoutInflater.from(parent.context).inflate(
                    R.layout.fragment_expense_participants,
                    parent,
                    false
                )
                ))
    }

    override fun getItemCount(): Int {
        return participants.size
    }

    fun getCheckedParticipants(): ArrayList<Participant> {
        return checkedParticipants
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        val currentParticipant = participants[position]

        holder.itemView.apply{
            findViewById<TextView>(R.id.ExpenseParticipantTV).text = currentParticipant.getName()
            val checkbox = findViewById<CheckBox>(R.id.ExpenseParticipantCB)
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    checkedParticipants.add(currentParticipant)
                } else{
                    checkedParticipants.remove(currentParticipant)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateParticipants(participants:ArrayList<Participant>){
        this.participants = participants
        checkedParticipants = ArrayList()
        notifyDataSetChanged()
    }

}