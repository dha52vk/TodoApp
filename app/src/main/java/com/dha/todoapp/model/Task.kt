package com.dha.todoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey
    val id: String,
    var title: String,
    var dateModified: Date,
    var isCompleted: Boolean,
)