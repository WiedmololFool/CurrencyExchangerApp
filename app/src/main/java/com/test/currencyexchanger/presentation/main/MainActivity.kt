package com.test.currencyexchanger.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.test.currencyexchanger.R
import com.test.currencyexchanger.domain.model.ExchangeInput
import com.test.currencyexchanger.presentation.Dialog
import com.test.currencyexchanger.presentation.CurrencyInput
import com.test.currencyexchanger.presentation.currency.CurrencyPicker
import com.test.currencyexchanger.ui.components.BrandButton
import com.test.currencyexchanger.ui.components.TextInput
import com.test.currencyexchanger.ui.components.TopBar
import com.test.currencyexchanger.ui.theme.CurrencyExchangerTheme
import com.test.currencyexchanger.utils.Utility
import com.test.currencyexchanger.utils.Utility.toMoneyFormat
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadUserProfile()
        println("Is network available ${Utility.isNetworkAvailable(applicationContext)}")
        setContent {
            CurrencyExchangerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopBar(
                            modifier = Modifier.fillMaxWidth(),
                            title = stringResource(R.string.main_screen_title),
                        )
                    }
                ) { paddingValues ->
                    paddingValues
                    Content(
                        modifier = Modifier.fillMaxSize(),
                        viewState = viewModel.viewState,
                        onSubmit = viewModel::convertBalance,
                        onUpdateInput = viewModel::updateInput
                    )
                    with(viewModel.viewState) {
                        Dialog(
                            visible = showCurrencyConvertedDialog,
                            text = "You converted ${input.amount} ${input.soughtCurrency?.symbol} " +
                                    "to ${convertedInputValue.toMoneyFormat()} ${input.boughtCurrency?.symbol}." +
                                    " Commission Fee - ${userProfile?.commissionFee?.toMoneyFormat()} ${input.soughtCurrency?.symbol}.",
                            title = getString(R.string.currency_converted),
                            onDismiss = { viewModel.showCurrencyConvertedDialog(show = false) },
                            closeText = stringResource(R.string.done)
                        )
                        Dialog(
                            visible = viewModel.viewState.error != null,
                            text = viewModel.viewState.error?.message.orEmpty(),
                            title = stringResource(id = R.string.error),
                            onDismiss = viewModel::onErrorShown,
                            closeText = stringResource(R.string.close)
                        )
                    }

                }
            }
        }
    }
}


@Composable
fun Content(
    modifier: Modifier,
    viewState: MainViewState,
    onSubmit: () -> Unit,
    onUpdateInput: (ExchangeInput) -> Unit,
) {
    Column(modifier = modifier.padding(16.dp)) {
        with(viewState) {
            if (showScreenLoadingProgress) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                Text(
                    modifier = Modifier.padding(vertical = 10.dp),
                    text = stringResource(R.string.my_balances).uppercase(),
                    style = MaterialTheme.typography.caption
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(space = 26.dp),
                ) {
                    items(
                        items = viewState.userProfile?.balances ?: listOf(),
                        itemContent = { balance ->
                            CompositionLocalProvider(
                                LocalTextStyle provides MaterialTheme.typography.h1,
                            ) {
                                Text(text = "${balance.value.toMoneyFormat()} ${balance.currency.symbol}")
                            }
                        })
                }
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = stringResource(R.string.currency_exchange).uppercase(),
                    style = MaterialTheme.typography.caption
                )
                CurrencyInput(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.sell),
                    content = {
                        TextInput(
                            enabled = viewState.input.boughtCurrency != null && viewState.input.soughtCurrency != null,
                            value = input.amount.orEmpty()
                            ,
                            onValueChange = { value ->
                                onUpdateInput(input.copy(amount = value.filter { it.isDigit() || it == '.' }))
                            },
                            placeholder = {
                                val text =
                                    if (viewState.input.boughtCurrency != null && viewState.input.soughtCurrency != null) stringResource(
                                        R.string.enter_value
                                    )
                                    else stringResource(R.string.select_currencies_first)
                                Text(text = text)
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                            )
                    },
                    currencyPicker = {
                        CurrencyPicker(
                            modifier = Modifier,
                            value = input.soughtCurrency,
                            onValueChange = { value ->
                                onUpdateInput(input.copy(soughtCurrency = value))
                            })
                    },
                    icon = {
                        Icon(
                            modifier = Modifier
                                .size(50.dp),
                            tint = Color.Red,
                            painter = painterResource(id = R.drawable.ic_baseline_arrow_circle_up_24),
                            contentDescription = null
                        )
                    }
                )
                CurrencyInput(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.receive),
                    content = {
                        if (viewState.showConversionProgress) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(all = 16.dp)
                            )
                        } else {
                            Text(
                                modifier = Modifier,
                                text = "+ ${convertedInputValue.toMoneyFormat()}",
                                textAlign = TextAlign.End,
                                color = MaterialTheme.colors.secondary
                            )
                        }
                    },
                    currencyPicker = {
                        CurrencyPicker(
                            modifier = Modifier,
                            value = input.boughtCurrency,
                            onValueChange = { value ->
                                onUpdateInput(input.copy(boughtCurrency = value))
                            })
                    },
                    icon = {
                        Icon(
                            modifier = Modifier
                                .size(50.dp),
                            tint = MaterialTheme.colors.secondary,
                            painter = painterResource(id = R.drawable.ic_baseline_arrow_circle_down_24),
                            contentDescription = null
                        )
                    }
                )
                BrandButton(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, top = 50.dp),
                    onClick = onSubmit
                ) {
                    Text(stringResource(R.string.submit).uppercase())
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CurrencyExchangerTheme {
        Content(
            modifier = Modifier,
            viewState = MainViewState(),
            onSubmit = {},
            onUpdateInput = {})
    }
}