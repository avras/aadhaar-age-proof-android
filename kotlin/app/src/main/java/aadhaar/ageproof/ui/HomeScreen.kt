package aadhaar.ageproof.ui

import aadhaar.ageproof.data.AgeProofUiState
import aadhaar.ageproof.ui.theme.AadhaarAgeProofTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    ageProofUiState: AgeProofUiState,
    generatePublicParameters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if (ageProofUiState.ppGenerated) {
            Text("Public parameters generated")
        } else {
            if (ageProofUiState.ppGenerationInProgress) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                Spacer(modifier.height(8.dp))
                Text("Public parameters generation in progress")
            } else {
                Button(onClick = generatePublicParameters, modifier = modifier.fillMaxWidth()) {
                    Text("Generate Parameters")
                }

            }
        }
        if (ageProofUiState.publicParameters.isEmpty()) {
            Text("Empty pp")

        } else {
            Text("Current value = " + ageProofUiState.publicParameters)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AadhaarAgeProofTheme {
        HomeScreen(
            ageProofUiState = AgeProofUiState(
                ppGenerationInProgress = false,
                ppGenerated = false,
            ),
            {},
            modifier = Modifier.padding(16.dp)
        )
    }
}