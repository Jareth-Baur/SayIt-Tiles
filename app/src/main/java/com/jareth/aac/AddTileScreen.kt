package com.jareth.aac

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jareth.aac.data.CategoryDao
import com.jareth.aac.data.TileDao
import com.jareth.aac.data.TileEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTileScreen(
    tileDao: TileDao,
    categoryDao: CategoryDao, // ✅ Add this
    onTileAdded: () -> Unit

) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var emoji by remember { mutableStateOf("") }
    var label by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    var categories by remember { mutableStateOf<List<String>>(emptyList()) }

    // ✅ Load categories from DB
    LaunchedEffect(Unit) {
        categories = categoryDao.getAllCategories().map { it.name }
        if (categories.isNotEmpty()) {
            selectedCategory = categories.first()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding() + 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = emoji,
            onValueChange = { emoji = it },
            label = { Text("Emoji") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = label,
            onValueChange = { label = it },
            label = { Text("Label") },
            modifier = Modifier.fillMaxWidth()
        )

        // 🔽 Category Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
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

        Button(
            onClick = {
                if (emoji.isNotBlank() && label.isNotBlank() && selectedCategory.isNotBlank()) {
                    val tile = TileEntity(
                        emoji = emoji.trim(),
                        label = label.trim(),
                        category = selectedCategory.trim()
                    )
                    coroutineScope.launch {
                        tileDao.insert(tile)
                        onTileAdded()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Tile")
        }
    }
}
