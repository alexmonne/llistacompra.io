package org.insbaixcamp.llistacompra

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.insbaixcamp.llistacompra.ui.theme.LlistaCompraTheme

@Composable
fun ListScreen() {
    // Llista buida per defecte
    val listas = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Títol
        Text(text = "Mis listas", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Si la llista està buida, mostrem el missatge
        if (listas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Encara no tens cap llista",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            // Quan en creem, es mostraran automàticament aquí
            listas.forEach { nombre ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = nombre, fontWeight = FontWeight.Medium)

                        Row {
                            IconButton(onClick = {}) { Icon(Icons.Default.Edit, contentDescription = null) }
                            IconButton(onClick = {}) { Icon(Icons.Default.Share, contentDescription = null) }
                            IconButton(onClick = {}) { Icon(Icons.Default.Delete, contentDescription = null) }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListScreenPreview() {
    LlistaCompraTheme {
        ListScreen()
    }
}