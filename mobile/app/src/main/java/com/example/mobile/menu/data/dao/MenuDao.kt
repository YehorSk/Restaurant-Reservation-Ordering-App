package com.example.mobile.menu.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mobile.menu.data.db.model.MenuEntity
import com.example.mobile.menu.data.db.model.MenuItemEntity
import com.example.mobile.menu.data.db.model.MenuWithMenuItems
import com.example.mobile.menu.data.remote.dto.MenuDto
import com.example.mobile.menu.data.remote.dto.toMenuEntity
import com.example.mobile.menu.data.remote.dto.toMenuItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {

    @Transaction
    @Query("SELECT * FROM menu_table")
    fun getMenuWithMenuItems(): Flow<List<MenuWithMenuItems>>

    @Query("SELECT * FROM menu_item_table WHERE isFavorite")
    fun getFavoriteItems(): Flow<List<MenuItemEntity>>

    @Query("UPDATE menu_item_table SET isFavorite = 1 WHERE id = :menuItemId")
    suspend fun addFavorite(menuItemId: String)

    @Query("UPDATE menu_item_table SET isFavorite = 0 WHERE id = :menuItemId")
    suspend fun deleteFavorite(menuItemId: String)

    @Query("SELECT * FROM menu_item_table WHERE name LIKE :text OR short_description LIKE :text OR long_description LIKE :text")
    fun searchItems(text: String): Flow<List<MenuItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenu(menu: MenuEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItem(menuItem: MenuItemEntity)

    @Delete
    suspend fun deleteMenu(menu: MenuEntity)

    @Delete
    suspend fun deleteMenuItem(menuItem: MenuItemEntity)

    @Update
    suspend fun updateMenu(menu: MenuEntity)

    @Update
    suspend fun updateMenuItem(menuItem: MenuItemEntity)

    @Transaction
    @Query("SELECT * FROM menu_table")
    suspend fun getMenuWithMenuItemsOnce(): List<MenuWithMenuItems>


    @Transaction
    suspend fun runInTransaction(block: suspend () -> Unit) {
        block()
    }
}