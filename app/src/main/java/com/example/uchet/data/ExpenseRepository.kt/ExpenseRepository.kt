package com.example.uchet.data

import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class ExpenseRepository(private val dao: ExpenseDao) {
    val allExpenses: Flow<List<Expense>> = dao.getAll()
    suspend fun add(e: Expense) = dao.insert(e)
    suspend fun delete(e: Expense) = dao.delete(e)
    fun monthSum(): Flow<Double?> = dao.sumSince(startOfMonth())
    fun byCategory(): Flow<List<CategorySum>> = dao.sumByCategory(startOfMonth())

    private fun startOfMonth(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }
}
