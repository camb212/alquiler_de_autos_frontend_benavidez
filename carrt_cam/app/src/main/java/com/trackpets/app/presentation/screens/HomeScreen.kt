package com.trackpets.app.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trackpets.app.presentation.components.InfoCard
import com.trackpets.app.presentation.components.SectionHeader
import com.trackpets.app.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    paddingValues: PaddingValues
) {
    val state = viewModel.uiState

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (state.errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = state.errorMessage, color = MaterialTheme.colorScheme.error)
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                SectionHeader(
                    title = "Resumen de la flota",
                    subtitle = "Panel Compose construido a partir del backend Django REST.",
                )
            }
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoCard(
                        title = "Vehículos",
                        value = state.vehicles.size.toString(),
                        subtitle = "Elementos disponibles en la API",
                        accent = Color(0xFF53A8FF),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    InfoCard(
                        title = "Categorías",
                        value = state.categories.size.toString(),
                        subtitle = "Grupos del catálogo del backend",
                        accent = Color(0xFF38E8D5),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    InfoCard(
                        title = "Reservas",
                        value = state.reservations.size.toString(),
                        subtitle = "Registros de reservas actuales",
                        accent = Color(0xFFFFB340),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
            item {
                Text(text = "Vehículos recientes", style = MaterialTheme.typography.titleLarge)
            }
            items(state.vehicles.take(3)) { vehicle ->
                Card {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "${vehicle.brand} ${vehicle.model}", style = MaterialTheme.typography.titleMedium)
                        Text(text = "${vehicle.year} • ${vehicle.plate}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "$${vehicle.pricePerDay} / día", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
