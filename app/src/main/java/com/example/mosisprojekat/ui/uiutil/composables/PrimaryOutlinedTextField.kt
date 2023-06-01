package com.example.mosisprojekat.ui.uiutil.composables

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.mosisprojekat.ui.theme.GreyLight
import com.example.mosisprojekat.ui.theme.MosisProjekatTheme

@Composable
fun PrimaryOutlinedTextField(
    modifier: Modifier = Modifier,
    textStateValue: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction = ImeAction.Next,
    onNext: () -> Unit = {},
    onDone: () -> Unit = {},
    trailingIconVector: ImageVector = Icons.Filled.Cancel,
    onTrailingIconClick: () -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
            //.focusRequester(usernameFocusRequester),
        singleLine = true,
        value = textStateValue,
        onValueChange = {  onValueChange(it) },
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.body1
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colors.onBackground,
            backgroundColor = MaterialTheme.colors.surface,
            unfocusedLabelColor = GreyLight,
            focusedLabelColor = GreyLight,
            cursorColor = MaterialTheme.colors.onBackground,
        ),
        textStyle = MaterialTheme.typography.body1.copy(
            textAlign = TextAlign.Start,
        ),
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = { onNext() },
            onDone = { onDone() }
        ),
        trailingIcon = {
            IconButton(
                onClick = { onTrailingIconClick() }
            ) {
                Icon(
                    imageVector = trailingIconVector,
                    contentDescription = "Cancel username input",
                    tint = GreyLight
                )
            }
        }
    )
}

@Preview
@Composable
private fun PrimaryOutlinedTextFieldPreview() {
    MosisProjekatTheme {
        PrimaryOutlinedTextField(
            textStateValue = "Value",
            onValueChange = {},
            label = "Label",
            isError = false,
            onTrailingIconClick = {}
        )
    }
}