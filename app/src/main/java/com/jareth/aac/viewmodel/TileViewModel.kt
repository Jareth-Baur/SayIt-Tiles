package com.jareth.aac.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jareth.aac.data.TileDao
import com.jareth.aac.data.TileEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TileViewModel(private val tileDao: TileDao) : ViewModel() {

    private val _tiles = MutableStateFlow<List<TileEntity>>(emptyList())
    val tiles: StateFlow<List<TileEntity>> = _tiles

    fun loadTilesByCategory(category: String) {
        viewModelScope.launch {
            _tiles.value = tileDao.getTilesByCategory(category)
        }
    }
    fun addTile(tile: TileEntity) {
        viewModelScope.launch {
            tileDao.insertAll(listOf(tile))
        }
    }

    suspend fun getAllCategories(): List<String> {
        return tileDao.getAllCategories()
    }

}
