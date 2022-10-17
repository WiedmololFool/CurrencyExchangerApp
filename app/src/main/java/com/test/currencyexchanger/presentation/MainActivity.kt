package com.test.currencyexchanger.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.test.currencyexchanger.domain.model.Balance
import com.test.currencyexchanger.domain.model.ExchangeInput
import com.test.currencyexchanger.ui.theme.CurrencyExchangerTheme
import com.test.currencyexchanger.utils.Utility
import okhttp3.internal.connection.Exchange
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel.fetchAllCurrencies()
        viewModel.loadUserProfile()
        println("Is network available ${Utility.isNetworkAvailable(applicationContext)}")
        setContent {
            CurrencyExchangerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Content(
                        modifier = Modifier.fillMaxSize(),
                        viewState = viewModel.viewState,
                        onButtonClick = { },
                        onUpdateInput = viewModel::updateInput
                    )
                }
            }
        }
    }
}

@Composable
fun Content(
    modifier: Modifier,
    viewState: MainViewState,
    onButtonClick: () -> Unit,
    onUpdateInput: (ExchangeInput) -> Unit,
) {
    Column(modifier = modifier) {
        with(viewState) {
            if (showProgress) {
                CircularProgressIndicator(modifier = Modifier.padding(all = 16.dp))
            } else {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(all = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
                ) {
                    //TODO remove filtering at least to viewModel
                    val fullBalanceList = viewState.userProfile?.balances
                    val euroBalance = fullBalanceList?.find {
                        it.currency.symbol == "EUR"
                    }
                    val dollarBalance = fullBalanceList?.find {
                        it.currency.symbol == "USD"
                    }
                    items(
                        items = listOf(euroBalance, dollarBalance),
                        itemContent = { balance ->
                            Text(text = "${balance?.currency?.symbol} ${balance?.value}")
                        })
                }
                Text(text = "Sale ${input.soughtCurrency} : $convertedInputValue")
                Text(text = "Buy ${input.boughtCurrency} : ")
                BasicTextField(
                    modifier = Modifier.background(color = Color.White),
                    value = viewState.input.amount.toString(),
                    onValueChange = { value ->
                        onUpdateInput(viewState.input.copy(amount = value.toDouble()))
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),

                    )
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = onButtonClick
                ) {
                    Text("Click me")
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
            onButtonClick = {},
            onUpdateInput = {})
    }
}