package de.projects.mobirememberall.remark

import android.app.IntentService
import android.content.Context
import android.content.Intent
import java.io.File
import java.nio.charset.Charset

private const val ACTION_SAVE_POSITIVE = "de.projects.mobirememberall.remark.action.SAVE_POSITIVE"
private const val ACTION_SAVE_NEGATIVE = "de.projects.mobirememberall.remark.action.SAVE_NEGATIVE"

private const val PAYLOAD = "de.projects.mobirememberall.remark.extra.PAYLOAD"

class RemarkSender : IntentService("RemarkSender") {
	override fun onHandleIntent(intent: Intent?) {
		when (intent?.action) {
			ACTION_SAVE_POSITIVE -> handleActionSavePositiveRemark(intent.getStringExtra(PAYLOAD))
			ACTION_SAVE_NEGATIVE -> handleActionSaveNegativeRemark(intent.getStringExtra(PAYLOAD))
		}
	}

	private fun handleActionSavePositiveRemark(payload: String) {
		File(baseContext.filesDir, "positive.log").appendText(payload + System.getProperty("line.separator"), Charset.defaultCharset())
	}

	private fun handleActionSaveNegativeRemark(payload: String) {
		File(baseContext.filesDir, "negative.log").appendText(payload + System.getProperty("line.separator"), Charset.defaultCharset())
	}

	companion object {
		fun startActionSavePositiveRemark(context: Context, payload: String) {
			val intent = Intent(context, RemarkSender::class.java).apply {
				action = ACTION_SAVE_POSITIVE
				putExtra(PAYLOAD, payload)
			}
			context.startService(intent)
		}
		fun startActionSaveNegativeRemark(context: Context, payload: String) {
			val intent = Intent(context, RemarkSender::class.java).apply {
				action = ACTION_SAVE_NEGATIVE
				putExtra(PAYLOAD, payload)
			}
			context.startService(intent)
		}
	}
}
