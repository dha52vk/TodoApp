package com.dha.todoapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {
    private val _taskTodo = MutableLiveData<List<Task>>()
    val taskTodo: LiveData<List<Task>> = _taskTodo;
    private val _taskDone = MutableLiveData<List<Task>>()
    val taskDone: LiveData<List<Task>> = _taskDone;

    init {
        getAllTask()
    }

    private fun getAllTask(){
        _taskTodo.value = TaskManager.getAllTaskTodo().reversed()
        _taskDone.value = TaskManager.getAllTaskDone().reversed()
    }

    fun addTodo(title: String){
        viewModelScope.launch {
            TaskManager.addTodo(title)
            getAllTask()
        }
    }

    fun removeTask(ids: List<String>){
        viewModelScope.launch {
            ids.forEach {
                TaskManager.removeTask(it)
            }
            getAllTask()
        }
    }

    fun removeTask(id: String){
        removeTask(listOf(id))
    }

    fun markAsCompleted(id: String) {
        viewModelScope.launch {
            TaskManager.markAsCompleted(id)
            getAllTask()
        }
    }

    fun markAsUncompleted(id: String) {
        viewModelScope.launch {
            TaskManager.markAsUncompleted(id)
            getAllTask()
        }
    }

    fun editTodo(editTodoId: String, editTodoText: String) {
        viewModelScope.launch {
            TaskManager.editTodo(editTodoId, editTodoText)
            getAllTask()
        }
    }
}
