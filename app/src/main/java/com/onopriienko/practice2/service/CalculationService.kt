package com.onopriienko.practice2.service

import com.onopriienko.practice2.enums.FuelType
import kotlin.math.pow

class CalculationService {

    fun calculateRateAndGrossEmissions(
        fuelType: FuelType,
        fuelAmount: Double,
    ): Pair<Double, Double> {
        when (fuelType) {
            FuelType.NATURAL_GAS -> {
                return 0.0 to 0.0
            }

            FuelType.COAL -> {
                val k = calculateEmissionRate(
                    lowestHeat = LOWEST_HEAT_COAL_MJ_PER_KG,
                    volatileAshFraction = COAL_VOLATILE_ASH_FRACTION,
                    ashAmountInWorkingMass = ASH_AMOUNT_IN_WORKING_MASS_COAL,
                    combustibleContentInEmissions = COMBUSTIBLE_CONTENT_IN_EMISSIONS_OF_COAL,
                )
                val grossEmissions = calculateGrossEmissions(
                    emissionRate = k,
                    lowestHeat = LOWEST_HEAT_COAL_MJ_PER_KG,
                    fuelAmount = fuelAmount,
                )
                return k to grossEmissions
            }

            FuelType.FUEL_OIL -> {
                val k = calculateEmissionRate(
                    lowestHeat = LOWEST_HEAT_FUEL_OIL_MJ_PER_KG,
                    volatileAshFraction = FUEL_OIL_VOLATILE_ASH_FRACTION,
                    ashAmountInWorkingMass = ASH_AMOUNT_IN_WORKING_MASS_FUEL_OIL,
                    combustibleContentInEmissions = COMBUSTIBLE_CONTENT_IN_EMISSIONS_OF_FUEL_OIL,
                )
                val grossEmissions = calculateGrossEmissions(
                    emissionRate = k,
                    lowestHeat = LOWEST_HEAT_FUEL_OIL_MJ_PER_KG,
                    fuelAmount = fuelAmount,
                )
                return k to grossEmissions
            }
        }
    }

    private fun calculateEmissionRate(
        lowestHeat: Double,
        volatileAshFraction: Double,
        ashAmountInWorkingMass: Double,
        combustibleContentInEmissions: Double,
    ): Double = (10.0.pow(6) / lowestHeat) *
            volatileAshFraction *
            (ashAmountInWorkingMass / (100 - combustibleContentInEmissions)) *
            (1 - FLUE_GAS_CLEANING_EFFICIENCY) +
            EMISSION_RATE_OF_SOLID_PRODUCTS_OF_INTERACTION

    private fun calculateGrossEmissions(
        emissionRate: Double,
        lowestHeat: Double,
        fuelAmount: Double,
    ): Double = 10.0.pow(-6) * emissionRate * lowestHeat * fuelAmount

    companion object {
        const val LOWEST_HEAT_COAL_MJ_PER_KG: Double = 20.47
        const val LOWEST_HEAT_FUEL_OIL_MJ_PER_KG: Double = 40.40

        const val COAL_VOLATILE_ASH_FRACTION: Double = 0.8
        const val FUEL_OIL_VOLATILE_ASH_FRACTION: Double = 1.0

        const val ASH_AMOUNT_IN_WORKING_MASS_COAL: Double = 25.20
        const val ASH_AMOUNT_IN_WORKING_MASS_FUEL_OIL: Double = 0.15

        const val FLUE_GAS_CLEANING_EFFICIENCY: Double = 0.985

        const val EMISSION_RATE_OF_SOLID_PRODUCTS_OF_INTERACTION: Double = 0.0

        const val COMBUSTIBLE_CONTENT_IN_EMISSIONS_OF_COAL: Double = 1.5
        const val COMBUSTIBLE_CONTENT_IN_EMISSIONS_OF_FUEL_OIL: Double = 0.0
    }
}
