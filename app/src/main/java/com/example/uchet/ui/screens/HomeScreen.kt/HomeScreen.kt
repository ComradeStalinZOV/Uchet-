package com.example.uchet.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uchet.data.Expense
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    expenses: List<Expense>,
    monthTotal: Double,
    onAddClick: () -> Unit,
    onDelete: (Expense) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Мои траты", fontWeight = FontWeight.Bold) })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddClick,
                icon = { Icon(Icons.Default.Add, "Добавить") },
                text = { Text("Добавить") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("Потрачено в этом месяце", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f))
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "%,.0f руб".format(monthTotal),
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Text("История", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            if (expenses.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Пока нет записей. Добавьте первую трату",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(expenses, key = { it.id }) { e -> ExpenseItem(e, onDelete) }
                }
            }
        }
    }
}

@Composable
fun ExpenseItem(e: Expense, onDelete: (Expense) -> Unit) {
    val fmt = remember { SimpleDateFormat("dd MMM, HH:mm", Locale("ru")) }
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(e.category, fontWeight = FontWeight.SemiBold)
                if (e.note.isNotBlank()) Text(e.note, fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                Text(fmt.format(Date(e.timestamp)), fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            }
            Text("%,.0f руб".format(e.amount), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            IconButton(onClick = { onDelete(e) }) {
                Icon(Icons.Default.Delete, "Удалить")
            }
        }
    }
}
