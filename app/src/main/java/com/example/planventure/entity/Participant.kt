package com.example.planventure.entity

class Participant(
    private var id: Long,
    private var name: String
) {

    fun getId(): Long{return id}
    fun getName(): String{return name}
}