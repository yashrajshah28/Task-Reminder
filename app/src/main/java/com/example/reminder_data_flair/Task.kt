package com.example.reminder_data_flair

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "task") val task: String?,
    @ColumnInfo(name = "taskDesc") val desc: String?
    )
