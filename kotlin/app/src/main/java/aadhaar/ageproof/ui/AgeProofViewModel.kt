package aadhaar.ageproof.ui

import aadhaar.ageproof.AgeProofApplication
import aadhaar.ageproof.KEY_PUBLIC_PARAMS
import aadhaar.ageproof.data.AgeProofRepository
import aadhaar.ageproof.data.AgeProofUiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.WorkInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AgeProofViewModel(private val ageProofRepository: AgeProofRepository) : ViewModel() {
    val uiState: StateFlow<AgeProofUiState> = ageProofRepository.outputWorkInfo
        .map { info ->
            val pp = info.outputData.getString(KEY_PUBLIC_PARAMS)
            when {
                info.state.isFinished && !pp.isNullOrEmpty() -> {
                    AgeProofUiState(
                        publicParameters = pp,
                        ppGenerated = true,
                        ppGenerationInProgress = false
                    )
                }

                info.state == WorkInfo.State.CANCELLED -> {
                    AgeProofUiState()
                }

                else -> AgeProofUiState(ppGenerationInProgress = true)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AgeProofUiState()
        )

    fun generatePublicParameters() {
        ageProofRepository.generatePublicParameters()
    }

    fun cancelPublicParameterGeneration() {
        ageProofRepository.cancelPublicParameterGeneration()
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

