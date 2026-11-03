package org.insbaixcamp.llistacompra

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var nouNomLlista by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Benvingut/da/de!",
            fontSize = 33.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Gestiona i organitza les teves llistes de la compra d'una manera fàcil i ràpida.",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = { mostrarFormulari = true }) {
            Text(text = "Crear llista")
        }
    }

    if (mostrarFormulari) {
        AlertDialog(
            onDismissRequest = { mostrarFormulari = false },
            title = { Text(text = "Crear nova llista") },
            text = {
                OutlinedTextField(
                    value = nouNomLlista,
                    onValueChange = { nouNomLlista = it },
                    label = { Text("Nom de la llista") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nouNomLlista.isNotBlank()) {
                            onCrearLlista(nouNomLlista) // Envia el nom escrit a MainActivity
                            nouNomLlista = ""
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
fun HomeScreenPreview() {
    LlistaCompraTheme {
        IniciScreen()
    }
}