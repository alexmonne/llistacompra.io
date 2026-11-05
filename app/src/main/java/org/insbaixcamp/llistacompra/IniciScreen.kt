package org.insbaixcamp.llistacompra

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.insbaixcamp.llistacompra.ui.theme.LlistaCompraTheme

@Composable
fun IniciScreen(
    onCrearLlista: (String) -> Unit = {}
) {
    var mostrarFormulari by remember { mutableStateOf(false) }
    var nomLlista by remember { mutableStateOf("") }

    // --- PANTALLA PRINCIPAL ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Benvingut/da!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Gestiona i organitza les teves llistes de la compra d'una manera fàcil i ràpida.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { mostrarFormulari = true }) {
            Text("Crear llista")
        }
    }

    // --- POP-UP PER CREAR LLISTA ---
    if (mostrarFormulari) {
        AlertDialog(
            onDismissRequest = { mostrarFormulari = false },
            title = { Text("Crear nova llista") },
            text = {
                OutlinedTextField(
                    value = nomLlista,
                    onValueChange = { nomLlista = it },
                    label = { Text("Nom de la llista") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nomLlista.isNotBlank()) {
                            onCrearLlista(nomLlista)
                            nomLlista = ""
                            mostrarFormulari = false
                        }
                    }
                ) {
                    Text("Crear")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarFormulari = false }) {
                    Text("Cancel·lar")
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun IniciScreenPreview() {
    LlistaCompraTheme {
        IniciScreen()
    }
}