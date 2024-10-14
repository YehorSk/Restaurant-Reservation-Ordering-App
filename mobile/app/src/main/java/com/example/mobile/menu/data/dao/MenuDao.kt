package com.example.mobile.menu.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.mobile.menu.data.db.model.MenuEntity
import com.example.mobile.menu.data.db.model.MenuItemEntity
import com.example.mobile.menu.data.db.model.MenuWithMenuItems
import com.example.mobile.menu.data.remote.model.Menu
import com.example.mobile.menu.data.remote.model.MenuItem
import com.example.mobile.menu.data.remote.model.toMenuEntity
import com.example.mobile.menu.data.remote.model.toMenuItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {

    @Transaction
    @Query("SELECT * FROM menu_table")
    fun getMenuWithMenuItems(): Flow<List<MenuWithMenuItems>>

    @Transaction
    suspend fun insert(menus: List<Menu>){
        for(item in menus){
            insertMenu(item.toMenuEntity())
            for(menuItem in item.items){
                insertMenuItem(menuItem.toMenuItemEntity())
            }
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenu(menu: MenuEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItem(menuItem: MenuItemEntity)


}