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

// --- MODELS DE DADES ---

data class Producte(
    var nom: String = "",
    var completat: Boolean = false
)

data class Categoria(
    var nom: String = "",
    var productes: MutableList<Producte> = mutableListOf()
)

data class Llista(
    var id: String = "",
    var nom: String = "",
    var usuarisCompartits: MutableList<String> = mutableListOf(),
    var categories: MutableList<Categoria> = mutableListOf()
)

// --- PANTALLA DE PRODUCTES ---

@Composable
fun Productes(
    llista: Llista = Llista(),
    onGuardar: () -> Unit = {}
) {
    var mostrarFormulari by remember { mutableStateOf(false) }
    var nomProducte by remember { mutableStateOf("") }
    var nomCategoria by remember { mutableStateOf("") }

    var refrescar by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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

        Button(onClick = { mostrarFormulari = true }) {
            Text("+ Afegir producte")
        }

        Spacer(modifier = Modifier.height(16.dp))

        key(refrescar) {
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
                                        onCheckedChange = { checked ->
                                            producte.completat = checked
                                            refrescar++
                                        }
                                    )
                                    Text(
                                        text = producte.nom,
                                        textDecoration = if (producte.completat) TextDecoration.LineThrough else TextDecoration.None
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        categoria.productes.remove(producte)
                                        if (categoria.productes.isEmpty()) {
                                            llista.categories.remove(categoria)
                                        }
                                        refrescar++
                                    }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = null)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarFormulari) {
        AlertDialog(
            onDismissRequest = { mostrarFormulari = false },
            title = { Text("Nou producte") },
            text = {
                Column {
                    OutlinedTextField(
                        value = nomProducte,
                        onValueChange = { nomProducte = it },
                        label = { Text("Nom del producte") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = nomCategoria,
                        onValueChange = { nomCategoria = it },
                        label = { Text("Categoria del producte") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nomProducte.isNotBlank()) {
                            val nomCat = if (nomCategoria.isBlank()) "General" else nomCategoria

                            var categoriaTrobada: Categoria? = null
                            for (c in llista.categories) {
                                if (c.nom == nomCat) {
                                    categoriaTrobada = c
                                }
                            }

                            if (categoriaTrobada == null) {
                                categoriaTrobada = Categoria(nom = nomCat)
                                llista.categories.add(categoriaTrobada)
                            }

                            categoriaTrobada.productes.add(Producte(nom = nomProducte))

                            nomProducte = ""
                            nomCategoria = ""
                            mostrarFormulari = false
                            refrescar++
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

// --- VISTA PRÈVIA ---
@Preview(showBackground = true)
@Composable
fun ProductesPreview() {
    Productes()
}