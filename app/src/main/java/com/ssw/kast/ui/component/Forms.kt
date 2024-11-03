package com.ssw.kast.ui.component

import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.ssw.kast.model.component.PickerElement
import com.ssw.kast.ui.theme.Grey
import com.ssw.kast.ui.theme.KastTheme
import com.ssw.kast.ui.theme.LightGrey
import com.ssw.kast.ui.theme.notoSans
import java.util.Date
import java.util.Locale

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date(millis))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    modifier: Modifier = Modifier,
    onDateSelected: (String) -> Unit,
    label: String = "",
    shape: Shape = RoundedCornerShape(12.dp),
    withTopPadding: Boolean = false,
    topPadding: Dp = 16.dp,
    withBottomPadding: Boolean = false,
    bottomPadding: Dp = 16.dp
) {
    if (withTopPadding) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(topPadding)
        )
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            shape = shape,
            modifier = modifier
                .fillMaxWidth()
                .height(64.dp)
                .clickable {
                    showDatePicker = !showDatePicker
                }
        )

        if (showDatePicker) {
            Popup (
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.Center
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )

                    Button(
                        onClick = {
                            onDateSelected(selectedDate)
                            Log.d("DatePicker", "SELECTED DATE => {$selectedDate}")
                            showDatePicker = false
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = "Valid")
                    }
                }
            }
        }
    }

    if (withBottomPadding) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(bottomPadding)
        )
    }
}

