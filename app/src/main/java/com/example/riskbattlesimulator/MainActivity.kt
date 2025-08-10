package com.example.riskbattlesimulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.riskbattlesimulator.ui.BattleResult
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
    var stopAtTroops by remember { mutableStateOf("1") }

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
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Resultado final:", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Atacante: ${result.remainingAttacker} tropas",
            style = MaterialTheme.typography.bodyLarge)
        Text("Defensor: ${result.remainingDefender} tropas",
            style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Text("Detalle por rondas:", style = MaterialTheme.typography.titleMedium)

        // Contenedor desplazable con peso (weight) para ocupar espacio disponible
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
                .padding(vertical = 8.dp)
        ) {
            result.rounds.forEachIndexed { index, round ->
                BattleRoundCard(
                    roundNumber = index + 1,
                    attackerDice = round.attackerDice,
                    defenderDice = round.defenderDice,
                    attackerLosses = round.attackerLosses,
                    defenderLosses = round.defenderLosses
                )
            }
        }
    }
}

@Composable
fun BattleRoundCard(
    roundNumber: Int,
    attackerDice: List<Int>,
    defenderDice: List<Int>,
    attackerLosses: Int,
    defenderLosses: Int
) {
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
            DiceRow(
                label = "Atacante",
                dice = attackerDice,
                losses = attackerLosses,
                maxDice = 3,
                color = Color(0xFFD32F2F) // Rojo
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Dados defensor
            DiceRow(
                label = "Defensor",
                dice = defenderDice,
                losses = defenderLosses,
                maxDice = 2,
                color = Color(0xFF1976D2) // Azul
            )
        }
    }
}

@Composable
fun DiceRow(
    label: String,
    dice: List<Int>,
    losses: Int,
    maxDice: Int,
    color: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("$label:",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(80.dp))
        Row {
            dice.sortedDescending().forEachIndexed { index, value ->
                val isWinner = index >= losses
                DiceIcon(
                    value = value,
                    color = color,
                    isWinner = isWinner
                )
            }
            // Rellenar espacios vacíos
            for (i in dice.size until maxDice) {
                EmptyDiceIcon()
            }
        }
    }
}

@Composable
fun DiceIcon(value: Int, color: Color, isWinner: Boolean) {
    val diceColor = if (isWinner) Color(0xFF388E3C) else color // Verde si gana, color original si pierde
    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(4.dp)
            .background(
                diceColor.copy(alpha = 0.1f),
                MaterialTheme.shapes.medium
            )
            .border(
                width = 1.dp,
                color = diceColor,
                shape = MaterialTheme.shapes.medium
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value.toString(),
            color = diceColor,
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