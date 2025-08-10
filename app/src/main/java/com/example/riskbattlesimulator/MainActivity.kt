package com.example.riskbattlesimulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        singleLine = true,
        modifier = Modifier.fillMaxWidth(0.8f)
    )
}

@Composable
fun BattleResultView(result: BattleResult) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Resultado final:", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Tropas restantes
        Text("Atacante: ${result.remainingAttacker} tropas",
            style = MaterialTheme.typography.bodyLarge)
        Text("Defensor: ${result.remainingDefender} tropas",
            style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(24.dp))

        // Dados agrupados por ronda
        val attackerGroups = result.attackerDice.chunked(3)
        val defenderGroups = result.defenderDice.chunked(2)
        val maxRounds = maxOf(attackerGroups.size, defenderGroups.size)

        Text("Detalle por rondas:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        for (round in 0 until maxRounds) {
            BattleRoundCard(
                roundNumber = round + 1,
                attackerDice = if (round < attackerGroups.size) attackerGroups[round] else emptyList(),
                defenderDice = if (round < defenderGroups.size) defenderGroups[round] else emptyList()
            )
        }
    }
}

@Composable
fun BattleRoundCard(roundNumber: Int, attackerDice: List<Int>, defenderDice: List<Int>) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Ronda $roundNumber",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary)

            Spacer(modifier = Modifier.height(12.dp))

            // Dados atacante
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text("Atacante:",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.width(80.dp))
                Row {
                    attackerDice.forEach { dice ->
                        DiceIcon(value = dice, color = Color(0xFFD32F2F)) // Rojo
                    }
                    // Rellenar espacios si hay menos de 3 dados
                    for (i in attackerDice.size until 3) {
                        EmptyDiceIcon()
                    }
                }
            }

            // Dados defensor
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Defensor:",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.width(80.dp))
                Row {
                    defenderDice.forEach { dice ->
                        DiceIcon(value = dice, color = Color(0xFF1976D2)) // Azul
                    }
                    // Rellenar espacios si hay menos de 2 dados
                    for (i in defenderDice.size until 2) {
                        EmptyDiceIcon()
                    }
                }
            }
        }
    }
}

@Composable
fun DiceIcon(value: Int, color: Color) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(4.dp)
            .background(
                color.copy(alpha = 0.1f),
                MaterialTheme.shapes.medium
            )
            .border(
                width = 1.dp,
                color = color.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.medium
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value.toString(),
            color = color,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun EmptyDiceIcon() {
    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(4.dp)
            .background(
                Color.LightGray.copy(alpha = 0.1f),
                MaterialTheme.shapes.medium
            )
            .border(
                width = 1.dp,
                color = Color.LightGray.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.medium
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "-",
            color = Color.LightGray,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}