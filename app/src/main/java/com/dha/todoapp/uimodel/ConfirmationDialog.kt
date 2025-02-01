package com.dha.todoapp.uimodel

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dha.todoapp.R

@Composable
fun ConfirmationDialog(
    bigTitle: String = "Confirm",
    title: String,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(bigTitle) },
            text = { Text(title) },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }
}