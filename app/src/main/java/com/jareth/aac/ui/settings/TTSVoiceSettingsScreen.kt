package com.jareth.aac.ui.settings

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TTSVoiceSettingsScreen(tts: TextToSpeech) {
    val context = LocalContext.current

    var voices by remember { mutableStateOf<List<Voice>>(emptyList()) }
    var selectedVoice by remember { mutableStateOf<Voice?>(null) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        voices = tts.voices
            .filter { it.locale.language == "en" }
            .sortedBy { it.name }

        val prefs = context.getSharedPreferences("tts_prefs", Context.MODE_PRIVATE)
        val savedVoice = prefs.getString("selected_voice", null)
        selectedVoice = voices.find { it.name == savedVoice } ?: voices.firstOrNull()
    }

    Column(Modifier.padding(top = 50.dp, start = 16.dp, end = 16.dp)) {
        Text("Select TTS Voice", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(12.dp))

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
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                voices.forEach { voice ->
                    DropdownMenuItem(
                        text = { Text(voice.name) },
                        onClick = {
                            selectedVoice = voice
                            expanded = false

                            tts.voice = voice
                            context.getSharedPreferences("tts_prefs", Context.MODE_PRIVATE)
                                .edit().putString("selected_voice", voice.name).apply()
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            tts.speak("Hello! This is my voice.", TextToSpeech.QUEUE_FLUSH, null, null)
        }) {
            Text("Test Voice")
        }
    }
}
