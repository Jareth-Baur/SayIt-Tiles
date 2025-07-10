// TileViewModelFactory.kt
package com.jareth.aac.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jareth.aac.data.TileDao

class TileViewModelFactory(private val tileDao: TileDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TileViewModel(tileDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
