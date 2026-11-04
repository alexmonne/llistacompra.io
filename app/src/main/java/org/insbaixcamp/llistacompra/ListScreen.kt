package org.insbaixcamp.llistacompra

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.insbaixcamp.llistacompra.ui.theme.LlistaCompraTheme

@Composable
fun ListScreen(
    llistes: List<Llista> = emptyList(),
    onObrirLlista: (Llista) -> Unit = {},
    onEliminarLlista: (Llista) -> Unit = {},
    onCompartirLlista: (Llista) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Les meves llistes", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        if (llistes.isEmpty()) {
            Text("No tens cap llista guardada.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(llistes) { llista ->
                    Card(
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
                            Text(
                                text = llista.nom,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Row {
                                // Botó Compartir
                                IconButton(onClick = { onCompartirLlista(llista) }) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Compartir llista"
                                    )
                                }
                                // Botó Eliminar
                                IconButton(onClick = { onEliminarLlista(llista) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar llista"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ListScreenPreview() {
    LlistaCompraTheme {
        ListScreen(
            llistes = listOf(
                Llista(nom = "Cita con el Kenai"),
                Llista(nom = "Supermercat")
            )
        )
    }
}