package com.example.planventure

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class ExpenseActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_expense)

        backButton = findViewById(R.id.backButton_Expenses)

        backButton.setOnClickListener{
            this.finish()
        }


    }
}