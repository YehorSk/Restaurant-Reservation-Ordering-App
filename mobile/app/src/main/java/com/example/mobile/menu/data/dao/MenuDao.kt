package com.example.mobile.menu.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.mobile.menu.data.db.model.MenuWithMenuItems
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {

    @Transaction
    @Query("SELECT * FROM menu_table")
    fun getMenuWithMenuItems(): Flow<List<MenuWithMenuItems>>

}