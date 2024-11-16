package aadhaar.ageproof.ui.scanner.screen

import aadhaar.ageproof.R
import aadhaar.ageproof.databinding.BarcodeLayoutBinding
import aadhaar.ageproof.findActivity
import aadhaar.ageproof.ui.scanner.navhost.NavigationItem
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.camera.CameraSettings

@Composable
fun ScanScreen(
    navController: NavController,
    modifier: Modifier = Modifier.Companion,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Companion.CenterHorizontally,
    ) {
        val context = LocalContext.current
        AndroidView(factory = {
            View.inflate(it, R.layout.barcode_layout, null)
        }, update = {
            val beepManager = BeepManager(context.findActivity())
            beepManager.isBeepEnabled = true
            beepManager.isVibrateEnabled = true
            val binding = BarcodeLayoutBinding.bind(it)
            binding.barcodeView.resume()
            val s = CameraSettings()
            s.requestedCameraId = 0 // front/back/etc
            binding.barcodeView.cameraSettings = s
            binding.barcodeView.decodeSingle(object : BarcodeCallback {
                override fun barcodeResult(result: BarcodeResult?) {
                    beepManager.playBeepSound()
                    val resultText = result?.result?.text
                    var intent = Intent()
                    intent.data = Uri.parse(resultText)
                    binding.barcodeView.pause()
                    val a = context.findActivity()
                    a.setResult(Activity.RESULT_OK, intent)
                    a.finish()
                }

                override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
                    super.possibleResultPoints(resultPoints)
                }
            })
        })
        Spacer(modifier = Modifier.Companion.height(8.dp))
        Text("OR")
        Spacer(modifier = Modifier.Companion.height(8.dp))
        Button(onClick = {
            navController.navigate(NavigationItem.ImagePicker.route)
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_image),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.Companion.width(8.dp))
            Text(stringResource(R.string.select_code_form_device))
        }
    }
}