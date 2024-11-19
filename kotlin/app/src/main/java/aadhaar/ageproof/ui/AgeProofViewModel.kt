package aadhaar.ageproof.ui

import aadhaar.ageproof.AgeProofApplication
import aadhaar.ageproof.data.AgeProofRepository
import aadhaar.ageproof.data.AgeProofUiState
import aadhaar.ageproof.data.QrCodeScanStatus
import android.content.Context
import android.content.Intent
import android.util.JsonWriter
import androidx.core.content.FileProvider
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
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
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

    fun generateProof(context: Context) {
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
            val proof = ageProofRepository.generateProof(
                uiState.value.publicParameters,
                uiState.value.qrCodeData,
                currentDate
            )
            val proofEndTime = timeSource.markNow()
            _uiState.update { currentState ->
                currentState.copy(
                    proof = proof,
                    proofGenerated = true,
                    proofGenerationInProgress = false,
                    proofGenerationTime = proofEndTime - proofStartTime,
                )
            }
            createProofJsonFile(context)
        }
    }

    fun resetProof() {
        _uiState.update { currentState ->
            currentState.copy(
                proofGenerated = false,
                proofGenerationInProgress = false,
                proofGenerationTime = 0.seconds,
                proof = null,
            )
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

    fun resetQrCodeBytes() {
        _uiState.update { currentState ->
            currentState.copy(
                qrCodeScanStatus = QrCodeScanStatus.NOT_SCANNED,
                qrCodeData = byteArrayOf(),
            )
        }
    }

    private fun createProofJsonFile(context: Context) {
        var proofJsonFile = File(context.filesDir, "aadhaar-age-proof.json")
        val proof = _uiState.value.proof
        if (proof != null) {
            try {
                val writer = JsonWriter(FileWriter(proofJsonFile))
                writer.beginObject()
                writer.name("version").value(proof.version.toLong())
                writer.name("pp_hash").value(proof.ppHash)
                writer.name("num_steps").value(proof.numSteps.toLong())
                writer.name("current_date_ddmmyyyy").value(proof.currentDateDdmmyyyy)
                writer.name("snark_proof").value(proof.snarkProof)
                writer.endObject()
                writer.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun shareProofJsonFile(context: Context) {
        val jsonFile = File(context.filesDir, "aadhaar-age-proof.json")
        val uri =
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", jsonFile)

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "application/json"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Share JSON file"))
    }

    fun getProofJsonBytes(context: Context): ByteArray {
        val jsonFile = File(context.filesDir, "aadhaar-age-proof.json")
        val uri =
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", jsonFile)
        val jsonBytes = context.contentResolver.openInputStream(uri)
            ?.use<InputStream, ByteArray> { inputStream ->
                inputStream.readBytes() // Read bytes from the InputStream into a ByteArray
            } ?: ByteArray(0) // Return an empty array if InputStream is null
        return jsonBytes
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

