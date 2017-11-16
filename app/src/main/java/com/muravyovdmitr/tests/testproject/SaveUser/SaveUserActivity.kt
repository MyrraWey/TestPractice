package com.muravyovdmitr.tests.testproject.SaveUser

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.muravyovdmitr.tests.testproject.R
import com.muravyovdmitr.tests.testproject.SavedUser.SavedUserActivity

class SaveUserActivity : AppCompatActivity(), SaveUser.View {
	private var presenter: SaveUser.Presenter = SaveUserPresenter(this)
	private var progressDialog: ProgressDialog? = null
	private lateinit var userName: EditText
	private lateinit var saveButton: Button
	private lateinit var savedUsersCounter: TextView

	internal fun setPresenter(presenter: SaveUser.Presenter) {
		this.presenter = presenter
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_save_user)

		initViews()
		configureViews()
	}

	override fun onStart() {
		super.onStart()
		presenter.onStart()
	}

	override fun onStop() {
		presenter.onStop()
		progressDialog?.dismiss()
		super.onStop()
	}

	override fun showError(message: String?) {
		message?.let { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
	}

	override fun showShortNameError() {
		showError("Name is too short")
	}

	override fun showUserSavedWithAction(userName: String, actionName: String?, action: (() -> Unit)?) =
			Snackbar
					.make(findViewById(android.R.id.content), "SaveUser '$userName' saved", Snackbar.LENGTH_SHORT)
					.apply {
						if (actionName != null && action != null) {
							setAction(actionName, { action.invoke() })
						}
					}
					.show()

	override fun clearName() {
		userName.text.clear()
	}

	override fun hideKeyboard() {
		currentFocus?.let { view ->
			(getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
					.hideSoftInputFromWindow(view.windowToken, 0)
		}
	}

	override fun goToTheSavedUserActivity(userName: String) {
		startActivity(SavedUserActivity.createIntent(this, userName))
	}

	override fun updateCounter(count: Int) {
		savedUsersCounter.text = "saved users: $count"
	}

	override fun enableButton() {
		saveButton.isEnabled = true
	}

	override fun disableButton() {
		saveButton.isEnabled = false
	}

	override fun showProgress() {
		progressDialog?.show()
	}

	override fun hideProgress() {
		progressDialog?.hide()
	}

	private fun initViews() {
		progressDialog = createProgressDialog()
		userName = findViewById(R.id.userName)
		saveButton = findViewById(R.id.saveButton)
		savedUsersCounter = findViewById(R.id.counter)
	}

	private fun configureViews() {
		saveButton.setOnClickListener { presenter.saveName(userName.text.toString()) }
	}

	private fun createProgressDialog(): ProgressDialog =
			ProgressDialog(this).apply {
				isIndeterminate = true
				setMessage("Loading")
			}
}
