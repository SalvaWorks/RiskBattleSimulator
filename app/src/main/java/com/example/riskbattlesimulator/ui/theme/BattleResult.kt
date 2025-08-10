package com.example.riskbattlesimulator.ui.theme

data class BattleResult(
    val remainingAttacker: Int,
    val remainingDefender: Int,
    val attackerDice: List<Int>,
    val defenderDice: List<Int>
)