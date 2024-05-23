package com.example.reminder_data_flair

import android.app.*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.EditText
import android.widget.Toast
import com.example.reminder_data_flair.databinding.ActivityMainBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity()
{
    private lateinit var binding : ActivityMainBinding
    private lateinit var appDb : AppDatabase
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDb = AppDatabase.getDatabase(this)

        createNotificationChannel()
        binding.submitButton.setOnClickListener { scheduleNotification() }
        binding.reminders.setOnClickListener {  changeActivity()  }
    }

    private fun createNotificationChannel()
    {
        val name = "Notification Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun scheduleNotification()
    {
        val intent = Intent(applicationContext, Notification::class.java)
        val title = binding.titleET.text.toString()
        val message = binding.messageET.text.toString()
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        showAlert(time, title, message)
    }

    private fun getTime(): Long
    {
        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }

    private fun showAlert(time: Long, title: String, message: String)
    {
        val date = Date(time)
        val dateFormat = DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle("Reminder Scheduled")
            .setMessage(
                "Title: " + title +
                        "\nMessage: " + message +
                        "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date))
            .setPositiveButton("Okay"){_,_ ->writeData()}
            .show()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun writeData() {
        val tasktitle = binding.titleET.text.toString()
        val desc = binding.messageET.text.toString()

        if(tasktitle.isNotEmpty() && desc.isNotEmpty()){
            val task = Task(
                null,tasktitle,desc
            )
            GlobalScope.launch(Dispatchers.IO){
                appDb.taskDao().insert(task)
            }
            Toast.makeText(this, "Reminder saved to database", Toast.LENGTH_SHORT).show()
        }
    }

    private fun changeActivity() {
        val task = findViewById<EditText>(R.id.titleET)
        val desc = findViewById<EditText>(R.id.messageET)
        val taskTitle = task.text.toString()
        val taskDetails = desc.text.toString()

        val intent = Intent(this, MainActivity2::class.java).also {
            it.putExtra("title", taskTitle)
            it.putExtra("desc", taskDetails)
            startActivity(it)
        }
    }

}
