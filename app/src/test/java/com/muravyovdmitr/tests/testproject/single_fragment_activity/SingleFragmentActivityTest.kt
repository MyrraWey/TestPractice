package com.muravyovdmitr.tests.testproject.single_fragment_activity

import com.muravyovdmitr.tests.testproject.BuildConfig
import com.muravyovdmitr.tests.testproject.R
import com.muravyovdmitr.tests.testproject.text_fragment.TextFragment
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


/**
 * Created by Dima Muravyov on 23.11.2017.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class SingleFragmentActivityTest {

	@Test
	fun testFragmentFromActivity() {
		val activity = Robolectric.buildActivity(SingleFragmentActivity::class.java).create().visible().get()
		val fragment: TextFragment = activity.fragmentManager.findFragmentById(R.id.flFrame) as TextFragment
		Assert.assertNotNull(fragment)
	}
}