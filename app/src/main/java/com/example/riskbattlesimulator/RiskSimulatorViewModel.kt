package com.example.riskbattlesimulator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.riskbattlesimulator.ui.BattleResult

enum class AppScreen {
    INPUT, RESULTS
}

class RiskSimulatorViewModel : ViewModel() {
    var currentScreen by mutableStateOf(AppScreen.INPUT)
    var battleResult by mutableStateOf<BattleResult?>(null)

    var attackerTroops by mutableStateOf("10")
    var defenderTroops by mutableStateOf("6")
    var stopAtTroops by mutableStateOf("1")
}