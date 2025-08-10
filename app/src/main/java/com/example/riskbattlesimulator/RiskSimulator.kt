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

        // Continuar hasta que el atacante alcance el límite o el defensor sea eliminado
        while (attacker > stopAt && defender > 0) {
            val attDiceCount = minOf(3, attacker - 1)
            val defDiceCount = minOf(2, defender)

            val attDice = rollDice(attDiceCount).sortedDescending()
            val defDice = rollDice(defDiceCount).sortedDescending()

            val (attOutcomes, defOutcomes, attLosses, defLosses) = compareDice(attDice, defDice)

            rounds.add(
                BattleRound(
                    attackerDice = attDice,
                    defenderDice = defDice,
                    attackerOutcomes = attOutcomes,
                    defenderOutcomes = defOutcomes,
                    attackerLosses = attLosses,
                    defenderLosses = defLosses
                )
            )

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
    ): Quadruple<List<Boolean>, List<Boolean>, Int, Int> {
        val attOutcomes = MutableList(attDice.size) { false }
        val defOutcomes = MutableList(defDice.size) { false }
        var attLosses = 0
        var defLosses = 0

        val pairs = minOf(attDice.size, defDice.size)
        for (i in 0 until pairs) {
            if (attDice[i] > defDice[i]) {
                // Atacante gana
                defOutcomes[i] = true
                defLosses++
            } else {
                // Defensor gana (incluye empates)
                attOutcomes[i] = true
                attLosses++
            }
        }

        return Quadruple(attOutcomes, defOutcomes, attLosses, defLosses)
    }

    private fun rollDice(count: Int): List<Int> {
        return List(count) { Random.nextInt(1, 7) }
    }

    // Clase de utilidad para retornar 4 valores
    data class Quadruple<out A, out B, out C, out D>(
        val first: A,
        val second: B,
        val third: C,
        val fourth: D
    )
}