package com.example.riskbattlesimulator

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
import com.example.riskbattlesimulator.ui.theme.BattleResult
import com.example.riskbattlesimulator.ui.theme.RiskBattleSimulatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RiskBattleSimulatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RiskSimulatorScreen()
                }
            }
        }
    }
}

@Composable
fun RiskSimulatorScreen() {
    var attackerTroops by remember { mutableStateOf("") }
    var defenderTroops by remember { mutableStateOf("") }
    var stopAtTroops by remember { mutableStateOf("1") }  // Valor por defecto 1

    var result by remember { mutableStateOf<BattleResult?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Risk Battle Simulator", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        // Inputs
        NumberInputField(
            value = attackerTroops,
            onValueChange = { attackerTroops = it },
            label = "Tropas atacantes"
        )

        Spacer(modifier = Modifier.height(8.dp))

        NumberInputField(
            value = defenderTroops,
            onValueChange = { defenderTroops = it },
            label = "Tropas defensoras"
        )

        Spacer(modifier = Modifier.height(8.dp))

        NumberInputField(
            value = stopAtTroops,
            onValueChange = { stopAtTroops = it },
            label = "Plantar al llegar a"
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val attacker = attackerTroops.toIntOrNull() ?: 0
                val defender = defenderTroops.toIntOrNull() ?: 0
                val stopAt = stopAtTroops.toIntOrNull() ?: 1

                if (attacker > 1 && defender > 0) {
                    result = RiskSimulator().simulateBattle(attacker, defender, stopAt)
                }
            },
            modifier = Modifier.width(200.dp)
        ) {
            Text("Simular Batalla")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Resultados
        result?.let { battleResult ->
            BattleResultView(battleResult)
        }
    }
}

@Composable
fun NumberInputField(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.isEmpty() || newValue.toIntOrNull() != null) {
                onValueChange(newValue)
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}

@Composable
fun BattleResultView(result: BattleResult) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Resultado:", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Atacante: ${result.remainingAttacker} tropas", style = MaterialTheme.typography.bodyLarge)
        Text("Defensor: ${result.remainingDefender} tropas", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Dados atacante: ${result.attackerDice.joinToString()}")
        Text("Dados defensor: ${result.defenderDice.joinToString()}")
    }
}