package com.muravyovdmitr.tests.testproject.SaveUser

import android.app.ProgressDialog
import android.support.design.widget.Snackbar
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.muravyovdmitr.tests.testproject.BuildConfig
import com.muravyovdmitr.tests.testproject.R
import com.muravyovdmitr.tests.testproject.SavedUser.SavedUserActivity
import com.muravyovdmitr.tests.testproject.shadows.ShadowSnackbar
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
import org.robolectric.Shadows
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowApplication
import org.robolectric.shadows.ShadowProgressDialog
import org.robolectric.shadows.ShadowToast

/**
 * Created by Dima Muravyov on 07.11.2017.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class,
		shadows = arrayOf(ShadowSnackbar::class),
		instrumentedPackages = arrayOf("android.support.design.widget"))
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
		Assert.assertEquals("Loading", Shadows.shadowOf(progressDialog).message)
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
		Assert.assertEquals(expectedIntent.component, actualIntent.component)
		Assert.assertEquals(expectedIntent.extras.size(), actualIntent.extras.size())
		Assert.assertTrue(actualIntent.extras.containsKey("USER_NAME_EXTRA"))
		Assert.assertEquals(expectedIntent.extras["USER_NAME_EXTRA"], actualIntent.extras["USER_NAME_EXTRA"])
	}

	@Test
	fun testUserSavedWithoutAction() {
		val userName = "userName"
		saveUserActivityController.create().start().resume()

		saveUserActivityController.get().showUserSavedWithAction(userName)

		val shadowSnackBar = ShadowSnackbar.getLatestSnackbar()
		Assert.assertEquals(Snackbar.LENGTH_SHORT, shadowSnackBar.duration)
		Assert.assertEquals("SaveUser '$userName' saved", ShadowSnackbar.getTextOfLatestSnackbar())
		Assert.assertEquals(true, shadowSnackBar.isShown)
	}
}