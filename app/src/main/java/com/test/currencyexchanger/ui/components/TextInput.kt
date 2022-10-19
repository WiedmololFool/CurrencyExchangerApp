package com.test.currencyexchanger.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.test.currencyexchanger.ui.theme.CurrencyExchangerTheme
import com.test.currencyexchanger.ui.theme.TextPrimary
import com.test.currencyexchanger.ui.theme.TextSecondary
import com.test.currencyexchanger.ui.components.border.BorderColors
import com.test.currencyexchanger.ui.components.border.BorderDefaults
import com.test.currencyexchanger.ui.components.border.animateBorderStrokeAsState


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TextInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    singleLine: Boolean = false,
    readOnly: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    maxLength: Int = Int.MAX_VALUE,
    textStyle: TextStyle = MaterialTheme.typography.body1.copy(textAlign = TextAlign.End),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        textColor = TextPrimary,
        disabledTextColor = TextPrimary.copy(alpha = 0.5f),
        placeholderColor = MaterialTheme.colors.primaryVariant,
        disabledPlaceholderColor = MaterialTheme.colors.primaryVariant.copy(alpha = 0.5f),
        backgroundColor = MaterialTheme.colors.surface,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        focusedLabelColor = TextSecondary,
        unfocusedLabelColor = TextSecondary,
        disabledLabelColor = TextSecondary.copy(alpha = 0.5f),
    ),
    borderColors: BorderColors? = BorderDefaults.borderColors(),
    contentPadding: PaddingValues = PaddingValues(horizontal = 0.dp, vertical = 23.dp),
    minWidth: Dp = 140.dp,
) {
    val borderModifier = borderColors?.let { nonNullable ->
        Modifier.border(
            border = animateBorderStrokeAsState(
                isEmpty = value.isBlank(),
                enabled = enabled,
                isError = isError,
                interactionSource = interactionSource,
                colors = nonNullable,
            ).value,
            shape = shape,
        )
    } ?: Modifier

    val labelTextStyle = textStyle.copy(
        color = colors.labelColor(
            enabled = enabled,
            error = isError,
            interactionSource = interactionSource
        ).value
    )

    val mergedTextStyle = textStyle.copy(
        color = colors.textColor(enabled = enabled).value
    )

    BasicTextField(
        value = value.ofMaxLength(maxLength = maxLength),
        modifier = modifier
            .background(colors.backgroundColor(enabled).value, shape)
            .indicatorLine(enabled, isError, interactionSource, colors)
            .width(minWidth),
        onValueChange = { newValue -> onValueChange(newValue.ofMaxLength(maxLength = maxLength)) },
        enabled = enabled,
        readOnly = readOnly,
        textStyle = mergedTextStyle,
        cursorBrush = SolidColor(colors.cursorColor(isError).value),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.TextFieldDecorationBox(
                value = value,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                placeholder = placeholder,
                label = if (label != null) {
                    {
                        ProvideTextStyle(value = labelTextStyle) {
                            label.invoke()
                        }
                    }
                } else {
                    null
                },
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                singleLine = singleLine,
                enabled = enabled,
                isError = isError,
                interactionSource = interactionSource,
                colors = colors,
                contentPadding = contentPadding
            )
        }
    )
}

@Composable
fun TextInput(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    singleLine: Boolean = false,
    readOnly: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    maxLength: Int = Int.MAX_VALUE,
    textStyle: TextStyle = MaterialTheme.typography.body2,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        placeholderColor = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
        backgroundColor = MaterialTheme.colors.background,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
    ),
    borderColors: BorderColors? = BorderDefaults.borderColors(),
) {
    val borderModifier = borderColors?.let { nonNullable ->
        Modifier.border(
            border = animateBorderStrokeAsState(
                isEmpty = value.text.isBlank(),
                enabled = enabled,
                isError = isError,
                interactionSource = interactionSource,
                colors = nonNullable,
            ).value,
            shape = shape,
        )
    } ?: Modifier

    TextField(
        modifier = modifier.then(other = borderModifier),
        value = value.ofMaxLength(maxLength = maxLength),
        onValueChange = { newValue -> onValueChange(newValue.ofMaxLength(maxLength = maxLength)) },
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
    )
}

private fun String.ofMaxLength(maxLength: Int): String {
    val overLength = this.length - maxLength
    return if (overLength > 0) {
        take(maxLength)
    } else {
        this
    }
}

private fun TextFieldValue.ofMaxLength(maxLength: Int): TextFieldValue {
    val overLength = text.length - maxLength
    return if (overLength > 0) {
        val headIndex = selection.end - overLength
        val trailIndex = selection.end
        // Under normal conditions, headIndex >= 0
        if (headIndex >= 0) {
            copy(
                text = text.substring(0, headIndex) + text.substring(trailIndex, text.length),
                selection = TextRange(headIndex)
            )
        } else {
            // exceptional
            copy(text.take(maxLength), selection = TextRange(maxLength))
        }
    } else {
        this
    }
}

@Preview(showBackground = true)
@Composable
fun TextInputPreview() {
    CurrencyExchangerTheme {
        Column(modifier = Modifier.padding(all = 16.dp)) {
            TextInput(
                value = TextFieldValue(),
                onValueChange = { },
            )
            Spacer(modifier = Modifier.height(height = 8.dp))
            TextInput(
                value = TextFieldValue(),
                onValueChange = { },
                placeholder = {
                    Text(text = "Placeholder")
                },
                enabled = false
            )
            Spacer(modifier = Modifier.height(height = 8.dp))
            TextInput(
                value = TextFieldValue(),
                onValueChange = { },
                label = {
                    Text(text = "Label")
                }
            )
            Spacer(modifier = Modifier.height(height = 8.dp))
            TextInput(
                value = TextFieldValue(text = "Lorem ipsum"),
                onValueChange = { },
                label = {
                    Text(text = "Label")
                }
            )
            Spacer(modifier = Modifier.height(height = 8.dp))
            TextInput(
                value = TextFieldValue(text = "Lorem ipsum"),
                onValueChange = { },
                label = {
                    Text(text = "Label")
                },
                enabled = false,
            )
            Spacer(modifier = Modifier.height(height = 8.dp))
            TextInput(
                value = "Hello",
                onValueChange = { },
                label = {
                    Text(text = "Label")
                },
                enabled = false,
            )
            Spacer(modifier = Modifier.height(height = 8.dp))
            TextInput(
                value = TextFieldValue(text = "Lorem ipsum"),
                maxLength = 4,
                onValueChange = { },
                label = {
                    Text(text = "Label")
                },
                isError = true,
            )
        }
    }
}