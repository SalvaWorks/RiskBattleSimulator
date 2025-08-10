package com.example.riskbattlesimulator

import com.example.riskbattlesimulator.ui.theme.BattleResult
import kotlin.random.Random

class RiskSimulator {
    fun simulateBattle(
        initialAttacker: Int,
        initialDefender: Int,
        stopAt: Int = 1
    ): BattleResult {
        var attacker = initialAttacker
        var defender = initialDefender
        val attackerDice = mutableListOf<Int>()
        val defenderDice = mutableListOf<Int>()

        while (attacker > stopAt && defender > 0) {
            // Lanzar dados
            val attDice = rollDice(minOf(3, attacker - 1)).sortedDescending()
            val defDice = rollDice(minOf(2, defender)).sortedDescending()

            attackerDice.addAll(attDice)
            defenderDice.addAll(defDice)

            // Comparar dados
            val pairs = minOf(attDice.size, defDice.size)
            for (i in 0 until pairs) {
                if (attDice[i] > defDice[i]) {
                    defender--
                } else {
                    attacker--
                }
            }
        }

        return BattleResult(
            remainingAttacker = attacker,
            remainingDefender = defender,
            attackerDice = attackerDice,
            defenderDice = defenderDice
        )
    }

    private fun rollDice(count: Int): List<Int> {
        return List(count) { Random.nextInt(1, 7) }
    }
}