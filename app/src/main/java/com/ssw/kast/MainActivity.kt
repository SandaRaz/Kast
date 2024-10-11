package com.ssw.kast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.jakewharton.threetenabp.AndroidThreeTen
import com.ssw.kast.model.entity.MusicGenre
import com.ssw.kast.ui.component.BottomNavigationBar
import com.ssw.kast.ui.component.SelectedItemManagement
import com.ssw.kast.model.manager.SongManager
import com.ssw.kast.model.entity.Song
import com.ssw.kast.model.entity.User
import com.ssw.kast.model.manager.AccountManager
import com.ssw.kast.model.manager.AuthManager
import com.ssw.kast.model.persistence.AppDatabase
import com.ssw.kast.model.persistence.PreferencesManager
import com.ssw.kast.ui.component.StartupPopUp
import com.ssw.kast.ui.screen.AppNavigation
import com.ssw.kast.ui.theme.KastTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- before using the classes from the Threeten.bp library ---
        AndroidThreeTen.init(this)

        // --- init room database ---
//        db = Room.databaseBuilder(
//            this.applicationContext,
//            AppDatabase::class.java,
//            "app_database"
//        ).build()

        db = AppDatabase.getDatabase(this.applicationContext)

        enableEdgeToEdge()
        setContent {
            KastTheme {
                val navController = rememberNavController()

                var selectedItemName by remember { mutableStateOf("home") }
                val selectedItem = SelectedItemManagement(selectedItemName)

                val bottomNavigationBar: @Composable () -> Unit = {
                    BottomNavigationBar(navController, selectedItem)
                }
            // ----- Setup Data and features managers -----
                val preferencesManager = remember { PreferencesManager(this.applicationContext) }

                val currentSong by remember { mutableStateOf<Song?>(null) }
                val songManager = SongManager(
                    db,
                    this.applicationContext,
                    preferencesManager,
                    currentSong
                )

                val newUser by remember { mutableStateOf(User()) }
                val newUserMusicGenres by remember { mutableStateOf<List<MusicGenre>>(emptyList()) }
                val authManager = AuthManager(
                    newUser,
                    newUserMusicGenres
                )

                val currentUser by remember { mutableStateOf<User?>(null) }
                val accountManager = AccountManager(db,currentUser)
            // --------------------------------------------

                AppNavigation(
                    db,
                    navController,
                    selectedItem,
                    bottomNavigationBar,
                    preferencesManager,
                    songManager,
                    authManager,
                    accountManager
                )
            }
        }
    }

    companion object {
        fun restartMainActivity(context: Context) {
            val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            intent?.let {
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(it)
                Runtime.getRuntime().exit(0)
            }
        }

        fun exit() {
            Runtime.getRuntime().exit(0)
        }
    }
}