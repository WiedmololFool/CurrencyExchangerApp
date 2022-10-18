package com.test.currencyexchanger.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    onNavigateUp: (() -> Unit)? = null,
    elevation: Dp = 0.dp,
    contentPadding: PaddingValues = AppBarDefaults.ContentPadding,
    shape: Shape = RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp),
    backgroundColor: Color = MaterialTheme.colors.secondary,
    contentColor: Color = contentColorFor(backgroundColor = backgroundColor),
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        color = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
        shape = shape,
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
            Box(modifier = Modifier.fillMaxWidth()) {
                val padding = if (onNavigateUp == null)
                    contentPadding
                else
                    PaddingValues(horizontal = 52.dp)
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                        .padding(paddingValues = padding),
                    content = content,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues = contentPadding)
                        .heightIn(min = 72.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    onNavigateUp?.run {
                        IconButton(onClick = this) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.ic_arrow_back_new),
//                                contentDescription = stringResource(id = R.string.content_description_navigate_back),
//                                tint = Color.Unspecified
//                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onNavigateUp: (() -> Unit)? = null,
    elevation: Dp = 0.dp,
    backgroundColor: Color = MaterialTheme.colors.primary,
) {
    TopBar(
        modifier = modifier,
        onNavigateUp = onNavigateUp,
        elevation = elevation,
        backgroundColor = backgroundColor,
        contentColor = contentColorFor(backgroundColor = backgroundColor),
        shape = RoundedCornerShape(bottomStart = 14.dp, bottomEnd = 14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.body1,
                color = contentColorFor(backgroundColor = backgroundColor)
            )
            subtitle?.run {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                    Text(
                        text = this,
                        style = MaterialTheme.typography.caption.copy(color = contentColorFor(
                            backgroundColor = backgroundColor).copy(alpha = 0.5f)),
                    )
                }
            }
        }
    }
}
