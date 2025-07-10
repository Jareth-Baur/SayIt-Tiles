package com.jareth.aac

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.jareth.aac.data.AppDatabase
import com.jareth.aac.ui.theme.AACTheme
import java.util.*

class MainActivity : ComponentActivity() {

    // ✅ Declare TTS outside onCreate
    private lateinit var tts: TextToSpeech
    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "aac-database"
        ).build()

        // ✅ Initialize TTS
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val prefs = getSharedPreferences("tts_prefs", Context.MODE_PRIVATE)
                val savedVoiceName = prefs.getString("selected_voice", null)

                val voice = tts.voices.find { it.name == savedVoiceName }
                if (voice != null) {
                    tts.voice = voice
                }
            }
        }

        setContent {
            AACTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = { BottomBar(navController) }
                ) { innerPadding ->
                    // ✅ Use ONE navigation system and pass TTS if needed
                    MainNavigation(navController = navController, tts = tts)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.stop()
        tts.shutdown()
    }
}

// ✅ TTS Button
@Composable
fun CommunicationTile(text: String, tts: TextToSpeech, modifier: Modifier = Modifier) {
    Button(
        onClick = { tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null) },
        modifier = modifier
    ) {
        Text(text)
    }
}
