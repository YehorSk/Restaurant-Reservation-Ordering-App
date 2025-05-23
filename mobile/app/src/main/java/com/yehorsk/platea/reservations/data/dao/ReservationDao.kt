package com.yehorsk.platea.reservations.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.yehorsk.platea.reservations.data.db.model.ReservationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {

    @Query("SELECT * FROM reservation_table WHERE (:search = '' OR code LIKE '%' || :search || '%') AND (:filter = '' OR status LIKE :filter) ORDER BY date ASC, start_time ASC;")
    fun getUserReservations(search: String = "", filter: String = ""): Flow<List<ReservationEntity>>

    @Query("SELECT * FROM reservation_table;")
    suspend fun getUserReservationsOnce(): List<ReservationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservations(reservations: List<ReservationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservation(reservation: ReservationEntity)

    @Update
    suspend fun updateReservation(reservation: ReservationEntity)

    @Delete
    suspend fun deleteReservation(reservation: ReservationEntity)

    @Delete
    suspend fun deleteItems(items: List<ReservationEntity>)

    @Query("DELETE FROM reservation_table")
    suspend fun deleteAllItems()

    @Transaction
    suspend fun runInTransaction(block: suspend () -> Unit) {
        block()
    }
    
}