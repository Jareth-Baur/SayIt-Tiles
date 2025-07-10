package com.jareth.aac

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jareth.aac.data.CategoryDao
import com.jareth.aac.data.CategoryEntity
import com.jareth.aac.data.TileDao
import com.jareth.aac.viewmodel.TileViewModel
import com.jareth.aac.viewmodel.TileViewModelFactory

@Composable
fun CategoriesScreen(
    categoryDao: CategoryDao,
    onCategorySelected: (String) -> Unit = {},
    onAddCategoryClicked: () -> Unit = {}
) {
    var categories by remember { mutableStateOf<List<CategoryEntity>>(emptyList()) }

    // Fetch latest categories each time screen appears
    LaunchedEffect(Unit) {
        categories = categoryDao.getAllCategories()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp, start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = { onAddCategoryClicked() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Add Category")
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(categories) { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCategorySelected(category.name) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

