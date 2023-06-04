package com.example.mosisprojekat.ui.uiutil.composables.bottomnav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mosisprojekat.ui.theme.MosisProjekatTheme
import com.example.mosisprojekat.ui.theme.spacing
import com.example.mosisprojekat.util.ComponentSizes

@Composable
fun BottomNavBar(
    currentRoute: String?,
    modifier: Modifier = Modifier,
    items: List<BottomNavItem>,
    bottomBarState: Boolean,
    onItemClick: (BottomNavItem) -> Unit
) {

    AnimatedVisibility(
        visible = bottomBarState,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {

        BottomNavigation(
            modifier = modifier
                .height(ComponentSizes.bottomNavBarHeight.dp)
                .clip(MaterialTheme.shapes.large),
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 5.dp
        ) {
            items.forEach { item ->

                val selected = item.route == currentRoute

                BottomNavigationItem(
                    selected = selected,
                    onClick = { onItemClick(item) },
                    selectedContentColor = MaterialTheme.colors.primary,
                    unselectedContentColor = MaterialTheme.colors.onSurface,
                    icon = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                modifier = Modifier
                                    .padding(vertical = MaterialTheme.spacing.extraSmall),
                                imageVector = item.icon,
                                contentDescription = item.name
                            )

                            if (selected) {
                                Text(
                                    text = item.name,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.caption
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}



@Preview
@Composable
private fun BottomNavBarPreview() {
    MosisProjekatTheme {
        BottomNavBar(
            currentRoute = "Menu",
            items = listOf(
                BottomNavItem(
                    name = "Home",
                    route = "Home",
                    icon = Icons.Filled.EmojiEvents
                ),
                BottomNavItem(
                    name = "Clients",
                    route = "Clients",
                    icon = Icons.Filled.Home
                ),
                BottomNavItem(
                    name = "Menu",
                    route = "Menu",
                    icon = Icons.Filled.Person
                )
            ),
            bottomBarState = true,
            onItemClick = {}
        )
    }
}