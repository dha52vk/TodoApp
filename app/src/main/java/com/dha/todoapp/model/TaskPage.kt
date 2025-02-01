package com.dha.todoapp.model

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dha.todoapp.R
import com.dha.todoapp.uimodel.ConfirmationDialog
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun TaskPage(taskViewModel: TaskViewModel = TaskViewModel()) {
    val taskTodo = taskViewModel.taskTodo.observeAsState()
    val taskDone = taskViewModel.taskDone.observeAsState()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var addTodoDialogVisible by remember { mutableStateOf(false) }
    var addTodoText by remember { mutableStateOf("") }
    var editTodoDialogVisible by remember { mutableStateOf(false) }
    var editTodoText by remember { mutableStateOf("") }
    var itemClickId by remember { mutableStateOf("") }
    var longClickMenuVisible by remember { mutableStateOf(false) }
    var clearAllDoneTaskConfirmDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            var floatingMenuExpanded by remember { mutableStateOf(false) }
            Column(horizontalAlignment = Alignment.End) {
                AnimatedVisibility(
                    visible = floatingMenuExpanded,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it }) + expandVertically(),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it }) + shrinkVertically()
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        ItemFAB(text = stringResource(R.string.add_random_todo), icon = Icons.Default.AddCircle) {
                            taskViewModel.addTodo(UUID.randomUUID().toString())
                        }
                        ItemFAB(text = stringResource(R.string.clear_all_done_tasks), icon = Icons.Default.Clear) {
                            clearAllDoneTaskConfirmDialogVisible = true
                            floatingMenuExpanded = false
                        }
                        ItemFAB(text = stringResource(R.string.add_todo), icon = Icons.Default.Edit) {
                            addTodoDialogVisible = true
                            floatingMenuExpanded = false
                        }
                        ItemFAB(text = stringResource(R.string.goto_top), icon = Icons.Default.KeyboardArrowUp) {
                            scope.launch {
                                scrollState.animateScrollTo(0)
                                floatingMenuExpanded = false
                            }
                        }
                    }
                }
                val transition = updateTransition(targetState = floatingMenuExpanded, label = "rotation")
                val rotation by transition.animateFloat(label = "rotation") { state ->
                    if (state) 315f else 0f
                }
                FloatingActionButton(modifier = Modifier.size(60.dp),
                    onClick = { floatingMenuExpanded = !floatingMenuExpanded }) {
                    Icon(
                        modifier = Modifier.rotate(rotation),
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            TaskCard(
                taskViewModel,
                cardTitle = stringResource(R.string.todo_task_title),
                tasks = taskTodo,
                onItemClick = {
                    editTodoDialogVisible = true
                    editTodoText = it.title
                    itemClickId = it.id
                },
                onItemLongClick = {
                    longClickMenuVisible = true
                    editTodoText = it.title
                    itemClickId = it.id
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            TaskCard(
                taskViewModel,
                cardTitle = stringResource(R.string.done_task_title),
                tasks = taskDone,
                defaultExpanded = false
            )
        }

        if (addTodoDialogVisible) {
            AlertDialog(
                onDismissRequest = {
                    addTodoDialogVisible = false
                    addTodoText = ""
                },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.todo_field_title))
                        TextField(
                            value = addTodoText,
                            onValueChange = { addTodoText = it },
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (addTodoText.isNotEmpty())
                            taskViewModel.addTodo(addTodoText);
                        addTodoDialogVisible = false
                        addTodoText = ""
                    }) {
                        Text(stringResource(R.string.add))
                    }
                })
        }

        if (editTodoDialogVisible) {
            AlertDialog(
                onDismissRequest = {
                    editTodoDialogVisible = false
                    editTodoText = ""
                    itemClickId = ""
                },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.todo_field_title))
                        TextField(
                            value = editTodoText,
                            onValueChange = { editTodoText = it },
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (editTodoText.isNotEmpty())
                            taskViewModel.editTodo(itemClickId, editTodoText);
                        editTodoDialogVisible = false
                        editTodoText = ""
                        itemClickId = ""
                    }) {
                        Text(stringResource(R.string.save))
                    }
                })
        }

        if (longClickMenuVisible){
            AlertDialog(
                onDismissRequest = {
                    longClickMenuVisible = false
                },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Button(modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                editTodoDialogVisible = true
                                longClickMenuVisible = false
                            }) {
                            Text(stringResource(R.string.edit))
                        }
                        Button(modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                taskViewModel.removeTask(itemClickId)
                                longClickMenuVisible = false
                            }) {
                            Text(stringResource(R.string.remove))
                        }
                    }
                },
                confirmButton = {
                    Button(modifier = Modifier.fillMaxWidth(),
                        onClick = { longClickMenuVisible = false }) {
                        Text(stringResource(R.string.close))
                    }
                })
        }

        ConfirmationDialog(
            title = "Are you sure you want to delete all done tasks?",
            showDialog = clearAllDoneTaskConfirmDialogVisible,
            onDismiss = { clearAllDoneTaskConfirmDialogVisible = false },
            onConfirm = {
                taskViewModel.removeTask(taskDone.value?.map { it.id } ?: listOf())
                clearAllDoneTaskConfirmDialogVisible = false
            }
        )
    }
}

@Composable
fun ItemFAB(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier.padding(bottom = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text)
        Spacer(modifier = Modifier.width(10.dp))
        FloatingActionButton(onClick = onClick) {
            Icon(imageVector = icon, contentDescription = null)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskCard(
    taskViewModel: TaskViewModel, cardTitle: String, tasks: State<List<Task>?>,
    onItemClick: ((Task) -> Unit)? = null, onItemLongClick: ((Task) -> Unit)? = null,
    defaultExpanded: Boolean = true) {
    var expanded by remember { mutableStateOf(defaultExpanded) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
    ) {
        Column {
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = cardTitle, fontSize = 20.sp)
                if (expanded) {
                    Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null)
                } else {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
                }
            }

            if (expanded) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.primary
                )
                tasks.value?.forEach { task ->
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .combinedClickable(
                                enabled = true,
                                onClick = {
                                    onItemClick?.invoke(task)
                                },
                                onLongClick = {
                                    onItemLongClick?.invoke(task)
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = task.isCompleted, onCheckedChange = {
                            if (task.isCompleted) {
                                taskViewModel.markAsUncompleted(task.id)
                            } else {
                                taskViewModel.markAsCompleted(task.id)
                            }
                        })
                        Text(text = task.title, fontSize = 16.sp)
                    }
                } ?: Text(text = stringResource(R.string.no_task_title))
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}