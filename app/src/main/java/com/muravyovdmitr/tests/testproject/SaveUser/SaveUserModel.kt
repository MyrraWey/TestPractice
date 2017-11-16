package com.muravyovdmitr.tests.testproject.SaveUser

import com.muravyovdmitr.tests.testproject.SaveUser.SaveUser.Model.State
import rx.subjects.BehaviorSubject
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit

/**
 * Created by Dima Muravyov on 07.11.2017.
 */
class SaveUserModel(private val minNameLength: Int = 5,
					state: State = State.Idle(0)) : SaveUser.Model {
	override val stateObservable: BehaviorSubject<State> = BehaviorSubject.create(state)
	override val errorObservable: PublishSubject<Throwable> = PublishSubject.create()

	private val compositeSubscription = CompositeSubscription()
	private val processUserName: PublishSubject<String> = PublishSubject.create()

	override fun onStart() {
		compositeSubscription.addAll(
				stateObservable
						.filter { state -> state is State.Saved || state is State.Error }
						.map { state -> State.Idle(state.savedNames) }
						.subscribe({ state -> stateObservable.onNext(state) }),
				processUserName
						.flatMap { name ->
							stateObservable
									.take(1)
									.map { state ->
										if (name.length < minNameLength) {
											State.Error(state.savedNames, ShortNameException())
										} else {
											State.Saved(state.savedNames + 1, name)
										}
									}
									.delay(500, TimeUnit.MILLISECONDS) //emulating long operation
									.startWith(
											stateObservable
													.take(1)
													.map<State> { state -> State.Processing(state.savedNames) })
						}
						.subscribe(
								{ state -> stateObservable.onNext(state) },
								{ error ->
									/*TODO maybe throw special exception, because chain is broken here*/
									errorObservable.onError(error)
								},
								{ /*TODO maybe throw special exception, because chain is broken here*/ }))
	}

	override fun onStop() {
		compositeSubscription.clear()
	}

	override fun sendName(name: String) = processUserName.onNext(name)
}