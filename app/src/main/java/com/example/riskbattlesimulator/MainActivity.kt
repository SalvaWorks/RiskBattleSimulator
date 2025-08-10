package com.example.riskbattlesimulator

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.riskbattlesimulator.ui.BattleResult
import com.example.riskbattlesimulator.ui.BattleRound
import com.example.riskbattlesimulator.ui.theme.RiskBattleSimulatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RiskBattleSimulatorTheme {
                val viewModel: RiskSimulatorViewModel = viewModel()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RiskSimulatorScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun RiskSimulatorScreen(viewModel: RiskSimulatorViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(modifier = Modifier.fillMaxSize()) {
        when (viewModel.currentScreen) {
            AppScreen.INPUT -> {
                InputScreen(
                    attackerTroops = viewModel.attackerTroops,
                    defenderTroops = viewModel.defenderTroops,
                    stopAtTroops = viewModel.stopAtTroops,
                    onAttackerChange = { viewModel.attackerTroops = it },
                    onDefenderChange = { viewModel.defenderTroops = it },
                    onStopAtChange = { viewModel.stopAtTroops = it },
                    onSimulate = {
                        keyboardController?.hide()
                        val attacker = viewModel.attackerTroops.toIntOrNull() ?: 0
                        val defender = viewModel.defenderTroops.toIntOrNull() ?: 0
                        val stopAt = viewModel.stopAtTroops.toIntOrNull() ?: 1

                        if (attacker > 1 && defender > 0) {
                            viewModel.battleResult = RiskSimulator().simulateBattle(attacker, defender, stopAt)
                            viewModel.currentScreen = AppScreen.RESULTS
                        }
                    }
                )
            }
            AppScreen.RESULTS -> {
                ResultsScreen(
                    battleResult = viewModel.battleResult,
                    onBack = { viewModel.currentScreen = AppScreen.INPUT }
                )
            }
        }
    }
}

@Composable
fun InputScreen(
    attackerTroops: String,
    defenderTroops: String,
    stopAtTroops: String,
    onAttackerChange: (String) -> Unit,
    onDefenderChange: (String) -> Unit,
    onStopAtChange: (String) -> Unit,
    onSimulate: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Risk Battle Simulator",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Bold,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.2f),
                            offset = Offset(2f, 2f),
                            blurRadius = 4f
                        ),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                )

                // Si tienes una imagen para mostrar
                /*
                Image(
                    painter = painterResource(R.drawable.risk_logo),
                    contentDescription = "Risk Logo",
                    modifier = Modifier.size(200.dp)
                )
                */
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NumberInputField(
                    value = attackerTroops,
                    onValueChange = onAttackerChange,
                    label = "Tropas atacantes"
                )
                Spacer(modifier = Modifier.height(16.dp))

                NumberInputField(
                    value = defenderTroops,
                    onValueChange = onDefenderChange,
                    label = "Tropas defensoras"
                )
                Spacer(modifier = Modifier.height(16.dp))

                NumberInputField(
                    value = stopAtTroops,
                    onValueChange = onStopAtChange,
                    label = "Plantar al llegar a"
                )
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onSimulate,
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp)
                ) {
                    Text("Simular Batalla", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Risk Battle Simulator",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.2f),
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    ),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            NumberInputField(
                value = attackerTroops,
                onValueChange = onAttackerChange,
                label = "Tropas atacantes"
            )
            Spacer(modifier = Modifier.height(8.dp))

            NumberInputField(
                value = defenderTroops,
                onValueChange = onDefenderChange,
                label = "Tropas defensoras"
            )
            Spacer(modifier = Modifier.height(8.dp))

            NumberInputField(
                value = stopAtTroops,
                onValueChange = onStopAtChange,
                label = "Plantar al llegar a"
            )
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onSimulate,
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
            ) {
                Text("Simular Batalla", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun ResultsScreen(
    battleResult: BattleResult?,
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text("Volver")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            battleResult?.let {
                BattleResultView(battleResult)
            } ?: run {
                Text("No hay resultados disponibles", style = MaterialTheme.typography.bodyLarge)
            }
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
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Columna para resultados finales
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Resultado final:",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )

                Text(
                    "Atacante: ${result.remainingAttacker} tropas",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFD32F2F)
                    )
                )

                Text(
                    "Defensor: ${result.remainingDefender} tropas",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1976D2)
                    )
                )
            }

            // Columna para detalles de rondas
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    "Detalle por rondas:",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                result.rounds.forEachIndexed { index, round ->
                    BattleRoundCard(
                        roundNumber = index + 1,
                        round = round,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Resultado final:",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )

            Text(
                "Atacante: ${result.remainingAttacker} tropas",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFD32F2F)
                )
            )

            Text(
                "Defensor: ${result.remainingDefender} tropas",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1976D2)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Detalle por rondas:",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                result.rounds.forEachIndexed { index, round ->
                    BattleRoundCard(
                        roundNumber = index + 1,
                        round = round
                    )
                }
            }
        }
    }
}

@Composable
fun BattleRoundCard(
    roundNumber: Int,
    round: BattleRound,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.95f)
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Ronda $roundNumber",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                textAlign = TextAlign.Center
            )

            DiceRow(
                label = "Atacante",
                dice = round.attackerDice,
                outcomes = round.attackerOutcomes,
                maxDice = 3,
                baseColor = Color(0xFFD32F2F)
            )
            Spacer(modifier = Modifier.height(12.dp))

            DiceRow(
                label = "Defensor",
                dice = round.defenderDice,
                outcomes = round.defenderOutcomes,
                maxDice = 2,
                baseColor = Color(0xFF1976D2)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        "Atacante pierde: ${round.attackerLosses}",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFD32F2F),
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        "Defensor pierde: ${round.defenderLosses}",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF1976D2),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun DiceRow(
    label: String,
    dice: List<Int>,
    outcomes: List<Boolean>,
    maxDice: Int,
    baseColor: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            "$label:",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = baseColor
            ),
            modifier = Modifier.width(100.dp)
        )

        val diceSize = if (maxDice == 3) 40.dp else 48.dp

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            dice.forEachIndexed { index, value ->
                DiceIcon(
                    value = value,
                    color = baseColor,
                    outcome = outcomes.getOrNull(index),
                    size = diceSize
                )
            }
            for (i in dice.size until maxDice) {
                EmptyDiceIcon(size = diceSize)
            }
        }
    }
}

@Composable
fun DiceIcon(
    value: Int,
    color: Color,
    outcome: Boolean?,
    size: Dp
) {
    val displayColor = when (outcome) {
        true -> Color(0xFFD32F2F)
        false -> Color(0xFF388E3C)
        null -> color.copy(alpha = 0.5f)
    }

    val imageRes = when (value) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        6 -> R.drawable.dice_6
        else -> R.drawable.dice_1
    }

    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(size),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Dado $value",
            modifier = Modifier.size(size * 0.85f)
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .border(
                    width = 2.dp,
                    color = displayColor,
                    shape = MaterialTheme.shapes.medium
                )
        )
    }
}

@Composable
fun EmptyDiceIcon(size: Dp) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(size),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.dice_empty),
            contentDescription = "Dado vacío",
            modifier = Modifier.size(size * 0.85f),
            colorFilter = ColorFilter.tint(Color.LightGray.copy(alpha = 0.5f))
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .border(
                    width = 2.dp,
                    color = Color.LightGray.copy(alpha = 0.3f),
                    shape = MaterialTheme.shapes.medium
                )
        )
    }
}