package de.projects.mobirememberall.remark

import android.app.IntentService
import android.content.Context
import android.content.Intent
import okhttp3.*
import java.io.File
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private const val ACTION_POST_REMARKS = "de.projects.mobirememberall.remark.action.POST_REMARKS"

class RemarksPosterService : IntentService("RemarksPosterService") {
	override fun onHandleIntent(intent: Intent?) {
		when (intent?.action) {
			ACTION_POST_REMARKS -> {
				handleActionPostRemarks()
			}
		}
	}

	private fun handleActionPostRemarks() {
		try {
			preparePostCallForPositiveRemarks()?.execute()
		} catch (e: UnknownHostException) {
		} catch (e: SocketTimeoutException) {
		}
		try {
			preparePostCallForNegativeRemarks()?.execute()
		} catch (e: UnknownHostException) {
		} catch (e: SocketTimeoutException) {
		}
	}

	private fun preparePostCallForPositiveRemarks(): Call? {
		val positiveDescriptions = File(baseContext.filesDir, "positive.log").readLines()
		return OkHttpClient.Builder().build().newCall(createPostRequest("""[${positiveDescriptions.joinToString(", ") { positiveRemarkAsJson(it) }}]"""))
	}

	private fun preparePostCallForNegativeRemarks(): Call? {
		val positiveDescriptions = File(baseContext.filesDir, "negative.log").readLines()
		return OkHttpClient.Builder().build().newCall(createPostRequest("""[${positiveDescriptions.joinToString(", ") { negativeRemarkAsJson(it) }}]"""))
	}


	private fun positiveRemarkAsJson(positiveDescription: String) = """{"description": "$positiveDescription", "quality": "positive"}"""
	private fun negativeRemarkAsJson(positiveDescription: String) = """{"description": "$positiveDescription", "quality": "negative"}"""

	private fun createPostRequest(content: String): Request {
		return Request.Builder()
				.url("http://192.168.0.104:8080/remarks")
				.post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), content)).build()
	}

	companion object {
		fun startPostRemarksAction(context: Context) {
			val intent = Intent(context, RemarksPosterService::class.java).apply {
				action = ACTION_POST_REMARKS
			}
			context.startService(intent)
		}
	}
}
