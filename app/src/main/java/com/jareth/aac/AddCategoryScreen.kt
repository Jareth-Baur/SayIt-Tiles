package com.jareth.aac

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jareth.aac.data.CategoryEntity
import com.jareth.aac.data.CategoryDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AddCategoryScreen(
    categoryDao: CategoryDao,
    navController: NavHostController // 👈 Add this
) {
    var categoryName by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = categoryName,
            onValueChange = { categoryName = it },
            label = { Text("New Category Name") }
        )

        Button(
            onClick = {
                if (categoryName.isNotBlank()) {
                    val newCategory = CategoryEntity(name = categoryName.trim())
                    coroutineScope.launch {
                        categoryDao.insert(newCategory)
                        navController.popBackStack() // ✅ Go back after adding
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Category")
        }
    }
}

