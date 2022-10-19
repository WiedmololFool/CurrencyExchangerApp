package com.test.currencyexchanger.presentation

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.test.currencyexchanger.ui.theme.CurrencyExchangerTheme

@Composable
fun Dialog(
    visible: Boolean,
    title: String,
    text: String,
    onDismiss: () -> Unit,
    closeText: String
) {
    if (!visible) return

    AlertDialog(
        backgroundColor = MaterialTheme.colors.background,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(text = closeText) }
        },
        title = { Text(text = title) },
        text = { Text(text = text) },
    )
}


@Preview(showBackground = true)
@Composable
fun ErrorDialogPreview() {
    CurrencyExchangerTheme {
        Dialog(
            visible = true,
            text = "Some error",
            title = "Error",
            onDismiss = {},
            closeText = "Close"
        )
    }
}