package de.projects.mobirememberall.remark

import android.app.IntentService
import android.content.Context
import android.content.Intent
import de.projects.mobirememberall.remark.Configuration.negativeFile
import de.projects.mobirememberall.remark.Configuration.positiveFile
import okhttp3.*
import java.io.File
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private const val ACTION_POST_REMARKS = "de.projects.mobirememberall.remark.action.POST_REMARKS"
private const val SERVER_NAME_PAYLOAD = "de.projects.mobirememberall.remark.extra.PAYLOAD"

class RemarksPosterService : IntentService("RemarksPosterService") {
	private var serverName = ""

	override fun onHandleIntent(intent: Intent?) {
		when (intent?.action) {
			ACTION_POST_REMARKS -> {
				serverName = intent.getStringExtra(SERVER_NAME_PAYLOAD)
				handleActionPostRemarks()
			}
		}
	}

	private fun handleActionPostRemarks() {
		try {
			if(preparePostCallForPositiveRemarks()?.execute()!!.isSuccessful)
				deleteFile(positiveFile)
		} catch (e: UnknownHostException) {
		} catch (e: SocketTimeoutException) {
		}
		try {
			if(preparePostCallForNegativeRemarks()?.execute()!!.isSuccessful)
				deleteFile(negativeFile)
		} catch (e: UnknownHostException) {
		} catch (e: SocketTimeoutException) {
		}
	}

	private fun preparePostCallForPositiveRemarks(): Call? {
		val positiveDescriptions = readFile(positiveFile)
		return OkHttpClient.Builder().build().newCall(createPostRequest("""[${positiveDescriptions.joinToString(", ") { positiveRemarkAsJson(it) }}]"""))
	}

	private fun preparePostCallForNegativeRemarks(): Call? {
		val negativeDescriptions = readFile(negativeFile)
		return OkHttpClient.Builder().build().newCall(createPostRequest("""[${negativeDescriptions.joinToString(", ") { negativeRemarkAsJson(it) }}]"""))
	}

	private fun readFile(filename: String) : List<String> {
		val file = File(baseContext.filesDir, filename)
		if(!file.exists()) {
			file.createNewFile()
			return emptyList()
		}
		return file.readLines()
	}

	private fun positiveRemarkAsJson(positiveDescription: String) = """{"description": "$positiveDescription", "quality": "positive"}"""
	private fun negativeRemarkAsJson(positiveDescription: String) = """{"description": "$positiveDescription", "quality": "negative"}"""

	private fun createPostRequest(content: String): Request {
		return Request.Builder()
				.url("http://$serverName/remarks")
				.post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), content)).build()
	}

	companion object {
		fun startPostRemarksAction(context: Context, text: String) {
			val intent = Intent(context, RemarksPosterService::class.java).apply {
				action = ACTION_POST_REMARKS
				putExtra(SERVER_NAME_PAYLOAD, text)
			}
			context.startService(intent)
		}
	}
}
