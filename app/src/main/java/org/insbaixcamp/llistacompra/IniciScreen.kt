package org.insbaixcamp.llistacompra

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import org.insbaixcamp.llistacompra.ui.theme.LlistaCompraTheme

@Composable
fun IniciScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Benvingut/da!",
            fontSize = 33.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Gestiona i organitza les teves llistes de la compra d'una manera fàcil i ràpida.",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold

        )
        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = {  }) {
            Text(text = "Crear llista")
        }
//Text Crear llista
        //Boto crear llista
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    LlistaCompraTheme {
        IniciScreen()
    }
}
