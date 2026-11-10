# 🛒 Llista de la Compra - Android App

Aplicació mòbil desenvolupada en **Android Studio** utilitzant **Jetpack Compose** i **Firebase** per a la gestió de llistes de la compra en temps real.

---

## 🚀 Característiques principals

* **Autenticació d'usuaris:** Registre i inici de sessió mitjançar Firebase Auth.
* **Sincronització en temps real:** Les llistes es guarden i s'actualitzen a la base de dades Firestore a l'instant.
* **Llistes compartides:** Capacitat per compartir llistes de la compra amb altres usuaris mitjançant el seu correu electrònic.
* **Organització per categories:** Afegiment de productes classificats automàticament per categoria amb control d'estat (pendent / completat).
* **Navegació integrada:** Menú inferior adaptatiu (*NavigationSuiteScaffold*) per navegar fàcilment entre les seccions principals.

---

## 🛠️ Tecnologies utilitzades

* **Llenguatge:** Kotlin
* **Interfície d'usuari:** Jetpack Compose (Material3)
* **Base de dades:** Firebase Firestore
* **Autenticació:** Firebase Authentication
* **Arquitectura:** Composables adaptats a l'estat en temps real (`LaunchedEffect`, `remember`, `mutableStateOf`).

---

## 📁 Estructura del Projecte

* `MainActivity.kt`: Punt d'entrada de l'aplicació, gestió d'estat global de la sessió i menú de navegació principal.
* `LoginScreen.kt`: Pantalla d'inici de sessió i registre d'usuaris amb Firebase Auth.
* `IniciScreen.kt`: Pantalla principal per crear noves llistes ràpidament.
* `ListScreen.kt`: Vista de totes les llistes de l'usuari (pròpies i compartides) amb opció de compartir o eliminar.
* `Productes.kt`: Gestió dels productes d'una llista agrupats per categories i marcatge de tasques.
* `ProfileScreen.kt`: Vista de perfil d'usuari i opció per tancar la sessió.

---

## 📹 Demostració i Descàrregues

* **Vídeo explicatiu:** [Enllaç al vídeo de demostració](POSA_AQUI_EL_TEU_ENLLAÇ_DE_DRIVE_O_YOUTUBE)
* **APK de l'aplicació:** [Descarregar APK](POSA_AQUI_EL_TEU_ENLLAÇ_AL_FITXER_APK)
