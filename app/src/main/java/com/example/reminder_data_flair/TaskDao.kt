package com.example.reminder_data_flair

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface TaskDao {
    @Insert
    fun insert(task: Task)
}