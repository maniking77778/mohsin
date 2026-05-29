package com.example.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers ORDER BY timestamp DESC")
    fun getAllCustomers(): Flow<List<Customer>>

    @Query("SELECT * FROM customers WHERE name LIKE :query OR phone LIKE :query OR deviceModel LIKE :query ORDER BY timestamp DESC")
    fun searchCustomers(query: String): Flow<List<Customer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: Customer): Long

    @Update
    suspend fun updateCustomer(customer: Customer)

    @Delete
    suspend fun deleteCustomer(customer: Customer)

    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun getCustomerById(id: Long): Customer?
}

@Dao
interface RepairDao {
    @Query("SELECT * FROM repair_records ORDER BY createdTimestamp DESC")
    fun getAllRepairs(): Flow<List<RepairRecord>>

    @Query("SELECT * FROM repair_records WHERE customerName LIKE :query OR deviceModel LIKE :query OR deviceImei LIKE :query OR status LIKE :query ORDER BY createdTimestamp DESC")
    fun searchRepairs(query: String): Flow<List<RepairRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepair(record: RepairRecord): Long

    @Update
    suspend fun updateRepair(record: RepairRecord)

    @Delete
    suspend fun deleteRepair(record: RepairRecord)

    @Query("SELECT * FROM repair_records WHERE id = :id")
    suspend fun getRepairById(id: Long): RepairRecord?

    @Query("SELECT * FROM repair_records WHERE customerId = :customerId ORDER BY createdTimestamp DESC")
    fun getRepairsForCustomer(customerId: Long): Flow<List<RepairRecord>>
}

@Dao
interface ImeiHistoryDao {
    @Query("SELECT * FROM imei_history ORDER BY timestamp DESC")
    fun getAllImeiHistory(): Flow<List<ImeiHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImeiHistory(history: ImeiHistory)

    @Query("DELETE FROM imei_history")
    suspend fun clearHistory()
}

@Database(entities = [Customer::class, RepairRecord::class, ImeiHistory::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
    abstract fun repairDao(): RepairDao
    abstract fun imeiHistoryDao(): ImeiHistoryDao
}
