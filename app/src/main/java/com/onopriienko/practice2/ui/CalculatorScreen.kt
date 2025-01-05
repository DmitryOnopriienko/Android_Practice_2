package com.onopriienko.practice2.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.onopriienko.practice2.enums.FuelType
import com.onopriienko.practice2.service.CalculationService
import com.onopriienko.practice2.ui.component.DropdownSelector
import java.util.Locale

@Composable
fun CalculatorScreen() {
    Column {
        var selectedFuelType by remember { mutableStateOf<FuelType?>(null) }
        var fuelAmount by remember { mutableStateOf("") }
        var fuelAmountLabel by remember { mutableStateOf("") }
        var kResult by remember { mutableStateOf("") }
        var eResult by remember { mutableStateOf("") }

        val calculationService = CalculationService()

        fuelAmountLabel = when (selectedFuelType) {
            FuelType.COAL, FuelType.FUEL_OIL -> ", тонн"
            FuelType.NATURAL_GAS -> ", метрів кубічних"
            else -> ""
        }

        DropdownSelector(selectedFuelType) { selectedFuelType = it }
        TextField(
            value = fuelAmount,
            label = { Text("Кількість палива$fuelAmountLabel") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {
                if (it.isEmpty()) {
                    fuelAmount = it
                } else {
                    fuelAmount = when (it.toDoubleOrNull()) {
                        null -> fuelAmount
                        else -> it
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Button(
            enabled = (selectedFuelType != null && fuelAmount.isNotBlank()),
            onClick = {
                val (k, e) = calculationService.calculateRateAndGrossEmissions(
                    fuelType = requireNotNull(selectedFuelType),
                    fuelAmount = fuelAmount.toDouble()
                )
                kResult = String.format(Locale.getDefault(), "%.4f", k)
                eResult = String.format(Locale.getDefault(), "%.4f", e)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("Розрахувати")
        }
        Text(
            "Показник емісії твердих частинок, г/ГДж: $kResult",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Text(
            "Валовий викид, т: $eResult",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
