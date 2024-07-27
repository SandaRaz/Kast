package com.ssw.kast.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.ui.theme.KastTheme

@Composable
fun AppNavigation(
    navController: NavHostController,
    selectedItem: SelectedItemManagement,
    bottomNavigationBar: @Composable () -> Unit
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, selectedItem, bottomNavigationBar)
        }
        composable("search") {
            SearchScreen(navController, selectedItem, bottomNavigationBar)
        }
        composable("menu") {
            MenuScreen(navController, selectedItem, bottomNavigationBar)
        }
        composable("content_details") {
            ContentDetails(navController, bottomNavigationBar)
        }
        composable("content_details2/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            // Screen(id)
        }
    }
}

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