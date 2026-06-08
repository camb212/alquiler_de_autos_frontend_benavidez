package com.trackpets.app.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trackpets.app.domain.repository.RentalRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Composable
fun LoginScreen(
    repository: RentalRepository,
    onLoginSuccess: () -> Unit,
    onGoRegister: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Carrt Cam",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Gestión de Alquiler de Autos",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.secondary
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        OutlinedTextField(
            value = username,
            onValueChange = { username = it; errorMessage = null },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; errorMessage = null },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            enabled = !isLoading
        )
        
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    errorMessage = null
                    repository.login(username, password)
                        .onSuccess { onLoginSuccess() }
                        .onFailure { e ->
                            errorMessage = when(e) {
                                is java.net.SocketTimeoutException -> "Error: Tiempo de espera agotado."
                                is java.net.ConnectException -> "Error: No se pudo conectar al servidor."
                                is HttpException -> {
                                    when(e.code()) {
                                        401 -> "Error 401: Usuario o clave incorrectos."
                                        404 -> "Error 404: Ruta de login no encontrada."
                                        400 -> "Error 400: Datos mal formados."
                                        else -> "Error del servidor: ${e.code()}"
                                    }
                                }
                                else -> "Error inesperado: ${e.localizedMessage ?: "Fallo de datos"}"
                            }
                        }
                    isLoading = false
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = !isLoading && username.isNotBlank() && password.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("INGRESAR")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(onClick = onGoRegister, enabled = !isLoading) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}
