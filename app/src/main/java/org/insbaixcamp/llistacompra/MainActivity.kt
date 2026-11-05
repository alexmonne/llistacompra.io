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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import org.insbaixcamp.llistacompra.ui.theme.LlistaCompraTheme

class MainActivity : ComponentActivity() {
    private lateinit var autenticacio: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autenticacio = Firebase.auth
        enableEdgeToEdge()

        setContent {
            LlistaCompraTheme {
                var usuariActual by remember { mutableStateOf<FirebaseUser?>(autenticacio.currentUser) }

                if (usuariActual == null) {
                    LoginScreen(onLoginSuccess = { usuari -> usuariActual = usuari })
                } else {
                    LlistaCompraApp(
                        usuariEmail = usuariActual?.email ?: "",
                        onTancarSessio = { usuariActual = null }
                    )
                }
            }
        }
    }
}

@Composable
fun LlistaCompraApp(
    usuariEmail: String = "",
    onTancarSessio: () -> Unit = {}
) {
    // Comprovem si estem a la vista prèvia de l'Android Studio
    val isPreview = LocalInspectionMode.current
    val db = if (!isPreview) Firebase.firestore else null

    var destinacioActual by rememberSaveable { mutableStateOf(DestinacionsApp.INICI) }

    val mevesLlistes = remember { mutableStateListOf<Llista>() }
    var llistaSeleccionada by remember { mutableStateOf<Llista?>(null) }

    // --- CARREGAR LLISTES DE FIRESTORE ---
    LaunchedEffect(usuariEmail) {
        if (!isPreview && usuariEmail.isNotBlank()) {
            db?.collection("llistes")
                ?.whereArrayContains("usuarisCompartits", usuariEmail)
                ?.addSnapshotListener { snapshot, _ ->
                    if (snapshot != null) {
                        mevesLlistes.clear()
                        for (doc in snapshot.documents) {
                            val llista = doc.toObject(Llista::class.java)
                            if (llista != null) {
                                mevesLlistes.add(llista)
                            }
                        }
                    }
                }
        }
    }

    // --- PANTALLA DE DETALL DELS PRODUCTES ---
    if (llistaSeleccionada != null) {
        Productes(
            llista = llistaSeleccionada!!,
            onGuardar = {
                val llista = llistaSeleccionada!!

                if (db != null) {
                    if (llista.id.isBlank()) {
                        llista.id = db.collection("llistes").document().id
                        llista.usuarisCompartits = mutableListOf(usuariEmail)
                    } else if (!llista.usuarisCompartits.contains(usuariEmail)) {
                        llista.usuarisCompartits.add(usuariEmail)
                    }

                    db.collection("llistes").document(llista.id).set(llista)
                }

                llistaSeleccionada = null
                destinacioActual = DestinacionsApp.LLISTES
            }
        )
    }
    // --- PANTALLES PRINCIPALS AMB NAVEGACIÓ ---
    else {
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
                                llistaSeleccionada = Llista(nom = nomEscrit)
                            }
                        )
                        DestinacionsApp.LLISTES -> ListScreen(
                            llistes = mevesLlistes,
                            onObrirLlista = { llistaTocada ->
                                llistaSeleccionada = llistaTocada
                            },
                            onEliminarLlista = { llistaAEliminar ->
                                if (llistaAEliminar.id.isNotBlank()) {
                                    db?.collection("llistes")?.document(llistaAEliminar.id)?.delete()
                                }
                            },
                            onCompartirLlista = { llista, emailNou ->
                                db?.collection("llistes")?.document(llista.id)
                                    ?.update("usuarisCompartits", FieldValue.arrayUnion(emailNou))
                            }
                        )
                        DestinacionsApp.PERFIL -> ProfileScreen(onSignOut = onTancarSessio)
                    }
                }
            }
        }
    }
}

// --- MENÚ DE NAVEGACIÓ ---
enum class DestinacionsApp(
    val etiqueta: String,
    val icona: Int
) {
    INICI("Inici", R.drawable.ic_home),
    LLISTES("Llistes", android.R.drawable.btn_star_big_off),
    PERFIL("Perfil", R.drawable.ic_account_box)
}

// --- VISTA PRÈVIA ---
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LlistaPreview() {
    LlistaCompraTheme {
        LlistaCompraApp()
    }
}