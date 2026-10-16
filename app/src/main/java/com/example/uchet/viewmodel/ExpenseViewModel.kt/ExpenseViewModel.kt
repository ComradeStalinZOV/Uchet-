package com.example.uchet.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uchet.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class OptimizationTip(val title: String, val description: String, val potentialSaving: Double)

class ExpenseViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = ExpenseRepository(AppDatabase.get(app).expenseDao())

    val expenses = repo.allExpenses.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val monthTotal = repo.monthSum().stateIn(viewModelScope, SharingStarted.Lazily, 0.0)
    val byCategory = repo.byCategory().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun add(amount: Double, category: String, note: String, essential: Boolean) {
        viewModelScope.launch {
            repo.add(Expense(amount = amount, category = category, note = note, isEssential = essential))
        }
    }

    fun delete(e: Expense) = viewModelScope.launch { repo.delete(e) }

    fun optimizationTips(): List<OptimizationTip> {
        val list = expenses.value
        val tips = mutableListOf<OptimizationTip>()
        if (list.isEmpty()) return tips

        val catMap = list.groupBy { it.category }.mapValues { it.value.sumOf { e -> e.amount } }
        val totalSum = list.sumOf { it.amount }.takeIf { it > 0 } ?: 1.0

        catMap["Кафе"]?.let {
            if (it > 3000) tips += OptimizationTip(
                "Сократите кафе",
                "Вы потратили %.0f руб на кафе. Готовка дома сэкономит около 30%%.".format(it),
                it * 0.3
            )
        }
        catMap["Развлечения"]?.let {
            if (it > 2000) tips += OptimizationTip(
                "Развлечения под контролем",
                "Пересмотрите подписки и разовые покупки. Можно урезать на 20%%.",
                it * 0.2
            )
        }
        catMap["Подписки"]?.let {
            if (it > 500) tips += OptimizationTip(
                "Аудит подписок",
                "Проверьте, какими сервисами вы реально пользуетесь. Отмена лишних сэкономит до 50%%.",
                it * 0.5
            )
        }
        catMap["Транспорт"]?.let {
            if (it > 4000) tips += OptimizationTip(
                "Оптимизация транспорта",
                "Попробуйте проездной или каршеринг вместо такси.",
                it * 0.25
            )
        }

        val nonEssential = list.filter { !it.isEssential }.sumOf { it.amount }
        if (nonEssential > totalSum * 0.4) {
            tips += OptimizationTip(
                "Много необязательных трат",
                "%.0f%% ваших расходов — необязательные. Урежьте их на 15%%.".format(nonEssential / totalSum * 100),
                nonEssential * 0.15
            )
        }
        return tips.sortedByDescending { it.potentialSaving }
    }
}
