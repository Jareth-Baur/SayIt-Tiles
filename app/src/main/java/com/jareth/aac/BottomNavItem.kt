package com.jareth.aac

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    data object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    data object AddTile : BottomNavItem("add", "Add Tile", Icons.Default.Add)
    data object Categories : BottomNavItem("categories", "Categories", Icons.Default.List)
    data object Settings : BottomNavItem("settings", "Settings", Icons.Default.Settings)
}