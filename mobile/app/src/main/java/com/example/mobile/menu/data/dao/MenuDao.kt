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
import com.example.mobile.menu.data.remote.dto.Menu
import com.example.mobile.menu.data.remote.dto.toMenuEntity
import com.example.mobile.menu.data.remote.dto.toMenuItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {

    @Transaction
    @Query("SELECT * FROM menu_table")
    fun getMenuWithMenuItems(): Flow<List<MenuWithMenuItems>>

    @Query("SELECT * FROM menu_item_table WHERE name LIKE :text OR short_description LIKE :text OR long_description LIKE :text")
    fun searchItems(text: String): Flow<List<MenuItemEntity>>

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

    @Delete
    suspend fun deleteMenu(menu: MenuEntity)

    @Delete
    suspend fun deleteMenuItem(menuItem: MenuItemEntity)

    @Update
    suspend fun updateMenu(menu: MenuEntity)

    @Update
    suspend fun updateMenuItem(menuItem: MenuItemEntity)

    @Transaction
    suspend fun syncMenusWithServer(serverMenus: List<Menu>) {
        val localMenus = getMenuWithMenuItemsOnce()

        val serverMenuIds = serverMenus.map { it.id }.toSet()

        val serverMenuItemsIds = serverMenus.flatMap { menu -> menu.items.map { it.id } }.toSet()

        val menusToDelete = localMenus.filter { it.menu.id !in serverMenuIds }

        val menuItemsToDelete = localMenus.flatMap { localMenu ->
            localMenu.menuItems.filter { it.id !in serverMenuItemsIds }
        }

        for (menu in menusToDelete) {
            deleteMenu(menu.menu)
            deleteMenuItems(menu.menuItems.map { it })
        }

        deleteMenuItems(menuItemsToDelete.map { it })

        insert(serverMenus)
    }


    @Transaction
    @Query("SELECT * FROM menu_table")
    suspend fun getMenuWithMenuItemsOnce(): List<MenuWithMenuItems>

    @Transaction
    suspend fun deleteMenuItems(menuItems: List<MenuItemEntity>) {
        for (item in menuItems) {
            deleteMenuItem(item)
        }
    }

    @Transaction
    suspend fun runInTransaction(block: suspend () -> Unit) {
        block()
    }
}