package com.jareth.aac.data

import androidx.room.*

@Dao
interface TileDao {
    @Query("SELECT * FROM tiles WHERE category = :category")
    suspend fun getTilesByCategory(category: String): List<TileEntity>

    @Query("SELECT DISTINCT category FROM tiles ORDER BY category")
    suspend fun getAllCategories(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tile: TileEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(tiles: List<TileEntity>)

    @Delete
    suspend fun deleteTile(tile: TileEntity)
}
