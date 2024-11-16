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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

class AgeProofViewModel(private val ageProofRepository: AgeProofRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AgeProofUiState())
    val uiState: StateFlow<AgeProofUiState> = _uiState.asStateFlow()

    fun generatePublicParameters() {
        _uiState.update { currentState ->
            currentState.copy(
                publicParameters = byteArrayOf(),
                ppGenerated = false,
                ppGenerationInProgress = true,
                ppGenerationTime = 0.seconds,
            )
        }
        val timeSource = TimeSource.Monotonic
        val ppStartTime = timeSource.markNow()

        viewModelScope.launch {
            val pp = ageProofRepository.generatePublicParameters()
            val ppEndTime = timeSource.markNow()
            _uiState.update { currentState ->
                currentState.copy(
                    publicParameters = pp,
                    ppGenerated = true,
                    ppGenerationInProgress = false,
                    ppGenerationTime = ppEndTime - ppStartTime,
                )
            }
        }
    }

    fun resetPublicParameters() {
        _uiState.update { currentState ->
            currentState.copy(
                publicParameters = byteArrayOf(),
                ppGenerated = false,
                ppGenerationInProgress = false,
                ppGenerationTime = 0.seconds,
            )
        }

    }

    fun generateProof() {
        _uiState.update { currentState ->
            currentState.copy(
                proofGenerated = false,
                proofGenerationInProgress = true,
                proofGenerationTime = 0.seconds,
            )
        }
        val timeSource = TimeSource.Monotonic
        val proofStartTime = timeSource.markNow()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val dateString = LocalDateTime.now().format(formatter)

        viewModelScope.launch {
            val currentDate = dateString.toByteArray()
            val proofValid = ageProofRepository.generateProof(
                uiState.value.publicParameters,
                uiState.value.qrCodeData,
                currentDate
            )
            val proofEndTime = timeSource.markNow()
            _uiState.update { currentState ->
                currentState.copy(
                    proofValid = proofValid,
                    proofGenerated = true,
                    proofGenerationInProgress = false,
                    proofGenerationTime = proofEndTime - proofStartTime,
                )
            }
        }
    }

    fun setQrCodeBytes(qr: String) {
        val resultPair = ageProofRepository.decodeQrCode(qr)
        _uiState.update { currentState ->
            currentState.copy(
                qrCodeScanStatus = resultPair.first,
                qrCodeData = resultPair.second ?: byteArrayOf(),
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

