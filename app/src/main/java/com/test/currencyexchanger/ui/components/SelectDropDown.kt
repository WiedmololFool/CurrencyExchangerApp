package com.test.currencyexchanger.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.test.currencyexchanger.ui.theme.ColorPalette
import com.test.currencyexchanger.ui.theme.CurrencyExchangerTheme
import com.test.currencyexchanger.ui.theme.TextPrimary
import com.test.currencyexchanger.ui.theme.TextSecondary
import com.trustthq.ui.design_system.border.BorderColors
import com.trustthq.ui.design_system.border.BorderDefaults
import com.trustthq.ui.design_system.border.animateBorderStrokeAsState
import com.trustthq.ui.design_system.scroll.drawVerticalScrollbar


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> SelectDropDown(
    selectedItem: T?,
    items: Array<T>,
    onItemClick: (index: Int, item: T) -> Unit,
    placeholder: @Composable () -> Unit,
    icon: @Composable (modifier: Modifier) -> Unit,
    modifier: Modifier = Modifier,
    dropDownHeader: (@Composable ColumnScope.() -> Unit)? = null,
    onExpand: (() -> Unit)? = null,
    onEndReached: ((item: T) -> Unit)? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    elevation: Dp = 0.dp,
    placeholderMode: PlaceholderMode = PlaceholderMode.NoAnimation,
    shape: Shape = SelectDropDownDefaults.Shape,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor = backgroundColor),
    borderColors: BorderColors? = BorderDefaults.borderColors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    itemContent: @Composable (item: T) -> Unit,
) {
    val borderStroke = borderColors?.let { colors ->
        animateBorderStrokeAsState(
            isEmpty = selectedItem == null,
            enabled = enabled,
            isError = isError,
            interactionSource = interactionSource,
            colors = colors,
        )
    }
    val iconColor = borderColors?.borderColor(
        isEmpty = selectedItem == null,
        enabled = enabled,
        isError = isError,
        interactionSource = interactionSource,
    )?.value ?: contentColor
    val expandedStates = remember { MutableTransitionState(initialState = false) }
    val transition = updateTransition(transitionState = expandedStates, label = "SelectDropDown")
    val animatedIconRotation by animateIconRotation(transition = transition)
    val animatedAlpha by animateAlpha(
        isEmpty = selectedItem == null,
        transition = transition,
        expandedAlpha = ContentAlpha.high,
        emptyAlpha = ContentAlpha.medium,
        filledAlpha = ContentAlpha.high,
    )
    val contentAlpha = if (enabled) {
        animatedAlpha
    } else {
        ContentAlpha.disabled
    }
    val placeholderTextStyle = if (selectedItem == null) {
        textStyle
    } else {
        MaterialTheme.typography.caption
    }
    var dropDownWidth by remember { mutableStateOf(value = 0) }
    val dropDownWidthDp = with(LocalDensity.current) { dropDownWidth.toDp() }

    Surface(
        modifier = modifier
            .onPlaced { layoutCoordinates ->
                dropDownWidth = layoutCoordinates.size.width
            },
        onClick = {
            expandedStates.targetState = !expandedStates.currentState
            onExpand?.invoke()
        },
        shape = shape,
        color = backgroundColor,
        contentColor = contentColor,
//        border = borderStroke?.value,
        enabled = enabled,
        elevation = elevation,
        interactionSource = interactionSource,
    ) {
        CompositionLocalProvider(LocalContentAlpha provides contentAlpha) {
            Row(
                modifier = Modifier.padding(all = 21.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.weight(weight = 1f)) {
                    when (placeholderMode) {
                        PlaceholderMode.NoAnimation -> {
                            selectedItem?.run {
                                ProvideTextStyle(
                                    value = textStyle.copy(
                                        color = TextPrimary.copy(
                                            contentAlpha
                                        )
                                    )
                                ) { itemContent(this) }
                            } ?: ProvideTextStyle(
                                value = placeholderTextStyle.copy(
                                    color = LocalTextStyle.current.color.copy(
                                        contentAlpha
                                    )
                                )
                            ) {
                                placeholder()
                            }
                        }
                        PlaceholderMode.Animate -> {
                            ProvideTextStyle(value = placeholderTextStyle) {
                                placeholder()
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            selectedItem?.run {
                                ProvideTextStyle(value = textStyle.copy(color = TextPrimary)) {
                                    itemContent(
                                        this
                                    )
                                }
                            }
                        }
                    }

                }
                CompositionLocalProvider(LocalContentColor provides iconColor) {
                    icon(Modifier.rotate(degrees = animatedIconRotation))
                }
            }

            DropDown(
                modifier = Modifier
                    .defaultMinSize(minWidth = dropDownWidthDp),
                expanded = expandedStates.currentState,
                items = items,
                onItemClick = { index, item ->
                    expandedStates.targetState = false
                    onItemClick(index, item)
                },
                onDismiss = { expandedStates.targetState = false },
                dropDownHeader = dropDownHeader,
                itemContent = itemContent,
                selectedItem = selectedItem,
                onEndReached = onEndReached
            )
        }
    }
}

@Composable
private fun <T> DropDown(
    modifier: Modifier,
    expanded: Boolean,
    items: Array<T>,
    onItemClick: (index: Int, item: T) -> Unit,
    onDismiss: () -> Unit,
    dropDownHeader: (@Composable ColumnScope.() -> Unit)? = null,
    itemContent: @Composable (item: T) -> Unit,
    selectedItem: T?,
    onEndReached: ((item: T) -> Unit)? = null,
) {
    val scrollState = rememberScrollState()
    val scrollModifier = if (items.size > 3)
        Modifier
            .height(height = 200.dp)
            .drawVerticalScrollbar(state = scrollState)
    else
        Modifier

    MaterialTheme(
        colors = ColorPalette.copy(
            surface = ColorPalette.surface,
            onSurface = TextPrimary,
        ),
    ) {
        var dropDownMenuWidth by remember { mutableStateOf(1000) }
        DropdownMenu(
            modifier = modifier.onGloballyPositioned { layoutCoordinates ->
                dropDownMenuWidth = layoutCoordinates.size.width
            },
            expanded = expanded,
            onDismissRequest = onDismiss,
        ) {
            dropDownHeader?.invoke(this)
            val dropDownMenuWidthDp = with(LocalDensity.current) {
                dropDownMenuWidth.toDp()
            }
            var dropDownMenuHeightDp = 0.dp
            items.forEach {
//                dropDownMenuHeightDp += 48.dp
                dropDownMenuHeightDp += 73.dp
            }
            val lazyListScrollState = rememberLazyListState()
            Box(
                modifier = modifier
                    .width(dropDownMenuWidthDp)
                    .height(if (items.size > 3) 200.dp else dropDownMenuHeightDp)
                    .drawVerticalScrollbar(lazyListScrollState)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    state = lazyListScrollState
                ) {
                    items(items.size) { i ->
                        val item = items[i]
                        val backgroundColor =
                            if (selectedItem == item) MaterialTheme.colors.primaryVariant else Color.Transparent
                        val textColor =
                            if (selectedItem == item) contentColorFor(backgroundColor = backgroundColor) else TextPrimary
                        if (i >= items.size - 1 && onEndReached != null) {
                            onEndReached(items[i])
                        }
                        DropdownMenuItem(
                            contentPadding = PaddingValues(25.dp),
                            onClick = { onItemClick(i, item) },
                            modifier = modifier
                                .padding(horizontal = 20.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(color = backgroundColor)
                        ) {
                            ProvideTextStyle(value = MaterialTheme.typography.body2.copy(color = textColor)) {
                                itemContent(items[i])
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun animateIconRotation(transition: Transition<Boolean>): State<Float> {
    return transition.animateFloat(
        transitionSpec = {
            if (false isTransitioningTo true) {
                // Dismissed to expanded
                tween(
                    durationMillis = InTransitionDuration,
                    easing = LinearOutSlowInEasing,
                )
            } else {
                // Expanded to dismissed.
                tween(
                    durationMillis = OutTransitionDuration,
                )
            }
        },
        label = "SelectDropDownIconAnimation",
    ) { expanded ->
        if (expanded) {
            -180f
        } else {
            0f
        }
    }
}

@Composable
private fun animateAlpha(
    isEmpty: Boolean,
    transition: Transition<Boolean>,
    expandedAlpha: Float,
    emptyAlpha: Float,
    filledAlpha: Float,
): State<Float> {
    return transition.animateFloat(
        transitionSpec = {
            if (false isTransitioningTo true) {
                // Dismissed to expanded
                tween(
                    durationMillis = InTransitionDuration,
                    easing = LinearOutSlowInEasing,
                )
            } else {
                // Expanded to dismissed.
                tween(
                    durationMillis = OutTransitionDuration,
                )
            }
        },
        label = "SelectDropDownAlphaAnimation",
    ) { expanded ->
        if (expanded) {
            expandedAlpha
        } else if (isEmpty) {
            emptyAlpha
        } else {
            filledAlpha
        }
    }
}

private const val InTransitionDuration = 120
private const val OutTransitionDuration = 75

object SelectDropDownDefaults {

    val Shape = RoundedCornerShape(10.dp)
}

enum class PlaceholderMode {
    NoAnimation, Animate
}

@Preview(showBackground = true)
@Composable
fun SelectDropDownPreview() {
    CurrencyExchangerTheme {
        Box(modifier = Modifier.padding(all = 16.dp)) {
            var value by remember { mutableStateOf<String?>(value = null) }
            SelectDropDown(
                modifier = Modifier,
                selectedItem = value,
                placeholder = {
                    Text(text = "Select item")
                },
                onItemClick = { index, item ->
                    value = item
                },
                items = arrayOf("One", "Two", "Three"),
                icon = {
                    Icon(imageVector = Icons.Rounded.KeyboardArrowDown, contentDescription = null)
                },
            ) { item ->
                Text(text = item)
            }
        }
    }
}