package com.example.riskbattlesimulator.ui

data class BattleResult(
    val remainingAttacker: Int,
    val remainingDefender: Int,
    val rounds: List<BattleRound>
)

data class BattleRound(
    val attackerDice: List<Int>,
    val defenderDice: List<Int>,
    val attackerOutcomes: List<Boolean>, // true = ganó, false = perdió/no participó
    val defenderOutcomes: List<Boolean>, // true = ganó, false = perdió/no participó
    val attackerLosses: Int,
    val defenderLosses: Int
)