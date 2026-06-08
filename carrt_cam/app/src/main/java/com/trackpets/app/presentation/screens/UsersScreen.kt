package com.trackpets.app.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.trackpets.app.domain.model.User
import com.trackpets.app.presentation.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    viewModel: UserViewModel,
    onLogout: () -> Unit
) {
    val userRole = viewModel.userRole
    val isLoading = viewModel.isLoading
    var showDialog by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<User?>(null) }

    Scaffold(
        floatingActionButton = {
            if (userRole == "ADMIN") {
                FloatingActionButton(onClick = {
                    selectedUser = null
                    showDialog = true
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Nuevo Usuario")
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (userRole == "ADMIN") "Gestión de Usuarios" else "Mi Perfil",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Button(onClick = { viewModel.logout(onLogout) }) {
                    Text("Cerrar Sesión")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                if (userRole == "ADMIN") {
                    AdminUserList(
                        users = viewModel.users,
                        onEdit = {
                            selectedUser = it
                            showDialog = true
                        },
                        onDelete = { viewModel.deleteUser(it) }
                    )
                } else {
                    UserProfile(user = viewModel.currentUser)
                }
            }
        }
    }

    if (showDialog) {
        UserDialog(
            user = selectedUser,
            onDismiss = { showDialog = false },
            onConfirm = {
                viewModel.saveUser(it)
                showDialog = false
            }
        )
    }
}

@Composable
fun AdminUserList(
    users: List<User>,
    onEdit: (User) -> Unit,
    onDelete: (Int) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(users) { user ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "${user.firstName} ${user.lastName}", fontWeight = FontWeight.Bold)
                        Text(text = "@${user.username}", style = MaterialTheme.typography.bodySmall)
                        Text(text = user.email, style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            color = if (user.role == "ADMIN") Color.Red else Color.Blue,
                            shape = MaterialTheme.shapes.extraSmall
                        ) {
                            Text(
                                text = user.role,
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Row {
                        IconButton(onClick = { onEdit(user) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                        }
                        IconButton(onClick = { user.id?.let { onDelete(it) } }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserDialog(
    user: User?,
    onDismiss: () -> Unit,
    onConfirm: (User) -> Unit
) {
    var username by remember { mutableStateOf(user?.username ?: "") }
    var firstName by remember { mutableStateOf(user?.firstName ?: "") }
    var lastName by remember { mutableStateOf(user?.lastName ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var phone by remember { mutableStateOf(user?.phone ?: "") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(user?.role ?: "CLIENT") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (user == null) "Nuevo Usuario" else "Editar Usuario") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") }, modifier = Modifier.fillMaxWidth())
                
                OutlinedTextField(
                    value = password, 
                    onValueChange = { password = it }, 
                    label = { Text(if (user == null) "Contraseña" else "Nueva Contraseña (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
                
                Box {
                    OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                        Text("Rol: $role")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = { Text("CLIENT") }, onClick = { role = "CLIENT"; expanded = false })
                        DropdownMenuItem(text = { Text("ADMIN") }, onClick = { role = "ADMIN"; expanded = false })
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        User(
                            id = user?.id,
                            username = username,
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            phone = phone,
                            role = role,
                            password = if (password.isNotBlank()) password else null
                        )
                    )
                },
                enabled = username.isNotBlank() && firstName.isNotBlank() && (user != null || password.isNotBlank())
            ) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun UserProfile(user: User?) {
    if (user == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "${user.firstName} ${user.lastName}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(text = "@${user.username}", color = Color.Gray)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                ProfileInfoRow(label = "Correo", value = user.email)
                ProfileInfoRow(label = "Teléfono", value = user.phone)
                ProfileInfoRow(label = "Rol", value = user.role)
            }
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.SemiBold)
        Text(text = value)
    }
}
