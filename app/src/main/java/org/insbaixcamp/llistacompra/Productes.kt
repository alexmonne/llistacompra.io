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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


//Un producte tindra un nom, i si està completat o no
data class Producte(
    var nom: String = "",
    var completat: Boolean = false
)

//Una categoria tindra un nom i una llista de productes de la mateixa categoria
data class Categoria(
    var nom: String = "",
    var productes: MutableList<Producte> = mutableListOf()
)

//Una llista tindra un nom, una llista d'usuaris que la comparteixen, i una llista de categories
data class Llista(
    var id: String = "",
    var nom: String = "",
    var usuarisCompartits: MutableList<String> = mutableListOf(),
    var categories: MutableList<Categoria> = mutableListOf()
)

@Composable
fun Productes(
    //Llista de productes
    llista: Llista = Llista(),

    //Accio q prenem per a guardar la llista
    onGuardar: () -> Unit = {}
) {

    //variable per mostar formulari
    var mostrarFormulari by remember { mutableStateOf(false) }
    var nomProducte by remember { mutableStateOf("") }
    var nomCategoria by remember { mutableStateOf("") }

    //Variable per quan modifiquem la llista per actualitzar la vista
    var refrescar by remember { mutableIntStateOf(0) }

    //Mirem categoria per categoria quants productes ni ha, i els anem sumant.
    val totalProductes = llista.categories.sumOf { it.productes.size }

    //Mirem categoria per categoria quants productes estan completats, i els anem sumant.
    val completats = llista.categories.sumOf { it.productes.count { producte -> producte.completat } }

    //Calculem el proces de la barra --> dividirem els completats pels productes totals
    val progres = if (totalProductes > 0) completats.toFloat() / totalProductes else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        // Títol llista
        Text(
            text = llista.nom.ifBlank { "Llista" },
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Text del comptador de productes comprats
        Text(
            text = "Comprats: $completats de $totalProductes",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Barra visual de progrés
        LinearProgressIndicator(
            progress = { progres },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Fila amb els botons afegir productes i guardar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { mostrarFormulari = true },
                modifier = Modifier.weight(1f)
            ) {
                Text("+ Afegir producte")
            }

            Button(
                onClick = onGuardar,
                modifier = Modifier.weight(1f)
            ) {
                Text("Guardar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Quan refresquem..
        key(refrescar) {

            //Si no hi ha cap categoria, tampoc n'hi ha cap productes
            if (llista.categories.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hi ha cap producte.",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {

                //Mostrem la llista amb les categories i els productes
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(llista.categories) { categoria ->
                        Text(
                            text = categoria.nom,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                        )

                        categoria.productes.forEach { producte ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
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
                                        fontSize = 16.sp,
                                        textDecoration = if (producte.completat) TextDecoration.LineThrough else TextDecoration.None
                                    )
                                }

                                //Botons per eliminar producte, i si no hi ha cap producte, elimina categoria
                                IconButton(
                                    onClick = {
                                        categoria.productes.remove(producte)
                                        if (categoria.productes.isEmpty()) {
                                            llista.categories.remove(categoria)
                                        }
                                        refrescar++
                                    }
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
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
                        label = { Text("Nom del producte") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = nomCategoria,
                        onValueChange = { nomCategoria = it },
                        label = { Text("Categoria del producte") },
                        modifier = Modifier.fillMaxWidth()
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

//Vista previa
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductesPreview() {
    Productes()
}