package com.example.umrechnungsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlterInMinutenApp()
        }
    }
}

@Composable
fun AlterInMinutenApp() {
    // Speichert die Eingabe des Users
    var geburtsdatumText by remember { mutableStateOf("") }

    // Speichert das Ergebnis oder eine Fehlermeldung
    var ergebnis by remember { mutableStateOf("") }

    // Datumsformat: Tag.Monat.Jahr
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Alter in Minuten",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(20.dp))

                TextField(
                    value = geburtsdatumText,
                    onValueChange = { geburtsdatumText = it },
                    label = { Text("Geburtsdatum, z. B. 25.04.2005") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        try {
                            // Text in ein echtes Datum umwandeln
                            val geburtsdatum = LocalDate.parse(geburtsdatumText, formatter)
                            val heute = LocalDate.now()

                            // Prüfen, ob Datum in der Zukunft liegt
                            if (geburtsdatum.isAfter(heute)) {
                                ergebnis = "Fehler: Das Geburtsdatum darf nicht in der Zukunft liegen."
                            } else {
                                // Alter in Jahren berechnen
                                val alter = Period.between(geburtsdatum, heute).years

                                // Minuten seit Geburt berechnen
                                val geburtStart = geburtsdatum.atStartOfDay()
                                val jetzt = LocalDateTime.now()
                                val minuten = ChronoUnit.MINUTES.between(geburtStart, jetzt)

                                ergebnis = "Du bist ca. $alter Jahre alt.\n" +
                                        "Das entspricht ca. %,d Minuten.".format(minuten)
                            }
                        } catch (e: Exception) {
                            ergebnis = "Fehler: Bitte gib ein gültiges Datum im Format TT.MM.JJJJ ein."
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Berechnen")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = ergebnis,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}