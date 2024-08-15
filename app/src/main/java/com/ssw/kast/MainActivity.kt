package com.ssw.kast

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.jakewharton.threetenabp.AndroidThreeTen
import com.ssw.kast.model.persistence.PreferencesManager
import com.ssw.kast.screen.AppNavigation
import com.ssw.kast.screen.BottomNavigationBar
import com.ssw.kast.screen.SelectedItemManagement
import com.ssw.kast.ui.theme.KastTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        enableEdgeToEdge()
        setContent {
            KastTheme {
                val navController = rememberNavController()

                var selectedItemName by remember { mutableStateOf("home") }
                val selectedItem = SelectedItemManagement(selectedItemName)

                val bottomNavigationBar: @Composable () -> Unit = {
                    BottomNavigationBar(navController, selectedItem)
                }

                val preferencesManager = remember { PreferencesManager(this.applicationContext) }

                AppNavigation(navController, selectedItem, bottomNavigationBar, preferencesManager)
            }
        }
    }
}