package com.trackpets.app.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trackpets.app.domain.model.Category
import com.trackpets.app.domain.model.Vehicle
import com.trackpets.app.presentation.viewmodel.VehicleUiState
import com.trackpets.app.presentation.viewmodel.VehicleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiclesScreen(viewModel: VehicleViewModel) {
    val uiState = viewModel.uiState
    var showDialog by remember { mutableStateOf(false) }
    var selectedVehicle by remember { mutableStateOf<Vehicle?>(null) }

    Scaffold(
        floatingActionButton = {
            if (viewModel.isAdmin) {
                FloatingActionButton(onClick = { 
                    selectedVehicle = null
                    showDialog = true 
                }) { Icon(Icons.Default.Add, "Nuevo") }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = viewModel.searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar marca o modelo...") },
                leadingIcon = { Icon(Icons.Default.Search, null) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            when (uiState) {
                is VehicleUiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
                is VehicleUiState.Error -> Box(Modifier.fillMaxSize(), Alignment.Center) { Text(uiState.message, color = Color.Red) }
                is VehicleUiState.Success -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(uiState.vehicles) { vehicle ->
                            VehicleItem(
                                vehicle = vehicle,
                                isAdmin = viewModel.isAdmin,
                                onEdit = { selectedVehicle = vehicle; showDialog = true },
                                onDelete = { vehicle.id?.let { viewModel.deleteVehicle(it) } }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        VehicleDialog(
            vehicle = selectedVehicle,
            categories = viewModel.categories,
            onDismiss = { showDialog = false },
            onConfirm = { viewModel.saveVehicle(it); showDialog = false }
        )
    }
}

@Composable
fun VehicleItem(vehicle: Vehicle, isAdmin: Boolean, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("${vehicle.brand} ${vehicle.model}", style = MaterialTheme.typography.titleMedium)
                Text("Placa: ${vehicle.plate} • Año: ${vehicle.year}")
                Text("$${vehicle.pricePerDay} / día", color = MaterialTheme.colorScheme.primary)
            }
            if (isAdmin) {
                IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, "Editar") }
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Eliminar", tint = Color.Red) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDialog(
    vehicle: Vehicle?,
    categories: List<Category>,
    onDismiss: () -> Unit,
    onConfirm: (Vehicle) -> Unit
) {
    var brand by remember { mutableStateOf(vehicle?.brand ?: "") }
    var model by remember { mutableStateOf(vehicle?.model ?: "") }
    var year by remember { mutableStateOf(vehicle?.year?.toString() ?: "2024") }
    var plate by remember { mutableStateOf(vehicle?.plate ?: "") }
    var price by remember { mutableStateOf(vehicle?.pricePerDay?.toString() ?: "") }
    
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { 
        mutableStateOf(categories.find { it.id == vehicle?.categoryId } ?: categories.firstOrNull()) 
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (vehicle == null) "Nuevo Vehículo" else "Editar Vehículo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("Marca") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = model, onValueChange = { model = it }, label = { Text("Modelo") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = year, onValueChange = { year = it }, label = { Text("Año") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = plate, onValueChange = { plate = it }, label = { Text("Placa") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Precio por día") }, modifier = Modifier.fillMaxWidth())
                
                // Selector de Categoría
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory?.name ?: "Seleccionar Categoría",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        Vehicle(
                            id = vehicle?.id,
                            brand = brand,
                            model = model,
                            year = year.toIntOrNull() ?: 2024,
                            plate = plate,
                            pricePerDay = price.toDoubleOrNull() ?: 0.0,
                            available = true,
                            categoryId = selectedCategory?.id
                        )
                    )
                },
                enabled = brand.isNotBlank() && model.isNotBlank() && plate.isNotBlank() && selectedCategory != null
            ) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
