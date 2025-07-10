package com.jareth.aac

import android.speech.tts.TextToSpeech
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.platform.LocalContext
import com.jareth.aac.data.AppDatabase
import com.jareth.aac.ui.*
import com.jareth.aac.ui.settings.SettingsScreen
import com.jareth.aac.ui.settings.TTSSettingsScreen
import com.jareth.aac.ui.settings.TTSVoiceSettingsScreen

@Composable
fun MainNavigation(navController: NavHostController, tts: TextToSpeech) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val tileDao = db.tileDao()
    val categoryDao = db.categoryDao()

    NavHost(navController, startDestination = BottomNavItem.Home.route) {
        composable(BottomNavItem.Home.route) {
            HomeScreen(tts)
        }

        composable(BottomNavItem.AddTile.route) {
            AddTileScreen(
                tileDao = tileDao,
                categoryDao = categoryDao,
                onTileAdded = {
                    navController.popBackStack()
                }
            )
        }

        composable("addTile") {
            AddTileScreen(
                tileDao = tileDao,
                categoryDao = categoryDao,
                onTileAdded = {
                    navController.popBackStack()
                }
            )
        }

        composable(BottomNavItem.Categories.route) {
            CategoriesScreen(
                categoryDao = categoryDao,
                onAddCategoryClicked = { navController.navigate("addCategory") }
            )
        }

        composable("addCategory") {
            AddCategoryScreen(
                categoryDao = categoryDao,
                navController = navController
            )
        }

        // ✅ Main settings screen
        composable(BottomNavItem.Settings.route) {
            SettingsScreen(tts = tts, navController = navController)
        }

        // ✅ TTS Voice picker screen
        composable("tts_voice_settings") {
            TTSVoiceSettingsScreen(tts = tts)
        }
    }
}
