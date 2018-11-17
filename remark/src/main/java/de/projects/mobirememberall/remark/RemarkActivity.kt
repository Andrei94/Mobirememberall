package de.projects.mobirememberall.remark

import android.app.Activity
import android.os.Bundle
import org.jetbrains.anko.setContentView

class RemarkActivity : Activity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		RemarkComponent().setContentView(this)
	}
}