@Composable
fun DropDownList(
    modifier: Modifier = Modifier,
    label: String = "",
    selected: PickerElement,
    onSelectItem: (PickerElement) -> Unit,
    items: List<PickerElement>,
    shape: Shape = RoundedCornerShape(12.dp),
    withTopPadding: Boolean = false,
    topPadding: Dp = 16.dp,
    withBottomPadding: Boolean = false,
    bottomPadding: Dp = 16.dp
) {
    val defaultBorderColor = Grey
    val expandedBorderColor = MaterialTheme.colorScheme.primary

    var isExpanded by remember { mutableStateOf(false) }
    var borderColor by remember { mutableStateOf(defaultBorderColor) }

    if (withTopPadding) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(topPadding)
        )
    }

    Box(
        modifier = Modifier
            .border(
                1.dp,
                Color.Transparent
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .border(
                        width = (1.5f).dp,
                        color = borderColor,
                        shape = shape
                    )
                    .clickable {
                        isExpanded = !isExpanded
                        borderColor = expandedBorderColor
                    }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (selected.value == null) {
                    Text(
                        text = label
                    )
                } else {
                    Text(
                        text = selected.label
                    )
                }

                if (isExpanded) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropUp,
                        contentDescription = null
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = {
                    isExpanded = false
                    borderColor = defaultBorderColor
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .border(2.dp, Color.Transparent)
                    .fillMaxWidth(0.75f)
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item.label,
                                fontFamily = notoSans
                            )
                        },
                        onClick = {
                            isExpanded = false
                            borderColor = defaultBorderColor
                            onSelectItem(item)
                        }
                    )
                }
            }
        }
    }

    if (withBottomPadding) {5*7
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(bottomPadding)
        )
    }
}

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    labelColor: Color = MaterialTheme.colorScheme.tertiary,
    outline: Boolean = false,
    roundedCorner: Boolean = true,
    shape: Dp = 12.dp,
    onClick: () -> Unit
) {
    val cornerShape = if (roundedCorner) RoundedCornerShape(shape) else RectangleShape

    var backgroundColor = containerColor
    var borderColor = containerColor
    if (outline) {
        backgroundColor = Color.Transparent
    } else {
        borderColor = Color.Transparent
    }

    Row (
        modifier = modifier
            .border(
                width = 2.dp,
                color = borderColor,
                shape = cornerShape
            )
            .background(
                color = backgroundColor,
                shape = cornerShape
            )
            .clickable {
                onClick()
            }
            .padding(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Button icon",
            tint = labelColor
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = label,
            color = labelColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


@Composable
fun ImagePickerSample(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleColor: Color = MaterialTheme.colorScheme.primary,
    imageUri: Uri? = null,
    onChooseImage: (Uri?) -> Unit = {},
    onRemoveImage: () -> Unit = {}
) {
    var imgUri by remember { mutableStateOf(imageUri) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imgUri = uri
        onChooseImage(imgUri)
    }

    val cornerShape = RoundedCornerShape(12.dp)

    Column (
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                Color.Transparent
            )
            .padding(16.dp)
    ) {
        title?.let {
            Text (
                text = "$it:",
                fontSize = 22.sp,
                fontWeight = FontWeight.Normal,
                textDecoration = TextDecoration.Underline,
                color = titleColor
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (imgUri == null) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = cornerShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = {
                            launcher.launch("image/*")
                        }
                    ) {
                        Text (
                            text = "Add",
                            fontFamily = notoSans
                        )
                    }
                } else {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = cornerShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = Color.Red
                        ),
                        onClick = {
                            imgUri = null
                            onRemoveImage()
                        }
                    ) {
                        Text (
                            text = "Remove",
                            fontFamily = notoSans
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier
                    .width(16.dp)
            )

            Column (
                modifier = Modifier
                    .width(200.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = cornerShape
                    )
            ) {
                imageUri?.let {
                    Image (
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(it)
                                .size(Size.ORIGINAL)
                                .build()
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(cornerShape),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

@Composable
fun InputError(
    error: String
) {
    Text (
        text = error,
        color = Color.Red,
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                Color.Transparent
            )
            .padding(bottom = 16.dp)
    )
}

@Composable
fun ItemPicker(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    label: String,
    pickedItems: SnapshotStateList<PickerElement>,
    listItems: List<PickerElement>,
    onAddItem: (PickerElement) -> Unit,
    onDeleteItem: (PickerElement) -> Unit,
    withTopPadding: Boolean = false,
    topPadding: Dp = 16.dp,
    withBottomPadding: Boolean = false,
    bottomPadding: Dp = 16.dp
) {
    val selectedItem by remember { mutableStateOf(PickerElement(label,null)) }

    if (withTopPadding) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(topPadding)
        )
    }

    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surface,
                shape = shape
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.1f),
                        Color.Black.copy(alpha = 0.2f),
                        Color.Black.copy(alpha = 0.4f),
                        Color.Black.copy(alpha = 0.6f)
                    )
                ),
                shape = shape
            )
            .padding(16.dp)
    ) {
        DropDownList(
            label = label,
            selected = selectedItem,
            onSelectItem = { dropDownListItem ->
                onAddItem(dropDownListItem)
            },
            items = listItems,
            withBottomPadding = true
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 144.dp, max = 384.dp)
                .border(
                    1.dp,
                    Color.Transparent,
                    shape = shape
                ),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(pickedItems) { item ->
                Row(
                    modifier = Modifier
                        .height(48.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.label,
                        fontFamily = notoSans,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .border(
                                1.dp,
                                Color.Transparent
                            )
                    )
                    Icon(
                        imageVector = Icons.Rounded.Cancel,
                        contentDescription = "Remove",
                        tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                onDeleteItem(item)
                            }
                    )
                }
            }
        }
    }

    if (withBottomPadding) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(bottomPadding)
        )
    }
}

@Composable
fun OutlineSignButton(
    modifier: Modifier = Modifier,
    label: String = "Button",
    labelColor: Color = MaterialTheme.colorScheme.primary,
    lineColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
    shape: Shape = RoundedCornerShape(12.dp),
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
            .border(
                width = 2.dp,
                color = lineColor,
                shape = shape
            )
    ){
        Text(
            text = label,
            color = labelColor,
            fontFamily = notoSans,
            fontSize = 18.sp
        )
    }
}

