package com.example.riskbattlesimulator

import com.example.riskbattlesimulator.ui.BattleResult
import com.example.riskbattlesimulator.ui.BattleRound
import kotlin.random.Random

class RiskSimulator {
    fun simulateBattle(
        initialAttacker: Int,
        initialDefender: Int,
        stopAt: Int = 1
    ): BattleResult {
        var attacker = initialAttacker
        var defender = initialDefender
        val rounds = mutableListOf<BattleRound>()

        while (attacker > stopAt && defender > 0) {
            val attDice = rollDice(minOf(3, attacker - 1)).sortedDescending()
            val defDice = rollDice(minOf(2, defender)).sortedDescending()

            val (attLosses, defLosses) = compareDice(attDice, defDice)

            rounds.add(BattleRound(
                attackerDice = attDice,
                defenderDice = defDice,
                attackerLosses = attLosses,
                defenderLosses = defLosses
            ))

            attacker -= attLosses
            defender -= defLosses
        }

        return BattleResult(
            remainingAttacker = attacker,
            remainingDefender = defender,
            rounds = rounds
        )
    }

    private fun compareDice(
        attDice: List<Int>,
        defDice: List<Int>
    ): Pair<Int, Int> {
        var attLosses = 0
        var defLosses = 0

        val pairs = minOf(attDice.size, defDice.size)
        for (i in 0 until pairs) {
            if (attDice[i] > defDice[i]) {
                defLosses++
            } else {
                // Defensor gana en empates
                attLosses++
            }
        }

        return Pair(attLosses, defLosses)
    }

    private fun rollDice(count: Int): List<Int> {
        return List(count) { Random.nextInt(1, 7) }
    }
}