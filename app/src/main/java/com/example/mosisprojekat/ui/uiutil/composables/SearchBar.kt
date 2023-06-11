package com.example.mosisprojekat.ui.uiutil.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mosisprojekat.ui.theme.SearchBarPlaceholderTextColor
import com.example.mosisprojekat.ui.theme.fontRoboto

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    placeholderText: String
) {

    val focusManager = LocalFocusManager.current

    TextField(
        modifier = modifier
            .shadow(
                elevation = 7.dp,
                shape = MaterialTheme.shapes.small
            )
            .background(
                color = MaterialTheme.colors.surface,
                shape = MaterialTheme.shapes.small
            ),
        singleLine = true,
        textStyle = provideSearchBarTextStyle(MaterialTheme.colors.onSurface),
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = MaterialTheme.colors.onSurface,
            focusedIndicatorColor = MaterialTheme.colors.onSurface,

            ),
        value = searchText,
        onValueChange = { onSearchTextChange(it) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                tint = Color.White,
                contentDescription = "Search bar icon"
            )
        },
        placeholder = {
            Text(
                text = placeholderText,
                style = provideSearchBarTextStyle(SearchBarPlaceholderTextColor)
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { focusManager.clearFocus() }
        )
    )

}

private fun provideSearchBarTextStyle(color: Color) = TextStyle(
    color = color,
    fontSize = (14.4).sp,
    fontWeight = FontWeight.Normal,
    fontFamily = fontRoboto
)