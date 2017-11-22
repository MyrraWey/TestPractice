package com.muravyovdmitr.tests.testproject.text_fragment

import android.widget.TextView
import com.muravyovdmitr.tests.testproject.BuildConfig
import kotlinx.android.synthetic.main.fragment_text.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.FragmentController
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast
import org.robolectric.util.FragmentTestUtil


/**
 * Created by Dima Muravyov on 22.11.2017.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class TextFragmentTest {
	private lateinit var fragmentController: FragmentController<TextFragment>

	@Before
	fun setUp() {
		fragmentController = FragmentController.of(TextFragment())
	}

	@Test
	fun testFragmentCreationOldWay() {
		val textFragment = TextFragment()
		FragmentTestUtil.startFragment(textFragment)
		Assert.assertNotNull(textFragment)
	}

	@Test
	fun testFragmentCreationWithoutController() {
		val textFragment = Robolectric.buildFragment(TextFragment::class.java)
		Assert.assertNotNull(textFragment)
	}

	@Test
	fun testButton() {
		val fragment = fragmentController.create().start().resume().visible().get()
		val testMessage = "testMessage"

		fragment.etSource.setText(testMessage, TextView.BufferType.EDITABLE)
		fragment.bAction.performClick()

		Assert.assertEquals(testMessage, fragment.tvResult.text)
		Assert.assertEquals(ShadowToast.getTextOfLatestToast(), "Action complete")
	}
}