@Composable
fun PopupDropDownList(
    modifier: Modifier = Modifier,
    label: String = "",
    selected: PickerElement,
    onSelectItem: (PickerElement) -> Unit,
    items: List<PickerElement>,
    shape: Shape = RoundedCornerShape(12.dp),
    withTopPadding: Boolean = false,
    topPadding: Dp = 16.dp,
    withBottomPadding: Boolean = false,
    bottomPadding: Dp = 16.dp
) {
    val defaultBorderColor = Grey
    val expandedBorderColor = MaterialTheme.colorScheme.primary

    var isExpanded by remember { mutableStateOf(false) }
    var borderColor by remember { mutableStateOf(defaultBorderColor) }

    if (withTopPadding) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(topPadding)
        )
    }

    Box(
        modifier = Modifier
            .border(
                1.dp,
                Color.Transparent
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .border(
                        width = (1.5f).dp,
                        color = borderColor,
                        shape = shape
                    )
                    .clickable {
                        isExpanded = !isExpanded
                        borderColor = expandedBorderColor
                    }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (selected.value == null) {
                    Text(
                        text = label
                    )
                } else {
                    Text(
                        text = selected.label
                    )
                }

                if (isExpanded) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropUp,
                        contentDescription = null
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }

            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = {
                    isExpanded = false
                    borderColor = defaultBorderColor
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .border(
                        2.dp,
                        Color.Transparent
                    )
                    .background(
                        color = MaterialTheme.colorScheme.background
                    )
                    .padding(horizontal = 8.dp)
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text (
                        text = "Choose a playlist",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp
                    )

                    Icon (
                        imageVector = Icons.Outlined.Clear,
                        contentDescription = "Close list",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                isExpanded = false
                                borderColor = defaultBorderColor
                            }
                    )
                }

                items.forEach { item ->
                    Spacer(modifier = Modifier.height(16.dp))

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item.label,
                                fontFamily = notoSans,
                                fontSize = 18.sp
                            )
                        },
                        modifier = Modifier
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(8.dp),
                        onClick = {
                            isExpanded = false
                            borderColor = defaultBorderColor
                            onSelectItem(item)
                        }
                    )
                }
            }
        }
    }

    if (withBottomPadding) {5*7
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(bottomPadding)
        )
    }
}

@Composable
fun SignButton(
    modifier: Modifier = Modifier,
    label: String = "Button",
    shape: Shape = RoundedCornerShape(12.dp),
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.tertiary
        ),
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )
                ),
                shape = shape
            )
    ){
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = notoSans,
            fontSize = 18.sp
        )
    }
}

@Composable
fun SignTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    error: String? = null,
    label: String,
    icon: ImageVector,
    cornerShape: Dp = 12.dp,
    secured: Boolean = false,
    withTopPadding: Boolean = false,
    topPadding: Dp = 16.dp,
    withBottomPadding: Boolean = false,
    bottomPadding: Dp = 16.dp
) {
    var visiblePassword by remember { mutableStateOf(false) }

    val visibilityIcon = if (visiblePassword) Icons.Filled.Visibility
    else Icons.Filled.VisibilityOff

    if (withTopPadding) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(topPadding)
        )
    }

    Column (
        modifier = Modifier
            .border(
                1.dp,
                Color.Transparent
            )
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = label,
                    color = LightGrey
                )
            },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null)
            },
            trailingIcon = {
                if (secured) {
                    Icon(
                        imageVector = visibilityIcon,
                        contentDescription = null,
                        tint = LightGrey,
                        modifier = Modifier
                            .clickable{
                                visiblePassword = !visiblePassword
                            }
                    )
                }
            },
            visualTransformation = if (secured && !visiblePassword) PasswordVisualTransformation()
            else VisualTransformation.None,
            shape = RoundedCornerShape(cornerShape),
            modifier = modifier
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(cornerShape)
                ),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.tertiary,
                unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                focusedContainerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
            )
        )

        error?.let {
            if (it.isNotBlank()) {
                Text (
                    text = it,
                    color = Color.Red
                )
            }
        }
    }

    if (withBottomPadding) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(bottomPadding)
        )
    }
}

@Composable
fun TitleAndValue(
    modifier: Modifier = Modifier,
    title: String,
    titleColor: Color = Color.White,
    titleWeight: FontWeight = FontWeight.Medium,
    value: String,
    valueColor: Color = Color.White,
    valueWeight: FontWeight = FontWeight.Normal,
    textDecoration: TextDecoration? = null
) {
    Row (
        modifier = modifier
            .border(
                1.dp,
                Color.Transparent
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text (
            text = "$title:",
            color = titleColor,
            fontWeight = titleWeight,
            textDecoration = textDecoration
        )

        Spacer (
            modifier = Modifier
                .fillMaxHeight()
                .width(8.dp)
        )

        Text (
            text = value,
            color = valueColor,
            fontWeight = valueWeight
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FormsPreview() {
    KastTheme {
        Scaffold(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
        ) { innerPadding ->
            Column (
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState(), true, null, false)
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

            }
        }
    }
}