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

    // Tenim la pantalla principal de inci
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //Ens mostrara el missatge de Benvinguda
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
        //Tenim el botó que ens permetra crear una llista nova
        Button(onClick = { mostrarFormulari = true }) {
            Text("Crear llista")
        }
    }

    // Ens apareixera la finestra per introduir el nom de la llista
    if (mostrarFormulari) {
        AlertDialog(
            onDismissRequest = { mostrarFormulari = false },
            title = { Text("Crear nova llista") },
            text = {
                //Introduirem el nom de la llista
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
//Vista previa
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun IniciScreenPreview() {
    LlistaCompraTheme {
        IniciScreen()
    }
}