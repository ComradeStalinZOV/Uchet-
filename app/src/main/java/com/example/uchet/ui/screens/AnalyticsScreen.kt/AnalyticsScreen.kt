package com.example.uchet.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uchet.data.CategorySum
import com.example.uchet.viewmodel.OptimizationTip

@Composable
fun AnalyticsScreen(
    byCategory: List<CategorySum>,
    tips: List<OptimizationTip>
) {
    val total = byCategory.sumOf { it.total }.takeIf { it > 0 } ?: 1.0

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Text("Категории за месяц", fontWeight = FontWeight.Bold, fontSize = 20.sp) }
        items(byCategory) { c ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(c.category, fontWeight = FontWeight.SemiBold)
                        Text("%,.0f руб".format(c.total), fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { (c.total / total).toFloat() },
                        modifier = Modifier.fillMaxWidth().height(8.dp)
                    )
                }
            }
        }
        item {
            Spacer(Modifier.height(8.dp))
            Text("Как сэкономить", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
        if (tips.isEmpty()) {
            item { Text("Пока нечего оптимизировать — добавьте больше трат.",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)) }
        } else {
            items(tips) { tip ->
                Card(
                    Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(tip.title, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(4.dp))
                        Text(tip.description, fontSize = 14.sp)
                        Spacer(Modifier.height(6.dp))
                        Text("Экономия до %,.0f руб/мес".format(tip.potentialSaving),
                            color = Color(0xFF00A86B), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
        item { Spacer(Modifier.height(80.dp)) }
    }
}
