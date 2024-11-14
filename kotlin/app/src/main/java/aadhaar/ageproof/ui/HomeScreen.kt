package aadhaar.ageproof.ui

import aadhaar.ageproof.data.AgeProofUiState
import aadhaar.ageproof.ui.theme.AadhaarAgeProofTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.math.RoundingMode

@Composable
fun HomeScreen(
    ageProofUiState: AgeProofUiState,
    generatePublicParameters: () -> Unit,
    resetPublicParameters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth(),
    ) {
        Button(
            onClick = generatePublicParameters,
            enabled = !ageProofUiState.ppGenerationInProgress,
            modifier = modifier.fillMaxWidth()
        ) {
            Text("Generate Parameters")
        }
        if (ageProofUiState.ppGenerated) {
            Button(onClick = resetPublicParameters, modifier = modifier.fillMaxWidth()) {
                Text("Reset Public Parameters")
            }
            Text("Public parameters generated in ${ageProofUiState.ppGenerationTime.inWholeSeconds} sec")

            val ppSizeInMB =
                (ageProofUiState.publicParameters.size / (1024.0 * 1024.0)).toBigDecimal()
                    .setScale(2, RoundingMode.HALF_UP).toDouble()
            Text("Parameters size = $ppSizeInMB")
        } else {
            if (ageProofUiState.ppGenerationInProgress) {
                Text("Public parameters generation in progress")
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AadhaarAgeProofTheme {
        HomeScreen(
            ageProofUiState = AgeProofUiState(
                ppGenerationInProgress = true,
                ppGenerated = false,
            ),
            {},
            {},
            modifier = Modifier.padding(16.dp)
        )
    }
}