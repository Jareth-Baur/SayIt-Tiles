package com.jareth.aac.ui.settings

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TTSSettingsScreen(tts: TextToSpeech) {
    val context = LocalContext.current
    var availableVoices by remember { mutableStateOf<List<Voice>>(emptyList()) }
    var selectedVoice by remember { mutableStateOf<Voice?>(null) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        availableVoices = tts.voices.filter {
            it.locale.language == "en" // or customize this
        }.sortedBy { it.name }

        val prefs = context.getSharedPreferences("tts_prefs", Context.MODE_PRIVATE)
        val savedVoiceName = prefs.getString("selected_voice", null)
        selectedVoice = availableVoices.find { it.name == savedVoiceName } ?: availableVoices.firstOrNull()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Select Voice",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedVoice?.name ?: "Choose a voice",
                onValueChange = {},
                label = { Text("Voice") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                availableVoices.forEach { voice ->
                    DropdownMenuItem(
                        text = { Text(voice.name) },
                        onClick = {
                            selectedVoice = voice
                            expanded = false

                            // Save to SharedPreferences
                            context.getSharedPreferences("tts_prefs", Context.MODE_PRIVATE)
                                .edit().putString("selected_voice", voice.name).apply()

                            tts.voice = voice
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                tts.speak("Hello! This is how I sound.", TextToSpeech.QUEUE_FLUSH, null, "sampleVoice")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Test Voice")
        }
    }
}
