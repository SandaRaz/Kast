package com.ssw.kast.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicVideo
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ssw.kast.R
import com.ssw.kast.component.BottomNavigationBar
import com.ssw.kast.component.SearchBar
import com.ssw.kast.component.SelectedItemManagement
import com.ssw.kast.model.component.SearchResultModel
import com.ssw.kast.model.getImageFromResources
import com.ssw.kast.ui.theme.Grey
import com.ssw.kast.ui.theme.KastTheme
import com.ssw.kast.ui.theme.LightGrey

@Composable
fun SearchScreen(
    navController: NavHostController,
    selectedItem: SelectedItemManagement,
    bottomNavigationBar: @Composable () -> Unit
) {
    Scaffold (
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background),
        bottomBar = {
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Transparent)
            ) {
                bottomNavigationBar()
            }
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState(), true, null, false)
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            var searchedValue by remember { mutableStateOf(TextFieldValue("")) }

            SearchBar(
                searchValue = searchedValue,
                onValueChange = { newValue ->
                    searchedValue = newValue
                },
                onPrevious = {
                    val previousBackStackEntry = navController.previousBackStackEntry
                    val previousRoute = previousBackStackEntry?.destination?.route
                    if(previousRoute != null){
                        selectedItem.onSelectItem(previousRoute)
                        navController.popBackStack()
                    }
                }
            )

            HorizontalDivider(thickness = 1.dp,color = MaterialTheme.colorScheme.primary)

            val image1: ImageBitmap = getImageFromResources(resourceId = R.drawable.ic_launcher_foreground)
            val image2: ImageBitmap = getImageFromResources(resourceId = R.drawable.ic_launcher_background)
            val image3: ImageBitmap = getImageFromResources(resourceId = R.drawable.kast_old)

            val musicResult: List<SearchResultModel> = listOf(
                SearchResultModel("", "song", "", image1, "Everybody knows", "John Legend"),
                SearchResultModel("", "song", "", image2, "Titre 1", "Singer 1"),
                SearchResultModel("", "song", "", image3, "Titre 2", "Singer 2"),
                SearchResultModel("", "song", "", image1, "Titre 3", "Singer 3"),
                SearchResultModel("", "song", "", image2, "Titre 4", "Singer 4")
            )

            val userResult: List<SearchResultModel> = listOf(
                SearchResultModel("", "user", "", image3, "Everybody knows", "John Legend"),
                SearchResultModel("", "user", "", image1, "abc25", ""),
                SearchResultModel("", "user", "", image2, "_vroom", ""),
            )

            SearchResultContainer(
                title = "Song results",
                searchResults = musicResult,
                resultTypeIcon = Icons.Filled.MusicVideo,
                navController = navController
            )

            SearchResultContainer(
                title = "User results",
                searchResults = userResult,
                resultTypeIcon = Icons.Filled.Person,
                navController = navController
            )
        }
    }
}

@Composable
fun SearchResult(
    model: SearchResultModel,
    resultTypeIcon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val cornerShape = 12.dp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(cornerShape)
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(cornerShape)
            )
            .clickable {
                onClick()
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val imageCornerShape = 8.dp
        Image(
            bitmap = model.image,
            contentDescription = null,
            modifier = Modifier
                .clip(RoundedCornerShape(imageCornerShape))
                .background(
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(imageCornerShape)
                )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .fillMaxHeight()
                .border(
                    width = 1.dp,
                    color = Color.Transparent
                ),
                verticalArrangement = Arrangement.Center
        ) {
            if(model.primaryLabel.isNotBlank()){
                Text(
                    text = model.primaryLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            if(model.secondLabel.isNotBlank()){
                Text(
                    text = model.secondLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Icon(
            imageVector = resultTypeIcon,
            contentDescription = null,
            tint = LightGrey,
            modifier = Modifier
                .size(25.dp)
        )
    }
}

@Composable
fun SearchResultContainer(
    title: String,
    searchResults: List<SearchResultModel> = listOf(),
    resultTypeIcon: ImageVector,
    navController: NavHostController
) {
    val cornerShape = 16.dp
    val resultHeight = 80.dp

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.4f),
                        Color.Black.copy(alpha = 0.25f),
                        Color.Black.copy(alpha = 0.25f),
                        Color.Black.copy(alpha = 0.25f),
                        Color.Black.copy(alpha = 0.4f),
                        Color.Black.copy(alpha = 0.6f)
                    )
                ),
                shape = RoundedCornerShape(cornerShape)
            )
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
        )

        if (searchResults.isEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(resultHeight)
                    .border(1.dp, Color.Transparent),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "- No result -",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Grey
                )
            }
        } else {
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp, max = 240.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(cornerShape)
                    ),
            ) {
                items(searchResults) { result ->
                    SearchResult(
                        model = result,
                        resultTypeIcon = resultTypeIcon,
                        modifier = Modifier
                            .height(resultHeight)
                            .padding(vertical = 6.dp),
                        onClick = {
//                    val navigationRoute = "${result.route}/{${result.id}}"
//                    navController.navigate(navigationRoute)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    KastTheme {
        val navController = rememberNavController()

        val selectedItemName by remember { mutableStateOf("search") }
        val selectedItem = SelectedItemManagement(selectedItemName)

        val bottomNavigationBar: @Composable () -> Unit = {
            BottomNavigationBar(navController, selectedItem)
        }

        SearchScreen(navController, selectedItem, bottomNavigationBar)
    }
}