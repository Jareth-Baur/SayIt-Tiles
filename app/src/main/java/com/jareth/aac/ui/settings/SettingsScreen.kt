package com.jareth.aac.ui.settings

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.ChevronRight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(tts: TextToSpeech, navController: NavController) {
    val context = LocalContext.current
    var selectedVoiceName by remember { mutableStateOf<String?>(null) }

    // Load saved voice name
    LaunchedEffect(Unit) {
        val prefs = context.getSharedPreferences("tts_prefs", Context.MODE_PRIVATE)
        selectedVoiceName = prefs.getString("selected_voice", "Default")
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("General", style = MaterialTheme.typography.titleMedium)

            SettingsItem(
                title = "Text-to-Speech Voice",
                subtitle = selectedVoiceName ?: "Default",
                onClick = { navController.navigate("tts_voice_settings") }
            )

            // You can add more settings here:
            SettingsItem(
                title = "About App",
                subtitle = "Version 1.0.0",
                onClick = { /* navigate to about screen later */ }
            )
        }
    }
}

@Composable
fun SettingsItem(title: String, subtitle: String?, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            subtitle?.let {
                Text(it, style = MaterialTheme.typography.bodySmall)
            }
        }
        Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null)
    }
}
