package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            TipCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    TipTimeLayout()
                }
            }
        }
    }
}

@Composable
fun TipTimeLayout() {

    // Montant de la facture
    var amountInput by remember { mutableStateOf("") }

    // Pourcentage du pourboire
    var tipInput by remember { mutableStateOf("") }

    // Permet de savoir si l'utilisateur veut arrondir le pourboire
    var roundUp by remember { mutableStateOf(false) }

    // Conversion du montant en Double
    val amount = amountInput.toDoubleOrNull() ?: 0.0

    // Conversion du pourcentage en Double
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0

    // Calcul du pourboire
    /*val tip = calculateTip(
        amount = amount,
        tipPercent = tipPercent,
        roundUp = roundUp
    )*/
    val tip = calculateTip(amount, tipPercent, roundUp)

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .safeDrawingPadding(),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Titre
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(
                    bottom = 16.dp,
                    top = 40.dp
                )
                .align(Alignment.Start)
        )

        // Champ pour le montant de la facture
        EditNumberField(
            label = R.string.bill_amount,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = amountInput,
            onValueChange = {
                amountInput = it
            },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        // Champ pour le pourcentage du pourboire
        EditNumberField(
            label = R.string.how_was_the_service,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = tipInput,
            onValueChange = {
                tipInput = it
            },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        // Ligne pour arrondir le pourboire
        RoundTheTipRow(
            roundUp = roundUp,
            onRoundUpChanged = {
                roundUp = it
            },
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Affichage du résultat
        Text(
            text = stringResource(R.string.tip_amount, tip),
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(
            modifier = Modifier.height(150.dp)
        )
    }
}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = true,
        label = {
            Text(
                stringResource(label)
            )
        },
        keyboardOptions = keyboardOptions
    )
}

@Composable
fun RoundTheTipRow(
    roundUp: Boolean,
    onRoundUpChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Texte
        Text(
            text = stringResource(R.string.round_up_tip)
        )

        // Bouton bascule
        Switch(
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        )
    }
}

/**
 * Calcule le pourboire à partir du montant
 * de la facture et du pourcentage choisi.
 *
 * Si roundUp = true, le pourboire est arrondi
 * à l'entier supérieur.
 */
private fun calculateTip(amount: Double, tipPercent: Double=15.0, roundUp: Boolean): String {

    // Calcul du pourboire
    var tip = tipPercent / 100 * amount

    // Arrondir si le bouton est activé
    if (roundUp) {
        tip = kotlin.math.ceil(tip)
    }

    // Formatage en monnaie
    return NumberFormat
        .getCurrencyInstance()
        .format(tip)
}

@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {
    TipCalculatorTheme {
        TipTimeLayout()
    }
}