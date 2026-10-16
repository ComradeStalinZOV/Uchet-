package com.example.uchet.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    fun getAll(): Flow<List<Expense>>

    @Insert
    suspend fun insert(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("SELECT SUM(amount) FROM expenses WHERE timestamp >= :from")
    fun sumSince(from: Long): Flow<Double?>

    @Query("SELECT category, SUM(amount) as total FROM expenses WHERE timestamp >= :from GROUP BY category ORDER BY total DESC")
    fun sumByCategory(from: Long): Flow<List<CategorySum>>
}

data class CategorySum(val category: String, val total: Double)
