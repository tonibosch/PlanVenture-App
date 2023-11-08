package com.example.planventure.utility

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.planventure.R
import com.example.planventure.entity.Participant

class ExpensePartcipantAdapter (
    private var participants: ArrayList<Participant>,
    private val applicationContext: Context
) : RecyclerView.Adapter<ExpensePartcipantAdapter.ParticipantViewHolder>(){
    class ParticipantViewHolder(view: View) : RecyclerView.ViewHolder(view)
    private var checkedParticipants = ArrayList<Participant>()

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
        val currentParitcipant = participants[position]

        holder.itemView.apply{
            findViewById<TextView>(R.id.ExpenseParticipantTV).text = currentParitcipant.getName()
            var checkbox = findViewById<CheckBox>(R.id.ExpenseParticipantCB)
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    checkedParticipants.remove(currentParitcipant)
                } else{
                    checkedParticipants.add(currentParitcipant)
                }
            }
        }
    }

    fun updateParticipants(participants:ArrayList<Participant>){
        this.participants = participants
        checkedParticipants = ArrayList()
        notifyDataSetChanged()
    }

}