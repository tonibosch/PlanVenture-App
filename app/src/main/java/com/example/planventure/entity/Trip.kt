package com.example.planventure.entity

import com.example.planventure.enumerations.TRIP_STATE
import java.util.Date

class Trip(
    private var id: Long,
    private var name: String,
    private var date: Date,
    private var location: String,
    private var participants: ArrayList<Participant>,
    private var expences: ArrayList<Expence>,
    tripState: TRIP_STATE
) {
}