package com.farma.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class NewsMainViewModel @Inject constructor(
    private val getAllArticlesUseCase: Provider<GetAllArticlesUseCase>
) : ViewModel() {

    val state: StateFlow<State> = getAllArticlesUseCase.get().invoke(query = "android")
        .onEach { println(it.data) }
        .map { it.toState() }
        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)
}
