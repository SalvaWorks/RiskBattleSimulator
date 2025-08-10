package com.example.riskbattlesimulator.ui

data class BattleResult(
    val remainingAttacker: Int,
    val remainingDefender: Int,
    val rounds: List<BattleRound>
)

data class BattleRound(
    val attackerDice: List<Int>,
    val defenderDice: List<Int>,
    val attackerLosses: Int,
    val defenderLosses: Int
)