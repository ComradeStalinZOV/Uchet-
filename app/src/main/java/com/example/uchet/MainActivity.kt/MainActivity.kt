package com.example.uchet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.uchet.ui.screens.*
import com.example.uchet.ui.theme.UchetTheme
import com.example.uchet.viewmodel.ExpenseViewModel

class MainActivity : ComponentActivity() {
    private val vm: ExpenseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UchetTheme {
                val nav = rememberNavController()
                val expenses by vm.expenses.collectAsState()
                val monthTotal by vm.monthTotal.collectAsState()
                val byCategory by vm.byCategory.collectAsState()
                val currentRoute = nav.currentBackStackEntryAsState().value?.destination?.route

                Scaffold(
                    bottomBar = {
                        if (currentRoute != "add") {
                            NavigationBar {
                                NavigationBarItem(
                                    selected = currentRoute == "home",
                                    onClick = { nav.navigate("home") { launchSingleTop = true } },
                                    icon = { Icon(Icons.Default.Home, null) },
                                    label = { Text("Главная") }
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "analytics",
                                    onClick = { nav.navigate("analytics") { launchSingleTop = true } },
                                    icon = { Icon(Icons.Default.Analytics, null) },
                                    label = { Text("Аналитика") }
                                )
                            }
                        }
                    }
                ) { p ->
                    NavHost(nav, startDestination = "home", modifier = Modifier.padding(p)) {
                        composable("home") {
                            HomeScreen(
                                expenses = expenses,
                                monthTotal = monthTotal ?: 0.0,
                                onAddClick = { nav.navigate("add") },
                                onDelete = vm::delete
                            )
                        }
                        composable("analytics") {
                            AnalyticsScreen(byCategory, vm.optimizationTips())
                        }
                        composable("add") {
                            AddExpenseScreen(
                                onBack = { nav.popBackStack() },
                                onSave = { a, c, n, e ->
                                    vm.add(a, c, n, e)
                                    nav.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
