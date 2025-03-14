import android.content.pm.PackageManager
import android.content.pm.Signature
import java.security.MessageDigest
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    private val CHANNEL = "app/signature"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            if (call.method == "getSignature") {
                result.success(getSignature())
            } else {
                result.notImplemented()
            }
        }
    }

    private fun getSignature(): String {
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            val signatures = packageInfo.signatures
            val signature = signatures[0]
            val md = MessageDigest.getInstance("SHA1")
            val bytes = md.digest(signature.toByteArray())
            val sb = StringBuilder()
            for (i in bytes.indices) {
                sb.append(String.format("%02X:", bytes[i]))
            }
            if (sb.isNotEmpty()) {
                sb.setLength(sb.length - 1) // Remove the last ':'
            }
            return sb.toString()
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }
}
