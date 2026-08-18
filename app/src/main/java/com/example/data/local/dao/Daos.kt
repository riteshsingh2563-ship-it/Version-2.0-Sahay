package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.AddressEntity
import com.example.data.local.entities.BookingEntity
import com.example.data.local.entities.NotificationEntity
import com.example.data.local.entities.WalletTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY timestamp DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE status != 'COMPLETED' AND status != 'CANCELLED' ORDER BY timestamp DESC")
    fun getActiveBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE id = :id")
    suspend fun getBookingById(id: String): BookingEntity?

    @Query("SELECT * FROM bookings WHERE id = :id")
    fun observeBookingById(id: String): Flow<BookingEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Update
    suspend fun updateBooking(booking: BookingEntity)

    @Query("UPDATE bookings SET status = :newStatus WHERE id = :id")
    suspend fun updateBookingStatus(id: String, newStatus: String)

    @Query("DELETE FROM bookings WHERE id = :id")
    suspend fun deleteBooking(id: String)
}

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallet_transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<WalletTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: WalletTransactionEntity)

    @Query("SELECT SUM(CASE WHEN isCredit = 1 THEN amount ELSE -amount END) FROM wallet_transactions WHERE status = 'SUCCESS'")
    fun getWalletBalanceFlow(): Flow<Double?>
}

@Dao
interface AddressDao {
    @Query("SELECT * FROM saved_addresses")
    fun getAllAddresses(): Flow<List<AddressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: AddressEntity)

    @Query("DELETE FROM saved_addresses WHERE id = :id")
    suspend fun deleteAddress(id: String)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()
}
