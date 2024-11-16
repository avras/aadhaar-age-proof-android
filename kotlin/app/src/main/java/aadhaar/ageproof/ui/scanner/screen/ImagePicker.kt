package aadhaar.ageproof.ui.scanner.screen

import aadhaar.ageproof.findActivity
import aadhaar.ageproof.ui.scanner.navhost.NavigationItem
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import java.io.FileNotFoundException

@Composable
fun ImagePicker(
    navController: NavController,
) {
    val context = LocalContext.current
    var imageUris by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                try {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    val width = bitmap.width
                    val height = bitmap.height
                    val pixels = IntArray(width * height)
                    bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
                    bitmap.recycle()
                    val source = RGBLuminanceSource(width, height, pixels)
                    val bBitmap = BinaryBitmap(HybridBinarizer(source))
                    val reader = MultiFormatReader()
                    try {
                        val result = reader.decode(bBitmap)
                        val resultText = result?.text
                        var intent = Intent()
                        intent.data = Uri.parse(resultText)
                        val a = context.findActivity()
                        a.setResult(Activity.RESULT_OK, intent)
                        a.finish()
                    } catch (e: NotFoundException) {
                        Toast.makeText(context, "Invalid QR code", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }

                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            } else {
                navController.navigate(NavigationItem.Scan.route)
            }
            imageUris = uri
        }
    )
    LaunchedEffect(key1 = Unit) {
        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}