package de.projects.mobirememberall.remark

import android.view.Gravity
import android.widget.LinearLayout
import org.jetbrains.anko.*

class RemarkComponent : AnkoComponent<RemarkActivity> {
	override fun createView(ui: AnkoContext<RemarkActivity>) = with(ui) {
		verticalLayout {
			backgroundColor = context.getColor(R.color.gray)
			editText {
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
				}.lparams(width = wrapContent) {
					height = dip(40)
					topMargin = dip(10)
					horizontalMargin = dip(20)
				}
				button(context.getString(R.string.submit)) {
					textColor = context.getColor(R.color.white)
					backgroundColor = context.getColor(R.color.red)
				}.lparams(width = wrapContent) {
					height = dip(40)
					topMargin = dip(10)
					horizontalMargin = dip(20)
				}
			}
		}
	}
}