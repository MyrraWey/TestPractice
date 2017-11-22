package com.muravyovdmitr.tests.testproject.text_fragment

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.muravyovdmitr.tests.testproject.R
import kotlinx.android.synthetic.main.fragment_text.*

class TextFragment : Fragment() {

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater?.inflate(R.layout.fragment_text, container, false)
	}

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		bAction.setOnClickListener {
			setResultText(etSource.text.toString())
			Toast.makeText(activity, "Action complete", Toast.LENGTH_SHORT).show()
		}
	}

	fun setResultText(text: String) {
		tvResult.text = text
	}
}
