package com.dha.todoapp.model

import android.content.Context
import java.time.Instant
import java.util.Date
import java.util.UUID

object TaskManager {
    private val tasks: MutableList<Task> = mutableListOf()
    private lateinit var taskDatabase: TaskDatabase

    fun setContext(context: Context) {
        taskDatabase = TaskDatabase.getInstance(context)
    }

    suspend fun refreshTasks(){
        tasks.clear()
        tasks.addAll(taskDatabase.taskDao().getAllTask())
    }

    suspend fun addTodo(title: String) {
//        tasks.add(Task(UUID.randomUUID().toString(), title, Date.from(Instant.now()), false))
        taskDatabase.taskDao().addTodo(Task(UUID.randomUUID().toString(), title, Date.from(Instant.now()), false))
        refreshTasks()
    }

    suspend fun editTodo(id: String, title: String) {
//        tasks.add(Task(UUID.randomUUID().toString(), title, Date.from(Instant.now()), false))
        val task = tasks.find { it.id == id }
        if (task == null)
            return
        task.title = title
        task.dateModified = Date.from(Instant.now())
        taskDatabase.taskDao().editTask(task)
        refreshTasks()
    }

    suspend fun removeTask(id: String) {
//        tasks.removeIf{ it.id == id }
        taskDatabase.taskDao().removeTask(id)
        refreshTasks()
    }

    suspend fun markAsCompleted(id: String){
//        val task = tasks.find { it.id == id }
//        if (task != null)
//            task.isCompleted = true
        taskDatabase.taskDao().markAsCompleted(id)
        refreshTasks()
    }

    suspend fun markAsUncompleted(id: String){
//        val task = tasks.find { it.id == id }
//        if (task != null)
//            task.isCompleted = false
        taskDatabase.taskDao().markAsUncompleted(id)
        refreshTasks()
    }

    fun getAllTask(): List<Task>{
        return tasks
    }

    fun getAllTaskTodo(): List<Task>{
        return tasks.filter { !it.isCompleted }
    }

    fun getAllTaskDone(): List<Task>{
        return tasks.filter { it.isCompleted }
    }
}