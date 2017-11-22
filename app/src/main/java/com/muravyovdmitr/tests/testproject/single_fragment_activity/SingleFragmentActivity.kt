package com.muravyovdmitr.tests.testproject.single_fragment_activity

import android.app.Activity
import android.os.Bundle
import com.muravyovdmitr.tests.testproject.R
import com.muravyovdmitr.tests.testproject.text_fragment.TextFragment

class SingleFragmentActivity : Activity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_single_fragment)

		fragmentManager
				.beginTransaction()
				.add(R.id.flFrame, TextFragment())
				.commit()
	}
}
