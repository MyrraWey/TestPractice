package com.muravyovdmitr.tests.testproject.SaveUser

import rx.Observable

/**
 * Created by Dima Muravyov on 07.11.2017.
 */
interface SaveUser {

	interface View {

		fun showError(message: String?)

		fun showShortNameError()

		fun showUserSavedWithAction(userName: String, actionName: String? = null, action: (() -> Unit)? = null)

		fun clearName()

		fun hideKeyboard()

		fun updateCounter(count: Int)

		fun enableButton()

		fun disableButton()

		fun showProgress()

		fun hideProgress()

		fun goToTheSavedUserActivity(userName: String)
	}

	interface Presenter {

		fun onStart()

		fun onStop()

		fun saveName(name: String)
	}

	interface Model {

		sealed class State(val savedNames: Int) {
			class Idle(savedNames: Int) : State(savedNames)
			class Processing(savedNames: Int) : State(savedNames)
			class Saved(savedNames: Int, val name: String) : State(savedNames)
			class Error(savedNames: Int, val error: Throwable) : State(savedNames)
		}

		val stateObservable: Observable<State>
		val errorObservable: Observable<Throwable>

		fun onStart()

		fun onStop()

		fun sendName(name: String)
	}
}