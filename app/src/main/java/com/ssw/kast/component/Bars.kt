package com.ssw.kast.component

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.material.R
import com.ssw.kast.ui.theme.Darker
import com.ssw.kast.ui.theme.KastTheme
import com.ssw.kast.ui.theme.LightGrey

class SelectedItemManagement(var itemName: String) {
    var onSelectItem: (String) -> Unit = { item ->
        itemName = item
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, selectedItem: SelectedItemManagement) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
//                        Color.Transparent,
                        Color.Black.copy(alpha = 0.2f),
                        Color.Black.copy(alpha = 0.35f),
                        Color.Black.copy(alpha = 0.5f),
                        Color.Black.copy(alpha = 0.65f),
                        Color.Black.copy(alpha = 0.7f),
                        Color.Black.copy(alpha = 0.75f),
                        Color.Black.copy(alpha = 0.8f),
                        Color.Black.copy(alpha = 0.85f),
                        Color.Black.copy(alpha = 0.9f),
                        Color.Black
                    )
                )
            )
    ) {
        NavigationBar (
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            val selectedIconColor = Color.Black
            val selectedIndicatorColor = MaterialTheme.colorScheme.primary
            val unselectedIconColor = MaterialTheme.colorScheme.tertiary

            NavigationBarItem(
                selected = selectedItem.itemName == "home",
                onClick = {
                    selectedItem.onSelectItem("home")
                    navController.navigate("home")
                },
                icon = {
                    Icon(
                        Icons.Outlined.Home,
                        contentDescription = "Home",
                    )
                },
                label = null,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedIconColor,
                    unselectedIconColor = unselectedIconColor,
                    indicatorColor = selectedIndicatorColor
                ),
            )

            NavigationBarItem(
                selected = selectedItem.itemName == "search",
                onClick = {
                    selectedItem.onSelectItem("search")
                    navController.navigate("search")
                },
                icon = {
                    Icon(
                        Icons.Outlined.Search,
                        contentDescription = "Search",
                    )
                },
                label = null,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedIconColor,
                    unselectedIconColor = unselectedIconColor,
                    indicatorColor = selectedIndicatorColor
                ),
            )

            NavigationBarItem(
                selected = selectedItem.itemName == "music",
                onClick = {
                    selectedItem.onSelectItem("music")
//                    navController.navigate("music")
                },
                icon = {
                    Icon(
                        Icons.Outlined.MusicNote,
                        contentDescription = "Music",
                    )
                },
                label = null,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedIconColor,
                    unselectedIconColor = unselectedIconColor,
                    indicatorColor = selectedIndicatorColor
                ),
            )

            NavigationBarItem(
                selected = selectedItem.itemName == "menu",
                onClick = {
                    selectedItem.onSelectItem("menu")
                    navController.navigate("menu")
                },
                icon = {
                    Icon(
                        Icons.Outlined.Menu,
                        contentDescription = "Menu",
                    )
                },
                label = null,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedIconColor,
                    unselectedIconColor = unselectedIconColor,
                    indicatorColor = selectedIndicatorColor
                ),
            )
        }
    }
}

@Composable
fun Modifier.customSelectedIndicatorModifier(isSelected: Boolean): Modifier {
    return if (isSelected) {
        this.then(
            Modifier
                .padding(4.dp)
                .background(
                    color = Color.Red,
                    shape = CircleShape
                )
        )
    } else {
        this
    }
}

@SuppressLint("PrivateResource")
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onPrevious: () -> Unit = {},
    onSearch: () -> Unit = {}
) {
    val cornerShape: Dp = 16.dp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(cornerShape)
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(cornerShape)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = com.google.android.material.R.drawable.material_ic_keyboard_arrow_previous_black_24dp),
            contentDescription = "previous",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary),
            modifier = Modifier
                .size(44.dp)
                .clickable { onPrevious() }
                .padding(horizontal = 8.dp)
        )
        Row(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(topEnd = cornerShape, bottomEnd = cornerShape)
                )
                .background(
                    color = Color.White.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(topEnd = cornerShape, bottomEnd = cornerShape)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = searchValue,
                onValueChange = onValueChange,
                placeholder = {
                    Text("Search...")
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedTextColor = LightGrey,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    if(searchValue.text.isNotBlank()){
                        Icon(
                            imageVector = Icons.Rounded.Cancel,
                            contentDescription = null,
                            tint = Darker,
                            modifier = Modifier
                                .clickable {
                                    onValueChange(TextFieldValue(""))
                                }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .fillMaxHeight()
            )
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.Transparent,
                        shape = CircleShape
                    )
                    .size(44.dp)
                    .background(
                        color = Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable {
                        onSearch()
                    }
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@SuppressLint("PrivateResource")
@Composable
fun TitleBar(title: String, isNavigable: Boolean = false, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
        )
        if(isNavigable) {
            Image(
                painter = painterResource(id = R.drawable.material_ic_keyboard_arrow_next_black_24dp),
                contentDescription = "Advanced icon",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onClick() }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F17)
@Composable
fun BottomNavigationBarPreview() {
    KastTheme {
        val navController = rememberNavController()
        val selectedItemName by remember { mutableStateOf("bottom_bar") }
        val selectedItem = SelectedItemManagement(selectedItemName)
        BottomNavigationBar(navController = navController, selectedItem  = selectedItem)
    }
}