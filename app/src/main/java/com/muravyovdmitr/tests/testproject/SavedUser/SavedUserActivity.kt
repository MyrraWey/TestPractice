package com.muravyovdmitr.tests.testproject.SavedUser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.muravyovdmitr.tests.testproject.R

class SavedUserActivity : AppCompatActivity() {

	companion object {
		private val USER_NAME_EXTRA = "USER_NAME_EXTRA"

		fun createIntent(context: Context, userName: String) = Intent(context, SavedUserActivity::class.java).apply {
			putExtra(USER_NAME_EXTRA, userName)
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_saved_user)

		getUserNameFromExtras()?.let { userName -> title = userName }
	}

	private fun getUserNameFromExtras(): String? = intent.getStringExtra(USER_NAME_EXTRA)
}
