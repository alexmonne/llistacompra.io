package org.insbaixcamp.llistacompra

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
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

//Aqui veurem les llistes creades i tambe compartides
@Composable
fun ListScreen(
    llistes: List<Llista> = emptyList(),
    onObrirLlista: (Llista) -> Unit = {},
    onEliminarLlista: (Llista) -> Unit = {},
    onCompartirLlista: (Llista, String) -> Unit = { _, _ -> }
) {
    //Variables per la llista a compartir i tambe per l'email amb el qual el volem compartir
    var llistaACompartir by remember { mutableStateOf<Llista?>(null) }
    var emailACompartir by remember { mutableStateOf("") }

    // Pantalla Principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        //Titol
        Text(
            text = "Les meves llistes",
            fontSize = 33.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        //Si no tenim cap llista creada ens ensenyara un missatge de que no tenim llista sino ens mostrara el llistat
        if (llistes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            )
            {
                Text(
                    text = "No tens cap llista guardada.",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            //Llistat de targetas
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(llistes) { llista ->
                    Card(
                        //Funcio per obrir la llista
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onObrirLlista(llista) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            //Nom de la llista
                            Text(
                                text = llista.nom,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            //Botons per compartir i eliminar llista
                            Row {
                                IconButton(onClick = { llistaACompartir = llista }) {
                                    Icon(Icons.Default.Share, contentDescription = "Compartir")
                                }
                                IconButton(onClick = { onEliminarLlista(llista) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Finestra per escriure el emial de la persona que vols compartir
    if (llistaACompartir != null) {
        AlertDialog(
            onDismissRequest = { llistaACompartir = null },
            title = { Text("Compartir llista") },
            text = {
                OutlinedTextField(
                    value = emailACompartir,
                    onValueChange = { emailACompartir = it },
                    label = { Text("Email de la persona") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (emailACompartir.isNotBlank()) {
                            onCompartirLlista(llistaACompartir!!, emailACompartir)
                            emailACompartir = ""
                            llistaACompartir = null
                        }
                    }
                ) {
                    //Esta compartit
                    Text("Compartir")
                }
            },
            dismissButton = {
                TextButton(onClick = { llistaACompartir = null }) {
                    Text("Cancel·lar")
                }
            }
        )
    }
}
//Vista prèvia
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ListScreenPreview() {
    LlistaCompraTheme {
        ListScreen()
    }
}