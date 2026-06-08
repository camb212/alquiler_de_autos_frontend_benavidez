package com.trackpets.app.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.trackpets.app.domain.model.Reservation
import com.trackpets.app.domain.model.Vehicle
import com.trackpets.app.domain.model.User
import com.trackpets.app.presentation.viewmodel.ReservationUiState
import com.trackpets.app.presentation.viewmodel.ReservationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationsScreen(viewModel: ReservationViewModel) {
    val uiState = viewModel.uiState
    var showDialog by remember { mutableStateOf(false) }
    var selectedReservation by remember { mutableStateOf<Reservation?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { 
                selectedReservation = null
                showDialog = true 
            }) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Reserva")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text(
                text = "Gestión de Reservas",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            when (uiState) {
                is ReservationUiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
                is ReservationUiState.Error -> Box(Modifier.fillMaxSize(), Alignment.Center) { Text(uiState.message, color = Color.Red) }
                is ReservationUiState.Success -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(uiState.reservations) { reservation ->
                            ReservationItem(
                                reservation = reservation,
                                isAdmin = viewModel.isAdmin,
                                onEdit = { selectedReservation = reservation; showDialog = true },
                                onDelete = { reservation.id?.let { viewModel.deleteReservation(it) } }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        ReservationDialog(
            reservation = selectedReservation,
            vehicles = viewModel.vehicles,
            users = viewModel.users,
            isAdmin = viewModel.isAdmin,
            onDismiss = { showDialog = false },
            onConfirm = { viewModel.saveReservation(it); showDialog = false }
        )
    }
}

@Composable
fun ReservationItem(reservation: Reservation, isAdmin: Boolean, onEdit: () -> Unit, onDelete: () -> Unit) {
    val statusColor = when(reservation.status) {
        "CONFIRMED" -> Color(0xFF4CAF50)
        "CANCELLED" -> Color.Red
        else -> Color(0xFFFF9800)
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Reserva #${reservation.id}", fontWeight = FontWeight.Bold)
                Text("Vehículo ID: ${reservation.vehicleId}")
                Text("Fecha: ${reservation.startDate} a ${reservation.endDate}", style = MaterialTheme.typography.bodySmall)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = statusColor, shape = MaterialTheme.shapes.small) {
                        Text(
                            text = reservation.status,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("$${reservation.totalPrice}", fontWeight = FontWeight.Bold)
                }
            }
            if (isAdmin) {
                IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, "Editar") }
            }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Eliminar", tint = Color.Red) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationDialog(
    reservation: Reservation?,
    vehicles: List<Vehicle>,
    users: List<User>,
    isAdmin: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Reservation) -> Unit
) {
    var selectedVehicle by remember { mutableStateOf(vehicles.find { it.id == reservation?.vehicleId } ?: vehicles.firstOrNull()) }
    var selectedUser by remember { mutableStateOf(users.find { it.id == reservation?.userId } ?: users.firstOrNull()) }
    var startDate by remember { mutableStateOf(reservation?.startDate ?: "2024-05-20") }
    var endDate by remember { mutableStateOf(reservation?.endDate ?: "2024-05-25") }
    var status by remember { mutableStateOf(reservation?.status ?: "PENDING") }
    
    var vehExpanded by remember { mutableStateOf(false) }
    var userExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (reservation == null) "Nueva Reserva" else "Editar Reserva") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Selector de Vehículo
                ExposedDropdownMenuBox(expanded = vehExpanded, onExpandedChange = { vehExpanded = !vehExpanded }) {
                    OutlinedTextField(
                        value = selectedVehicle?.let { "${it.brand} ${it.model}" } ?: "Seleccionar Vehículo",
                        onValueChange = {}, readOnly = true, label = { Text("Vehículo") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = vehExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = vehExpanded, onDismissRequest = { vehExpanded = false }) {
                        vehicles.forEach { v ->
                            DropdownMenuItem(text = { Text("${v.brand} ${v.model}") }, onClick = { selectedVehicle = v; vehExpanded = false })
                        }
                    }
                }

                // Selector de Usuario (Solo Admin)
                if (isAdmin) {
                    ExposedDropdownMenuBox(expanded = userExpanded, onExpandedChange = { userExpanded = !userExpanded }) {
                        OutlinedTextField(
                            value = selectedUser?.username ?: "Seleccionar Cliente",
                            onValueChange = {}, readOnly = true, label = { Text("Cliente") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = userExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(expanded = userExpanded, onDismissRequest = { userExpanded = false }) {
                            users.forEach { u ->
                                DropdownMenuItem(text = { Text(u.username) }, onClick = { selectedUser = u; userExpanded = false })
                            }
                        }
                    }
                }

                OutlinedTextField(value = startDate, onValueChange = { startDate = it }, label = { Text("Inicio (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = endDate, onValueChange = { endDate = it }, label = { Text("Fin (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())

                if (reservation != null && isAdmin) {
                    ExposedDropdownMenuBox(expanded = statusExpanded, onExpandedChange = { statusExpanded = !statusExpanded }) {
                        OutlinedTextField(
                            value = status, onValueChange = {}, readOnly = true, label = { Text("Estado") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(expanded = statusExpanded, onDismissRequest = { statusExpanded = false }) {
                            listOf("PENDING", "CONFIRMED", "CANCELLED").forEach { s ->
                                DropdownMenuItem(text = { Text(s) }, onClick = { status = s; statusExpanded = false })
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(Reservation(
                    id = reservation?.id,
                    userId = selectedUser?.id,
                    vehicleId = selectedVehicle?.id ?: 0,
                    startDate = startDate,
                    endDate = endDate,
                    totalPrice = reservation?.totalPrice ?: 0.0,
                    status = status,
                    createdAt = ""
                ))
            }, enabled = selectedVehicle != null) { Text("Confirmar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
