package org.insbaixcamp.llistacompra

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.insbaixcamp.llistacompra.ui.theme.LlistaCompraTheme

// --- MODELS ---
data class Producte(
    var nom: String,
    var completat: Boolean = false
)

data class Categoria(
    var nom: String,
    val productes: MutableList<Producte> = mutableStateListOf()
)

data class Llista(
    var nom: String = "",
    val categories: MutableList<Categoria> = mutableStateListOf(),
    val esCompartida: Boolean = false
)

// --- PANTALLA ---
@Composable
fun Productes(
    llista: Llista,
    onGuardar: () -> Unit = {}
) {
    var mostrarFormulari by remember { mutableStateOf(false) }
    var nomProducte by remember { mutableStateOf("") }
    var nomCategoria by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Títol i Botó Guardar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = llista.nom.ifBlank { "Llista" },
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Button(onClick = onGuardar) {
                Text("Guardar")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botó per afegir producte
        Button(onClick = { mostrarFormulari = true }) {
            Text("+ Afegir producte")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Llista de productes agrupats per categoria
        if (llista.categories.isEmpty()) {
            Text("No hi ha cap producte.")
        } else {
            LazyColumn {
                items(llista.categories) { categoria ->
                    Text(
                        text = categoria.nom,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                    )

                    categoria.productes.forEach { producte ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = producte.completat,
                                    onCheckedChange = { producte.completat = it }
                                )
                                Text(
                                    text = producte.nom,
                                    textDecoration = if (producte.completat) TextDecoration.LineThrough else TextDecoration.None
                                )
                            }

                            IconButton(onClick = { categoria.productes.remove(producte) }) {
                                Icon(Icons.Default.Delete, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }

    // FORMULARI PER CREAR PRODUCTE I CATEGORIA
    if (mostrarFormulari) {
        AlertDialog(
            onDismissRequest = { mostrarFormulari = false },
            title = { Text("Nou producte") },
            text = {
                Column {
                    OutlinedTextField(
                        value = nomProducte,
                        onValueChange = { nomProducte = it },
                        label = { Text("Nom del producte") },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = nomCategoria,
                        onValueChange = { nomCategoria = it },
                        label = { Text("Categoria del producte") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nomProducte.isNotBlank()) {
                            val nomCat = if (nomCategoria.isBlank()) "General" else nomCategoria.trim()

                            var cat = llista.categories.find { it.nom.equals(nomCat, ignoreCase = true) }
                            if (cat == null) {
                                cat = Categoria(nom = nomCat)
                                llista.categories.add(cat)
                            }

                            cat.productes.add(Producte(nom = nomProducte.trim()))

                            nomProducte = ""
                            nomCategoria = ""
                            mostrarFormulari = false
                        }
                    }
                ) {
                    Text("Guardar producte")
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
fun ProductesPreview() {
    LlistaCompraTheme {
        Productes(llista = Llista(nom = "Llista de prova",))
    }
}