package de.projects.mobirememberall.remark

import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class RemarkComponent : AnkoComponent<RemarkActivity> {
	override fun createView(ui: AnkoContext<RemarkActivity>) = with(ui) {
		verticalLayout {
			backgroundColor = context.getColor(R.color.gray)
			val remark = editText {
				textColor = context.getColor(R.color.white)
				lines = 5
			}
			linearLayout {
				topPadding = dip(5)
				orientation = LinearLayout.VERTICAL
				gravity = Gravity.CENTER

				rememberallButtonLayout(remeberallButton(context.getString(R.string.submit)) {
					backgroundColor = context.getColor(R.color.green)
					onClick {
						if (remark.text.isNotBlank()) {
							RemarkSender.startActionSavePositiveRemark(ctx, remark.text.toString())
							showSavedFeedback(remark)
						} else
							showWarningFeedback()
					}
				})
				rememberallButtonLayout(remeberallButton(context.getString(R.string.submit)) {
					backgroundColor = context.getColor(R.color.red)
					onClick {
						if (remark.text.isNotBlank()) {
							RemarkSender.startActionSaveNegativeRemark(ctx, remark.text.toString())
							showSavedFeedback(remark)
						} else
							showWarningFeedback()
					}
				})
				val remoteServer = editText("ec2-3-84-92-47.compute-1.amazonaws.com") {
					textColor = context.getColor(R.color.white)
					lines = 1
					isEnabled = false
				}
				checkBox(context.getString(R.string.canChangeServer)) {
					textColor = context.getColor(R.color.white)
					isChecked = false
					onClick {
						remoteServer.isEnabled = !remoteServer.isEnabled
					}
				}
				rememberallButtonLayout(remeberallButton(context.getString(R.string.postRemarks)) {
					onClick {
						RemarksPosterService.startPostRemarksAction(ctx, remoteServer.text.toString())
						showSyncFeedback()
					}
				})
			}
		}
	}

	private fun AnkoContext<RemarkActivity>.showSavedFeedback(remark: EditText) {
		Toast.makeText(ctx, "Saved!", Toast.LENGTH_SHORT).show()
		remark.text.clear()
	}

	private fun AnkoContext<RemarkActivity>.showWarningFeedback() {
		Toast.makeText(ctx, "No Data!!!!", Toast.LENGTH_LONG).show()
	}

	private fun AnkoContext<RemarkActivity>.showSyncFeedback() {
		Toast.makeText(ctx, "Synced!!!!", Toast.LENGTH_LONG).show()
	}
}

private fun _LinearLayout.remeberallButton(text: String, function: (@AnkoViewDslMarker android.widget.Button).() -> Unit): Button {
	return button(text) {
		textColor = context.getColor(R.color.white)
		backgroundColor = context.getColor(R.color.blue)
		function()
	}
}

private fun _LinearLayout.rememberallButtonLayout(button: Button): Button {
	return button.lparams(width = dip(100)) {
		height = dip(40)
		topMargin = dip(20)
		horizontalMargin = dip(20)
	}
}