package org.insbaixcamp.llistacompra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import org.insbaixcamp.llistacompra.ui.theme.LlistaCompraTheme

// ============================================================================
// ACTIVITAT PRINCIPAL: Punt d'entrada de l'aplicació
// ============================================================================
class MainActivity : ComponentActivity() {
    private lateinit var analitiques: FirebaseAnalytics
    private lateinit var autenticacio: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        analitiques = Firebase.analytics
        autenticacio = Firebase.auth

        enableEdgeToEdge()

        setContent {
            LlistaCompraTheme {
                // Variable que guarda l'usuari actual autenticat
                var usuariActual by remember { mutableStateOf<FirebaseUser?>(autenticacio.currentUser) }

                // CONTROL DE FLUX:
                if (usuariActual == null) {
                    LoginScreen(onLoginSuccess = { usuari -> usuariActual = usuari })
                } else {
                    LlistaCompraApp(
                        onTancarSessio = { usuariActual = null }
                    )
                }
            }
        }
    }
}

// ============================================================================
// PANTALLA PRINCIPAL DE L'APP
// ============================================================================
@Composable
fun LlistaCompraApp(onTancarSessio: () -> Unit = {}) { // Declara la funció i rep una acció per tancar sessió

    // MEMÒRIA DE LA PANTALLA:
    // 'destinacioActual' guarda quina pestanya està seleccionada.
    // 'mutableStateOf' fa que la pantalla es redibuixi sola quan canvia aquesta variable.
    // 'rememberSaveable' fa que el valor no es perdi quan es gira el mòbil o es canvia d'orientació.
    var destinacioActual by rememberSaveable { mutableStateOf(DestinacionsApp.INICI) }

    // ESTRUCTURA ADAPTATIVA DE MATERIAL 3:
    // Crea automàticament la barra de navegació (inferior en mòbils, menú lateral en tauletes)
    NavigationSuiteScaffold(
        navigationSuiteItems = { // Bloc on es defineixen els botons del menú

            // Recorre cadascuna de les opcions de l'enum (INICI, FAVORITS, PERFIL)
            DestinacionsApp.entries.forEach { destinacio ->

                // Crea un botó individual per a la pestanya del bucle
                item(
                    icon = { // Dibuixa la icona del botó
                        Icon(
                            painterResource(destinacio.icona), // Carrega la imatge/icona des de la carpeta res/drawable
                            contentDescription = destinacio.etiqueta // Text d'accessibilitat per a lectors de pantalla
                        )
                    },

                    label = { Text(destinacio.etiqueta) }, // Dibuixa el text visible a sota de la icona ("Inici", etc.)

                    //"Aquesta pestanya que estic dibuixant (destinacio) és la mateixa on es troba l'usuari ara mateix (destinacioActual)?"
                    selected = destinacio == destinacioActual, // Diu a Compose si aquest botó ha d'estar remarcat com a actiu

                    //"Agafa la pantalla que acaba de tocar (destinacio) i guarda-la com a la nova pantalla activa
                    onClick = { destinacioActual = destinacio } // En fer clic, canvia la pestanya activa a la seleccionada
                )
            }
        }
    ) { // Contingut principal de la pantalla (el que queda fora del menú)

        // 'Scaffold' dona el disseny base i calcula els marges segurs de la pantalla
        Scaffold(modifier = Modifier.fillMaxSize()) { margeIntern -> // Ocupa tota la pantalla ('fillMaxSize')

            // 'Box' és un contenidor on s'aplica el marge 'margeIntern' perquè el contingut no quedi amagat sota el menú
            Box(modifier = Modifier.padding(margeIntern)) {

                // CANVI D'INTERFÍCIE (Controlador de vistes):
                // Avalua quina pestanya està guardada a 'destinacioActual' i mostra el seu contingut
                when (destinacioActual) {
                    DestinacionsApp.INICI -> Text(text = "Pantalla d'Inici") // Mostra la vista d'Inici
                    DestinacionsApp.FAVORITS -> Text(text = "Favorits") // Mostra la vista de Favorits
                    DestinacionsApp.PERFIL -> ProfileScreen(onSignOut = onTancarSessio) // Obre la pantalla de perfil i li passa la funció per desconnectar-se
                }
            }
        }
    }
}

// ============================================================================
// LLISTA DE DESTINACIONS (PESTANYES)
// ============================================================================
enum class DestinacionsApp(
    val etiqueta: String,
    val icona: Int,
) {
    INICI("Inici", R.drawable.ic_home),
    FAVORITS("Favorits", R.drawable.ic_favorite),
    PERFIL("Perfil", R.drawable.ic_account_box),
}

// ============================================================================
// VISTA PRÈVIA
// ============================================================================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LlistaPreview() {
    LlistaCompraTheme {
        LlistaCompraApp()
    }
}