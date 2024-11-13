package aadhaar.ageproof.ui

import aadhaar.ageproof.data.AgeProofUiState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AgeProofViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AgeProofUiState())
    val uiState: StateFlow<AgeProofUiState> = _uiState.asStateFlow()

    fun setPublicParameters(pp: ByteArray) {
        _uiState.update { currentState ->
            currentState.copy(
                publicParameters = pp,
            )
        }
    }

}

