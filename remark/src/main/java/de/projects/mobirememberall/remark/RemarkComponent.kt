package de.projects.mobirememberall.remark

import android.view.Gravity
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
				button(context.getString(R.string.submit)) {
					textColor = context.getColor(R.color.white)
					backgroundColor = context.getColor(R.color.green)
					onClick {
						if(remark.text.isNotBlank()) {
							RemarkSender.startActionSavePositiveRemark(ctx, remark.text.toString())
							showSavedFeedback(remark)
						} else
							showWarningFeedback()
					}
				}.lparams(width = dip(100)) {
					height = dip(40)
					topMargin = dip(20)
					horizontalMargin = dip(20)
				}
				button(context.getString(R.string.submit)) {
					textColor = context.getColor(R.color.white)
					backgroundColor = context.getColor(R.color.red)
					onClick {
						if(remark.text.isNotBlank()) {
							RemarkSender.startActionSaveNegativeRemark(ctx, remark.text.toString())
							showSavedFeedback(remark)
						} else
							showWarningFeedback()
					}
				}.lparams(width = dip(100)) {
					height = dip(40)
					topMargin = dip(20)
					horizontalMargin = dip(20)
				}
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
}