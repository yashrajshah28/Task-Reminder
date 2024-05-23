package com.example.reminder_data_flair

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        //fetching the task name and details from the main activity
        val title = intent.getStringExtra("title")
        val details = intent.getStringExtra("desc")

        //displaying the latest fetched reminder's name and details from the main activity
        val taskname = findViewById<TextView>(R.id.taskname).apply{
            text = title
        }
        val taskdesc = findViewById<TextView>(R.id.taskdesc).apply {
            text = details
        }
    }
}