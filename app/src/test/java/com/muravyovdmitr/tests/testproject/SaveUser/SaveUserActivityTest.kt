package com.muravyovdmitr.tests.testproject.SaveUser

import android.app.ProgressDialog
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.muravyovdmitr.tests.testproject.BuildConfig
import com.muravyovdmitr.tests.testproject.R
import com.muravyovdmitr.tests.testproject.SavedUser.SavedUserActivity
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowApplication
import org.robolectric.shadows.ShadowProgressDialog
import org.robolectric.shadows.ShadowToast

/**
 * Created by Dima Muravyov on 07.11.2017.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class SaveUserActivityTest {
	private lateinit var saveUserActivityController: ActivityController<SaveUserActivity>
	@Mock
	private lateinit var presenter: SaveUser.Presenter

	@Before
	fun setUp() {
		MockitoAnnotations.initMocks(this)
		saveUserActivityController = Robolectric.buildActivity(SaveUserActivity::class.java).visible()
		saveUserActivityController.get().setPresenter(presenter)
	}

	@Test
	fun testPresenterStarted() {
		saveUserActivityController.create().start().resume()
		verify(presenter).onStart()
		verifyNoMoreInteractions(presenter)
	}

	@Test
	fun testErrorToast() {
		val errorMessage = "errorMessage"
		saveUserActivityController.create().start().resume()
		saveUserActivityController.get().showError(errorMessage)
		Assert.assertEquals(ShadowToast.getTextOfLatestToast(), errorMessage)
		Assert.assertEquals(ShadowToast.getLatestToast().duration, Toast.LENGTH_SHORT)
	}

	@Test
	fun testEmptyState() {
		saveUserActivityController.create().start().resume()
		val saveUserActivity = saveUserActivityController.get()
		Assert.assertEquals(true, saveUserActivity.findViewById<Button>(R.id.saveButton).isEnabled)
		Assert.assertEquals(true, saveUserActivity.findViewById<EditText>(R.id.userName).text.isEmpty())
		Assert.assertEquals("User name", saveUserActivity.findViewById<EditText>(R.id.userName).hint)
		Assert.assertEquals("", saveUserActivity.findViewById<TextView>(R.id.counter).text)
	}

	@Test
	fun testProgressDialogDisplayed() {
		saveUserActivityController.create().start().resume()
		val saveUserActivity = saveUserActivityController.get()

		saveUserActivity.showProgress()

		val progressDialog = ShadowProgressDialog.getShownDialogs().firstOrNull() as? ProgressDialog
		Assert.assertEquals(true, progressDialog?.isIndeterminate)
		Assert.assertEquals(true, progressDialog?.isShowing)
		//TODO check dialog message
	}

	@Test
	fun testSavedUserActivityStarted() {
		val userName = "userName"
		saveUserActivityController.create().start().resume()
		val saveUserActivity = saveUserActivityController.get()

		saveUserActivity.goToTheSavedUserActivity(userName)

		val expectedIntent = SavedUserActivity.createIntent(RuntimeEnvironment.application, userName)
		val actualIntent = ShadowApplication.getInstance().nextStartedActivity
		//TODO incorrect behavior, return true even for intent without EXTRAs
		Assert.assertTrue(expectedIntent.filterEquals(actualIntent))
	}
}