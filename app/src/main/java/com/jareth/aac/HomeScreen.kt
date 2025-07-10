package com.jareth.aac

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jareth.aac.data.AppDatabase
import com.jareth.aac.data.TileEntity
import com.jareth.aac.viewmodel.TileViewModel
import com.jareth.aac.viewmodel.TileViewModelFactory

@Composable
fun HomeScreen(tts: TextToSpeech) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val viewModel: TileViewModel = viewModel(
        factory = TileViewModelFactory(db.tileDao())
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    val tiles by viewModel.tiles.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var categories by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch categories from database on first launch
    LaunchedEffect(Unit) {
        isLoading = true
        val catList = db.categoryDao().getAllCategories().map { it.name }
        categories = catList
        if (catList.isNotEmpty()) {
            selectedCategory = catList.first()
            viewModel.loadTilesByCategory(catList.first())
        }
        isLoading = false
    }

    // Fetch tiles again when category changes
    LaunchedEffect(selectedCategory) {
        if (selectedCategory.isNotBlank()) {
            viewModel.loadTilesByCategory(selectedCategory)
        }
    }

    Scaffold { paddingValues ->
        if (isLoading) {
            // Loading Spinner
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Dropdown menu for category selection
                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Category: $selectedCategory ⬇️")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Show tiles
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    tiles.chunked(2).forEach { row ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            row.forEach { tile ->
                                TileItem(tile = tile, tts = tts , modifier = Modifier.weight(1f))
                            }
                            if (row.size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TileItem(tile: TileEntity, tts: TextToSpeech, modifier: Modifier = Modifier) {
    Button(
        onClick = {
            tts.speak(tile.label, TextToSpeech.QUEUE_FLUSH, null, null)
        },
        modifier = Modifier
            .height(80.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text("${tile.emoji} ${tile.label}", maxLines = 2)
    }
}
