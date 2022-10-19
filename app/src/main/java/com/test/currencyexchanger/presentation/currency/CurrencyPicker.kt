package com.test.currencyexchanger.presentation.currency

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.test.currencyexchanger.R
import com.test.currencyexchanger.domain.model.Currency
import com.test.currencyexchanger.ui.components.SelectDropDown
import org.koin.androidx.compose.getViewModel

@Composable
fun CurrencyPicker(
    value: Currency?,
    onValueChange: (Currency) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    viewModel: CurrencyViewModel = getViewModel(),
) = with(viewModel.viewState) {
    Row(
        modifier = Modifier
            .height(intrinsicSize = IntrinsicSize.Min)
            .then(other = modifier),
    ) {
        SelectDropDown(
            modifier = Modifier
//                .weight(weight = 1f)
//                .fillMaxHeight()
//                .width(300.dp)
                .width(IntrinsicSize.Min),
            selectedItem = value,
            items = currencies.toTypedArray(),
            placeholder = { Text(text = stringResource(R.string.select)) },
            icon = { modifier ->
                Icon(imageVector = Icons.Rounded.KeyboardArrowDown, contentDescription = null)

            },
            isError = isError,
            onItemClick = { index, _ ->
                onValueChange(currencies[index])
            },
            onExpand = viewModel::loadCurrencies,
            dropDownHeader = {
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (showProgress) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(alignment = Alignment.BottomCenter)
                        )
                    }
                }
                if (currencies.isEmpty()) {
                    SearchNoMatches(modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
                }
            },
        ) { currency ->
            Text(
                text = currency.symbol,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }

    error?.run {
        Toast.makeText(LocalContext.current, localizedMessage, Toast.LENGTH_LONG).show()
        viewModel.onErrorShown()
    }
}

@Composable
fun SearchNoMatches(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.then(other = Modifier.padding(vertical = 16.dp)),
        text = "No matches found",
    )
}

