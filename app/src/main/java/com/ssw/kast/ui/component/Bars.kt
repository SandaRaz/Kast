package com.ssw.kast.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.R
import com.ssw.kast.model.global.getCachedImageFromResources
import com.ssw.kast.model.manager.SongManager
import com.ssw.kast.ui.screen.NavigationManager
import com.ssw.kast.ui.theme.Darker
import com.ssw.kast.ui.theme.KastTheme
import com.ssw.kast.ui.theme.LightGrey

class SelectedItemManagement {
    lateinit var itemName: String
    var onSelectItem: (String) -> Unit = { item ->
        itemName = item
    }

    constructor()

    constructor(itemName: String) {
        this.itemName = itemName
    }
}

@Composable
fun BackNavBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleSize: TextUnit = 22.sp,
    titleColor: Color = MaterialTheme.colorScheme.primary,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    underline: Boolean = false,
    underlineColor: Color = MaterialTheme.colorScheme.primary,
    onPressBackButton: () -> Unit = {},
    extraIcon: ImageVector? = null,
    extraIconColor: Color = MaterialTheme.colorScheme.tertiary,
    onClickExtraIcon: () -> Unit = {}
) {
    // ------------- Data --------------

    val horizontalArrangement = if (extraIcon == null) {
        Arrangement.Center
    } else {
        Arrangement.SpaceBetween
    }

    // ---------------------------------

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .border(
                1.dp,
                Color.Transparent
            )
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.ChevronLeft,
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .border(
                    1.dp,
                    Color.Transparent
                )
                .clickable {
                    onPressBackButton()
                },
            tint = iconColor
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    1.dp,
                    Color.Transparent
                ),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (extraIcon != null) {
                Spacer(modifier = Modifier)
            }

            Text(
                text = title ?: "",
                color = titleColor,
                fontSize = titleSize,
                fontWeight = FontWeight.SemiBold
            )

            extraIcon?.let {
                RotatingIcon(
                    imageVector = it,
                    tint = extraIconColor,
                    modifier = Modifier
                        .fillMaxHeight(),
                    onClick = {
                        onClickExtraIcon()
                    }
                )
            }
        }
    }

    if (underline) {
        HorizontalDivider(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            thickness = 2.dp,
            color = underlineColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BackNavBarPreview() {
    KastTheme {
        BackNavBar(title = "player")
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
                        Color.Black.copy(alpha = 0.4f),
                        Color.Black.copy(alpha = 0.5f),
                        Color.Black.copy(alpha = 0.6f),
                        Color.Black.copy(alpha = 0.75f),
                        Color.Black.copy(alpha = 0.8f),
                        Color.Black.copy(alpha = 0.85f),
                        Color.Black.copy(alpha = 0.9f),
                        Color.Black.copy(alpha = 0.95f),
                        Color.Black.copy(alpha = 1f),
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
                    NavigationManager.navigateTo(navController, "home")
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
                    NavigationManager.navigateTo(navController, "search")
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
                    NavigationManager.navigateTo(navController, "music")
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
                    NavigationManager.navigateTo(navController,"menu")
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CurrentSongBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    songManager: SongManager,
    onClickPlay: () -> Unit = {}
) {
    val songCover = if (songManager.currentSong?.cover != null) songManager.currentSong?.cover!!
        else getCachedImageFromResources(resourceId = R.drawable.default_cover)

    var isPlayed by remember { mutableStateOf(songManager.isPlayed) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .border(
                1.dp,
                Color.Transparent
            )
            .background(
                color = MaterialTheme.colorScheme.surface
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(
                    1.dp,
                    Color.Transparent
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                bitmap = songCover,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.FillBounds
            )

            Spacer(
                modifier = Modifier
                    .width(16.dp)
                    .fillMaxHeight()
                    .background(color = Color.Transparent)
            )

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(
                        1.dp,
                        Color.Transparent
                    ),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = songManager.currentSong?.title!!,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .basicMarquee(
                            delayMillis = 8000,
                            iterations = Int.MAX_VALUE
                        )
                )

                Text(
                    text = songManager.currentSong?.singer!!,
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .basicMarquee(
                            delayMillis = 8000,
                            iterations = Int.MAX_VALUE
                        )
                )
            }

            Spacer(
                modifier = Modifier
                    .width(16.dp)
                    .fillMaxHeight()
                    .background(color = Color.Transparent)
            )

            val playIcon: ImageVector = if (isPlayed) Icons.Filled.PauseCircle
                else Icons.Filled.PlayCircle

            Icon (
                imageVector = playIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(36.dp)
                    .clickable {
                        onClickPlay()
                        isPlayed = !isPlayed
                    }
            )
        }

        Spacer(
            modifier = Modifier
                .width(16.dp)
                .fillMaxHeight()
                .background(color = Color.Transparent)
        )

        Icon (
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(34.dp)
                .clickable {
                    NavigationManager.navigateTo(navController, "player")
                }
        )
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

@Composable
fun PlaylistNavBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleSize: TextUnit = 22.sp,
    titleColor: Color = MaterialTheme.colorScheme.primary,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    underline: Boolean = false,
    underlineColor: Color = MaterialTheme.colorScheme.primary,
    onPressBackButton: () -> Unit = {},
    extraIcon: ImageVector? = null,
    onClickExtraIcon:() -> Unit = {},
    onClickRefreshButton: () -> Unit = {}
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .border(
                1.dp,
                Color.Transparent
            )
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.ChevronLeft,
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .border(
                    1.dp,
                    Color.Transparent
                )
                .clickable {
                    onPressBackButton()
                },
            tint = iconColor
        )

        Text(
            text = title ?: "",
            color = titleColor,
            fontSize = titleSize,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Center)
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    1.dp,
                    Color.Transparent
                ),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RotatingIcon(
                imageVector = Icons.Outlined.Refresh,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .fillMaxHeight(),
                onClick = {
                    onClickRefreshButton()
                }
            )

            extraIcon?.let {
                Spacer(
                    modifier = Modifier.width(16.dp)
                )

                Icon (
                    imageVector = extraIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxHeight()
                        .clickable {
                            onClickExtraIcon()
                        }
                )
            }
        }
    }

    if (underline) {
        HorizontalDivider(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            thickness = 2.dp,
            color = underlineColor
        )
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
        Icon(
            imageVector = Icons.Outlined.ChevronLeft,
            contentDescription = "previous",
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier
                .clickable {
                    onPrevious()
                }
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
                            tint = Color.Black,
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
                contentDescription = "Search button",
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
fun TitleBar(
    title: String,
    titleColor: Color = MaterialTheme.colorScheme.tertiary,
    fontSize: TextUnit = 20.sp,
    extraIcon: ImageVector? = null,
    extraIconColor: Color = MaterialTheme.colorScheme.tertiary,
    onClickExtraIcon: () -> Unit = {},
    isNavigable: Boolean = false,
    onNavigationClick: () -> Unit = {}
) {
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
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold,
            color = titleColor,
        )

        Row (
            modifier = Modifier
                .fillMaxHeight()
                .border(
                    1.dp,
                    Color.Transparent
                )
        ) {
            extraIcon?.let {
                RotatingIcon(
                    imageVector = it,
                    tint = extraIconColor,
                    modifier = Modifier
                        .size(30.dp),
                    onClick = {
                        onClickExtraIcon()
                    }
                )
            }

            if(isNavigable) {
                Spacer(
                    modifier = Modifier
                        .width(16.dp)
                )

                Icon (
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = "Advanced icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            onNavigationClick()
                        }
                )
            }
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