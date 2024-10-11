package com.ssw.kast.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ssw.kast.R
import com.ssw.kast.model.component.PickerElement
import com.ssw.kast.model.entity.Playlist
import com.ssw.kast.model.entity.Song
import com.ssw.kast.model.global.getCachedImageFromResources
import com.ssw.kast.model.manager.AccountManager
import com.ssw.kast.model.manager.SongManager
import com.ssw.kast.ui.theme.Darker
import com.ssw.kast.ui.theme.KastTheme
import com.ssw.kast.ui.theme.montserrat
import com.ssw.kast.viewmodel.PlaylistViewModel
import com.ssw.kast.viewmodel.SignInViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddToPlaylistPopup (
    modifier: Modifier = Modifier,
    isShowed: MutableState<Boolean>,
    song: MutableState<Song?>,
    userId: Any,
    playlistViewModel: PlaylistViewModel,
    onCreate: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    val backgroundColor = Color.Black.copy(alpha = 0.95f)
    val cornerShape = RoundedCornerShape(12.dp)

    LaunchedEffect(Unit) {
        playlistViewModel.loadPlaylistPickers(userId)
    }
    val playlistPickers = playlistViewModel.playlistPickers

    var pickedPlaylist by remember { mutableStateOf(PickerElement("",null)) }
    var error by remember { mutableStateOf("") }
    var succes by remember { mutableStateOf("") }

    if (isShowed.value) {
        Popup (
            alignment = Alignment.Center,
            onDismissRequest = {
                //isShowed.value = false
            },
            properties  = PopupProperties(
                focusable = true
            )
        ) {
            Box (
                modifier = modifier
                    .fillMaxWidth(0.95f)
                    .shadow(
                        elevation = 12.dp,
                        shape = cornerShape,
                        spotColor = Color.White.copy(alpha = 0.4f),
                        ambientColor = Color.White
                    )
                    .border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = cornerShape
                    )
                    .background(
                        color = backgroundColor,
                        shape = cornerShape
                    )
                    .padding(16.dp)
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Color.Transparent
                        )
                        .background(
                            color = Color.Transparent
                        )
                        .zIndex(10f),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close button",
                        modifier = Modifier
                            .clickable {
                                error = ""
                                succes = ""
                                isShowed.value = false
                            }
                    )
                }

                Column (
                    modifier = Modifier
                        .border(
                            1.dp,
                            Color.Transparent
                        ),
                    verticalArrangement = Arrangement.Center
                ) {
                    song.value?.let {
                        Text (
                            text = "Add ${it.title} - ${it.singer} to a playlist",
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    PopupDropDownList(
                        label = "Select playlist",
                        selected = pickedPlaylist,
                        onSelectItem = {
                            pickedPlaylist = it
                        },
                        items = playlistPickers,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SignButton (
                        label = "Add",
                        onClick = {
                            error = ""
                            succes = ""
                            scope.launch {
                                val playlistId = pickedPlaylist.value
                                if (playlistId != null && song.value != null) {
                                    playlistViewModel.addSongToPlaylist(playlistId, song.value?.id!!)

                                    if (playlistViewModel.playlistError.value.code != 0) {
                                        error = playlistViewModel.playlistError.value.error
                                    } else {
                                        succes = "Added successfully"
                                        onCreate()
                                    }
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (error.isNotBlank()) {
                        Text (
                            text = error,
                            color = Color.Red
                        )
                    }
                    if (succes.isNotBlank()) {
                        Text (
                            text = succes,
                            color = Color.Green
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun CreateNewPlaylistPopup (
    modifier: Modifier = Modifier,
    isShowed: MutableState<Boolean>,
    creatorId: Any,
    countPlaylist: MutableIntState,
    playlistViewModel: PlaylistViewModel,
    onCreate: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    val backgroundColor = Color.Black.copy(alpha = 0.95f)
    val cornerShape = RoundedCornerShape(12.dp)

    var playlistName by remember { mutableStateOf("Playlist#${countPlaylist.intValue+1}") }
    var error by remember { mutableStateOf("") }

    if (isShowed.value) {
        val keyboardController = LocalSoftwareKeyboardController.current

        Popup (
            alignment = Alignment.Center,
            onDismissRequest = {
                keyboardController?.hide()
                //playlistName = "Playlist#${countPlaylist.intValue+1}"
                //isShowed.value = false

            },
            properties  = PopupProperties(
                focusable = true
            )
        ) {
            Box (
                modifier = modifier
                    .fillMaxWidth(0.95f)
                    .shadow(
                        elevation = 12.dp,
                        shape = cornerShape,
                        spotColor = Color.White.copy(alpha = 0.4f),
                        ambientColor = Color.White
                    )
                    .border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = cornerShape
                    )
                    .background(
                        color = backgroundColor,
                        shape = cornerShape
                    )
                    .padding(16.dp)
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Color.Transparent
                        )
                        .background(
                            color = Color.Transparent
                        )
                        .zIndex(10f),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close button",
                        modifier = Modifier
                            .clickable {
                                playlistName = "Playlist#${countPlaylist.intValue+1}"
                                isShowed.value = false
                            }
                    )
                }

                Column (
                    modifier = Modifier
                        .border(
                            1.dp,
                            Color.Transparent
                        ),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text (
                        text = "New playlist name",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = playlistName,
                        onValueChange = { newValue ->
                            playlistName = newValue
                            error = ""
                        },
                        placeholder = {
                            Text (
                                text = "Playlist name",
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        },
                        trailingIcon = {
                            if (playlistName.isNotBlank()) {
                                Icon(
                                    imageVector = Icons.Rounded.Cancel,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .clickable {
                                            playlistName = ""
                                        }
                                )
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.tertiary,
                            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SignButton (
                        label = "Create",
                        onClick = {
                            scope.launch {
                                error = ""
                                val newPlaylist = playlistViewModel.createPlaylist(playlistName, creatorId)
                                if (playlistViewModel.playlistCreationError.value.code != 0) {
                                    error = playlistViewModel.playlistCreationError.value.error
                                } else {
                                    newPlaylist?.let {
                                        playlistViewModel.playlists.add(newPlaylist)
                                        playlistViewModel.playlistPickers.add(PickerElement(newPlaylist.name,
                                            newPlaylist.id))

                                        countPlaylist.intValue = playlistViewModel.playlists.size
                                        playlistName = "Playlist#${countPlaylist.intValue+1}"

                                        isShowed.value = false
                                    }
                                }
                                onCreate()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (error.isNotBlank()) {
                        Text (
                            text = error,
                            color = Color.Red
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun ImagePopup (
    modifier: Modifier = Modifier,
    isShowed: MutableState<Boolean>,
    image: ImageBitmap
) {

    val backgroundColor = Color.Black.copy(alpha = 0.95f)

    if (isShowed.value) {
        Popup (
            alignment = Alignment.Center,
            onDismissRequest = {
                isShowed.value = false
            }
        ) {
            Box (
                modifier = modifier
                    .fillMaxSize()
                    .border(
                        1.dp,
                        Color.Transparent
                    )
                    .background(
                        color = backgroundColor
                    )
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Color.Transparent
                        )
                        .background(
                            color = backgroundColor
                        )
                        .zIndex(10f)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close button",
                        modifier = Modifier
                            .clickable {
                                isShowed.value = false
                            }
                    )
                }

                Image (
                    bitmap = image,
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterStart)
                )
            }
        }
    }
}

@Composable
fun RenamePlaylistPopup (
    modifier: Modifier = Modifier,
    isShowed: MutableState<Boolean>,
    userId: Any,
    playlistId: Any,
    playlistName: MutableState<String>,
    playlistViewModel: PlaylistViewModel,
    onRenameSuccess: (newName: String) -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    val backgroundColor = Color.Black.copy(alpha = 0.95f)
    val cornerShape = RoundedCornerShape(12.dp)

    if (isShowed.value) {
        val keyboardController = LocalSoftwareKeyboardController.current

        var error by remember { mutableStateOf("") }
        var newName by remember { mutableStateOf(playlistName.value) }

        Popup (
            alignment = Alignment.Center,
            onDismissRequest = {
                //isShowed.value = false
                keyboardController?.hide()
            },
            properties  = PopupProperties(
                focusable = true
            )
        ) {
            Box (
                modifier = modifier
                    .fillMaxWidth(0.95f)
                    .shadow(
                        elevation = 12.dp,
                        shape = cornerShape,
                        spotColor = Color.White.copy(alpha = 0.4f),
                        ambientColor = Color.White
                    )
                    .border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = cornerShape
                    )
                    .background(
                        color = backgroundColor,
                        shape = cornerShape
                    )
                    .padding(16.dp)
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Color.Transparent
                        )
                        .background(
                            color = Color.Transparent
                        )
                        .zIndex(10f),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close button",
                        modifier = Modifier
                            .clickable {
                                isShowed.value = false
                            }
                    )
                }

                Column (
                    modifier = Modifier
                        .border(
                            1.dp,
                            Color.Transparent
                        ),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text (
                        text = "Playlist new name",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newValue ->
                            newName = newValue
                            error = ""
                        },
                        placeholder = {
                            Text (
                                text = "Playlist name",
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        },
                        trailingIcon = {
                            if (playlistName.value.isNotBlank()) {
                                Icon(
                                    imageVector = Icons.Rounded.Cancel,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .clickable {
                                            newName = ""
                                        }
                                )
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.tertiary,
                            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SignButton (
                        label = "Rename",
                        onClick = {
                            scope.launch {
                                error = ""
                                playlistViewModel.renamePlaylist(userId, playlistId, newName)
                                if (playlistViewModel.playlistError.value.code != 0) {
                                    error = playlistViewModel.playlistError.value.error
                                } else {
                                    playlistName.value = newName
                                    isShowed.value = false

                                    onRenameSuccess(newName)
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (error.isNotBlank()) {
                        Text (
                            text = error,
                            color = Color.Red
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun SignOutPopup(
    modifier: Modifier = Modifier,
    isShowed: MutableState<Boolean>,
    title: String,
    navController: NavHostController,
    accountManager: AccountManager,
    songManager: SongManager,
    signInViewModel: SignInViewModel = hiltViewModel()
) {
    val cornerShape = RoundedCornerShape(12.dp)

    val scope = rememberCoroutineScope()

    if (isShowed.value) {
        Popup (
            alignment = Alignment.Center,
            onDismissRequest = {
                isShowed.value = false
            }
        ) {
            Column (
                modifier = modifier
                    .fillMaxWidth(0.8f)
                    .height(180.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = cornerShape,
                        spotColor = Color.White.copy(alpha = 0.4f),
                        ambientColor = Color.White
                    )
                    .border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = cornerShape
                    )
                    .background(
                        color = Color.Black.copy(alpha = 0.95f),
                        shape = cornerShape
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text (
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.tertiary
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                Row (
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlineSignButton (
                        label = "Yes",
                        labelColor = MaterialTheme.colorScheme.tertiary,
                        lineColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
                        onClick = {
                            scope.launch {
                                signInViewModel.signOut(accountManager, songManager)
                                navController.navigate("sign_in")
                            }

                            isShowed.value = false
                        }
                    )

                    OutlineSignButton (
                        label = "No",
                        onClick = {
                            isShowed.value = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StartupPopUp(
    isShowed: MutableState<Boolean>
) {
    var showLogo by remember { mutableStateOf(true) }
    var showTitle by remember { mutableStateOf(false) }

    val kastIconImage = getCachedImageFromResources(resourceId = R.drawable.kast_icon_1)

    LaunchedEffect(showLogo) {
        delay(4000)
        showLogo = false
        showTitle = true
    }

    if (isShowed.value) {
        Scaffold (
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
        ) { innerPadding ->
            Column (
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxSize()
                    .border(
                        1.dp,
                        Color.Transparent
                    )
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                Popup (
                    alignment = Alignment.Center,
                    onDismissRequest = {}
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = MaterialTheme.colorScheme.background
                            ),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (showLogo) {
                            //LogoHeader()
                            Image(
                                bitmap = kastIconImage,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(160.dp)
                            )
                        }

                        if (showTitle) {
                            Text (
                                text = "Audio Streaming App",
                                color = MaterialTheme.colorScheme.tertiary,
                                fontFamily = montserrat,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(48.dp))

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .width(50.dp)
                                    .aspectRatio(1f)
                            ) {
                                CircleLoader(
                                    isVisible = true,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun YesOrNoPopup(
    modifier: Modifier = Modifier,
    isShowed: MutableState<Boolean>,
    title: String,
    onClickYes: () -> Unit,
    onClickNo: () -> Unit = {},
    error: @Composable () -> Unit = {}
) {
    val cornerShape = RoundedCornerShape(12.dp)
    val backgroundColor = Color.Black.copy(alpha = 0.95f)

    if (isShowed.value) {
        Popup (
            alignment = Alignment.Center,
            onDismissRequest = {
                isShowed.value = false
            }
        ) {
            Column (
                modifier = modifier
                    .fillMaxWidth(0.8f)
                    .border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = cornerShape
                    )
                    .background(
                        color = backgroundColor,
                        shape = cornerShape
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text (
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.tertiary
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                Row (
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlineSignButton (
                        label = "Yes",
                        labelColor = MaterialTheme.colorScheme.tertiary,
                        lineColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
                        onClick = {
                            onClickYes()
                        }
                    )

                    OutlineSignButton (
                        label = "No",
                        onClick = {
                            onClickNo()
                            isShowed.value = false
                        }
                    )
                }

                error()
            }
        }
    }
}