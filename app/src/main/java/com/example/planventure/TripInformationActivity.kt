package com.example.planventure

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TripInformationActivity : AppCompatActivity() {

    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_information)

/*
        backButton = findViewById(R.id.backButton_TripInfo)


        backButton.setOnClickListener{
            this.finish()
        }
         */
    }
}