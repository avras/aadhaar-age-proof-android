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
        if (ageProofUiState.ppGenerated) {
            Text("Public parameters generated")
            Button(onClick = resetPublicParameters, modifier = modifier.fillMaxWidth()) {
                Text("Reset Public Parameters")
            }
        } else {
            if (ageProofUiState.ppGenerationInProgress) {
                Text("Public parameters generation in progress")
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            } else {
                Button(onClick = generatePublicParameters, modifier = modifier.fillMaxWidth()) {
                    Text("Generate Parameters")
                }
            }
        }
        if (!ageProofUiState.publicParameters.isEmpty()) {
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
                ppGenerationInProgress = true,
                ppGenerated = false,
            ),
            {},
            {},
            modifier = Modifier.padding(16.dp)
        )
    }
}