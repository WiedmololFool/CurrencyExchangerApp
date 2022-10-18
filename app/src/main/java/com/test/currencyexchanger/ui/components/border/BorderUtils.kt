package com.trustthq.ui.design_system.border

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun animateBorderStrokeAsState(
    isEmpty: Boolean,
    enabled: Boolean,
    isError: Boolean,
    interactionSource: InteractionSource,
    colors: BorderColors = BorderDefaults.borderColors(),
    focusedBorderThickness: Dp = BorderDefaults.FocusedBorderThickness,
    unfocusedBorderThickness: Dp = BorderDefaults.UnfocusedBorderThickness,
): State<BorderStroke> {
    val focused by interactionSource.collectIsFocusedAsState()
    val indicatorColor = colors.borderColor(
        isEmpty = isEmpty,
        enabled = enabled,
        isError = isError,
        interactionSource = interactionSource,
    )
    val targetThickness = if (focused) focusedBorderThickness else unfocusedBorderThickness
    val animatedThickness = if (enabled) {
        animateDpAsState(
            targetValue = targetThickness,
            animationSpec = tween(durationMillis = AnimationDuration),
        )
    } else {
        rememberUpdatedState(newValue = unfocusedBorderThickness)
    }
    return rememberUpdatedState(
        BorderStroke(width = animatedThickness.value,
            brush = SolidColor(value = indicatorColor.value))
    )
}

object BorderDefaults {

    val UnfocusedBorderThickness = 1.dp
    val FocusedBorderThickness = 2.dp

    @Composable
    fun borderColors(
        emptyBorderColor: Color = MaterialTheme.colors.primary,
        focusedBorderColor: Color =
            MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high),
        unfocusedBorderColor: Color = focusedBorderColor,
        disabledBorderColor: Color = unfocusedBorderColor.copy(alpha = ContentAlpha.disabled),
        errorBorderColor: Color = MaterialTheme.colors.error,
    ): BorderColors = DefaultBorderColors(
        emptyBorderColor = emptyBorderColor,
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
        disabledBorderColor = disabledBorderColor,
        errorBorderColor = errorBorderColor,
    )
}

class DefaultBorderColors(
    private val emptyBorderColor: Color,
    private val focusedBorderColor: Color,
    private val unfocusedBorderColor: Color,
    private val disabledBorderColor: Color,
    private val errorBorderColor: Color,
) : BorderColors {

    @Composable
    override fun borderColor(
        isEmpty: Boolean,
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource,
    ): State<Color> {
        val focused by interactionSource.collectIsFocusedAsState()

        val targetValue = when {
            !enabled -> disabledBorderColor
            isError -> errorBorderColor
            focused -> focusedBorderColor
            isEmpty -> emptyBorderColor
            else -> unfocusedBorderColor
        }
        return if (enabled) {
            animateColorAsState(targetValue, tween(durationMillis = AnimationDuration))
        } else {
            rememberUpdatedState(targetValue)
        }
    }
}

interface BorderColors {

    @Composable
    fun borderColor(
        isEmpty: Boolean,
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource,
    ): State<Color>
}

private const val AnimationDuration = 150