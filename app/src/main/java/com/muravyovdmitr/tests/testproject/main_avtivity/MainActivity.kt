package com.muravyovdmitr.tests.testproject.main_avtivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.muravyovdmitr.tests.testproject.R
import com.muravyovdmitr.tests.testproject.SaveUser.SaveUserActivity
import com.muravyovdmitr.tests.testproject.single_fragment_activity.SingleFragmentActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		bActivityTest.setOnClickListener { startActivity(Intent(this, SaveUserActivity::class.java)) }
		bFragmentTest.setOnClickListener { startActivity(Intent(this, SingleFragmentActivity::class.java)) }
	}
}
