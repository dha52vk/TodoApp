package com.dha.todoapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.dha.todoapp.model.TaskManager
import com.dha.todoapp.model.TaskPage
import com.dha.todoapp.ui.theme.TodoAppTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TaskManager.setContext(this)
        enableEdgeToEdge()
        // Loading
        setContent {
            TodoAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box{
                        Text(modifier = Modifier.align(Alignment.Center), text = "Loading...")
                    }
                }
            }
        }

        lifecycleScope.launch {
            TaskManager.refreshTasks()
            setContent {
                TodoAppTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {}
                    Surface(modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()) {
                        TaskPage()
                    }
                }
            }
        }
    }
}