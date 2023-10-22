package com.example.planventure

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class ExpenseActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var addExpenseButton: Button


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_expense)

        backButton = findViewById(R.id.backButton_Expenses)

        backButton.setOnClickListener{
            this.finish()
        }

        addExpenseButton = findViewById(R.id.addExpenseButton)

        addExpenseButton.setOnClickListener {
            val intent = Intent(this, CreateExpenseActivity::class.java)
            startActivity(intent)
        }



    }
}