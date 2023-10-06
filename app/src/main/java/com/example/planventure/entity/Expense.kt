package com.example.planventure.entity

class Expense(
    private var name: String,
    private var amount: Float
) {


    fun getName(): String{return name}
    fun getAmount(): Float{return amount}
}