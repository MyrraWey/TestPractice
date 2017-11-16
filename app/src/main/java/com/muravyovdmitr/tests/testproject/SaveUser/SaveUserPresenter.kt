package com.muravyovdmitr.tests.testproject.SaveUser

import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Created by Dima Muravyov on 07.11.2017.
 */
class SaveUserPresenter(private val view: SaveUser.View,
						private val model: SaveUser.Model = SaveUserModel()) : SaveUser.Presenter {
	private val compositeSubscription = CompositeSubscription()

	override fun onStart() {
		model.onStart()
		compositeSubscription.addAll(
				model
						.stateObservable
						.subscribeOn(Schedulers.computation())
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe({ state -> renderState(state) }),
				model
						.errorObservable
						.subscribeOn(Schedulers.computation())
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe({ error -> renderError(error) }))
	}

	override fun onStop() {
		model.onStop()
	}

	override fun saveName(name: String) = model.sendName(name)

	private fun renderState(state: SaveUser.Model.State) {
		when (state) {
			is SaveUser.Model.State.Idle -> {
				view.hideProgress()
				view.enableButton()
				view.updateCounter(state.savedNames)
				view.hideKeyboard()
			}
			is SaveUser.Model.State.Processing -> {
				view.showProgress()
				view.disableButton()
				view.updateCounter(state.savedNames)
			}
			is SaveUser.Model.State.Saved -> {
				view.hideProgress()
				view.enableButton()
				view.showUserSavedWithAction(
						state.name,
						"View User", /*TODO replace string from presenter*/
						{ view.goToTheSavedUserActivity(state.name) })
				view.updateCounter(state.savedNames)
				view.clearName()
				view.hideKeyboard()
			}
			is SaveUser.Model.State.Error -> {
				view.hideProgress()
				view.enableButton()
				renderError(state.error)
				view.updateCounter(state.savedNames)
			}
		}
	}

	private fun renderError(error: Throwable) =
			when (error) {
				is ShortNameException -> view.showShortNameError()
				else -> view.showError(error.message)
			}
}