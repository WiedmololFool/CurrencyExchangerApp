package com.test.currencyexchanger.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.test.currencyexchanger.ui.theme.CurrencyExchangerTheme

@Composable
fun CurrencyInput(
    modifier: Modifier,
    title: String,
    icon: @Composable () -> Unit,
    content: @Composable () -> Unit,
    currencyPicker: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        icon.invoke()
        Text(text = title)
        Spacer(Modifier.weight(1f))
        content.invoke()
        currencyPicker.invoke()
    }
    Divider()
}

@Preview(showBackground = true)
@Composable
fun SellInputPreview() {
    CurrencyExchangerTheme {
        CurrencyInput(
            icon = {},
            modifier = Modifier,
            content = {},
            title = "Title",
            currencyPicker = {}
        )
    }
}