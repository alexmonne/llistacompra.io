package org.insbaixcamp.llistacompra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import org.insbaixcamp.llistacompra.ui.theme.LlistaCompraTheme

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
                var usuariActual by remember { mutableStateOf<FirebaseUser?>(autenticacio.currentUser) }

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

@Composable
fun LlistaCompraApp(onTancarSessio: () -> Unit = {}) {
    var destinacioActual by rememberSaveable { mutableStateOf(DestinacionsApp.INICI) }

    // Guarda totes les llistes creades
    val mevesLlistes = remember { mutableStateListOf<Llista>() }
    // Guarda la llista oberta en aquest moment
    var llistaSeleccionada by remember { mutableStateOf<Llista?>(null) }

    // SI ESTEM EDITANT UNA LLISTA: Mostrem Productes
    if (llistaSeleccionada != null) {
        Productes(
            llista = llistaSeleccionada!!,
            onGuardar = {
                llistaSeleccionada = null
                destinacioActual = DestinacionsApp.LLISTES // Redirigeix a "Llistes"
            }
        )
    } else {
        // SI NO: Mostrem el menú normal de l'aplicació
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                DestinacionsApp.entries.forEach { destinacio ->
                    item(
                        icon = {
                            Icon(
                                painterResource(destinacio.icona),
                                contentDescription = destinacio.etiqueta,
                                modifier = Modifier.size(32.dp)
                            )
                        },
                        label = { Text(destinacio.etiqueta) },
                        selected = destinacio == destinacioActual,
                        onClick = { destinacioActual = destinacio }
                    )
                }
            }
        ) {
            Scaffold(modifier = Modifier.fillMaxSize()) { margeIntern ->
                Box(modifier = Modifier.padding(margeIntern)) {
                    when (destinacioActual) {
                        DestinacionsApp.INICI -> IniciScreen(
                            onCrearLlista = { nomEscrit ->
                                val nova = Llista(nom = nomEscrit)
                                mevesLlistes.add(nova)
                                llistaSeleccionada = nova
                            }
                        )
                        DestinacionsApp.LLISTES -> ListScreen(
                            llistes = mevesLlistes,
                            onObrirLlista = { llistaTocada ->
                                llistaSeleccionada = llistaTocada
                            },
                            onEliminarLlista = { llistaAEliminar ->
                                mevesLlistes.remove(llistaAEliminar)
                            },
                            onCompartirLlista = { llistaACompartir ->
                                // Lògica futura per compartir
                            }
                        )
                        DestinacionsApp.PERFIL -> ProfileScreen(onSignOut = onTancarSessio)
                    }
                }
            }
        }
    }
}

enum class DestinacionsApp(
    val etiqueta: String,
    val icona: Int,
) {
    INICI("Inici", R.drawable.ic_home),
    LLISTES("Llistes", android.R.drawable.btn_star_big_off),
    PERFIL("Perfil", R.drawable.ic_account_box),
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LlistaPreview() {
    LlistaCompraTheme {
        LlistaCompraApp()
    }
}