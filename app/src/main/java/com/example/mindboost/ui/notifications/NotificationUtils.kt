import android.content.Context
import android.content.Intent

class NotificationUtils {

    companion object {

        fun shareResultOnSocialMedia(context: Context, resultText: String) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, resultText)
            context.startActivity(Intent.createChooser(shareIntent, "Share Result"))
        }
    }
}
