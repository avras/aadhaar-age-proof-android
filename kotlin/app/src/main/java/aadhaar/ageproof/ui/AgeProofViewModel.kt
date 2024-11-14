package aadhaar.ageproof.ui

import aadhaar.ageproof.AgeProofApplication
import aadhaar.ageproof.data.AgeProofRepository
import aadhaar.ageproof.data.AgeProofUiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AgeProofViewModel(private val ageProofRepository: AgeProofRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AgeProofUiState())
    val uiState: StateFlow<AgeProofUiState> = _uiState.asStateFlow()

    fun generatePublicParameters() {
        _uiState.update { currentState ->
            currentState.copy(
                publicParameters = String(),
                ppGenerated = false,
                ppGenerationInProgress = true,
            )
        }

        viewModelScope.launch {
            val pp = ageProofRepository.generatePublicParameters()
            _uiState.update { currentState ->
                currentState.copy(
                    publicParameters = pp,
                    ppGenerated = true,
                    ppGenerationInProgress = false,
                )
            }
        }
    }

    fun resetPublicParameters() {
        _uiState.update { currentState ->
            currentState.copy(
                publicParameters = "Reset public parameters".toString(),
                ppGenerated = false,
                ppGenerationInProgress = false,
            )
        }

    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val ageProofRepository =
                    (this[APPLICATION_KEY] as AgeProofApplication).container.ageProofRepository
                AgeProofViewModel(
                    ageProofRepository = ageProofRepository
                )
            }
        }
    }

}

