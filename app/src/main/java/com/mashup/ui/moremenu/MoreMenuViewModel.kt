package com.mashup.ui.moremenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moremenu.model.Menu
import com.example.moremenu.model.Menu.Companion.toMenu
import com.example.moremenu.model.MoreMenuSideEffect
import com.example.moremenu.model.MoreMenuState
import com.mashup.core.data.repository.MetaRepository
import com.mashup.core.data.repository.PushHistoryRepository
import com.mashup.core.network.Response
import com.mashup.core.network.dto.PushHistoryResponse
import com.mashup.core.network.dto.RnbResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreMenuViewModel @Inject constructor(
    private val metaRepository: MetaRepository,
    private val pushHistoryRepository: PushHistoryRepository
) : ViewModel() {

    private val _moreMenuState = MutableStateFlow(MoreMenuState())
    val moreMenuState = _moreMenuState.asStateFlow()

    private val _moreMenuEvent = MutableSharedFlow<MoreMenuSideEffect>()
    val moreMenuEvent = _moreMenuEvent.asSharedFlow()

    fun getMoreMenuState() {
        viewModelScope.launch {
            val rnb: Flow<Response<RnbResponse>> = flow { emit(metaRepository.getRnb()) }
            val notice: Flow<Response<PushHistoryResponse>> =
                flow { emit(pushHistoryRepository.getPushHistory(page = 1, size = 1)) }

            combine(rnb, notice) { rnbFlow, noticeFlow ->
                rnbFlow to noticeFlow
            }.collect { (rnb, notice) ->

                when {
                    rnb.isSuccess() && notice.isSuccess() -> {
                        val rnbResponse = rnb.data?.toMenu() ?: emptyList()
                        val isHasNewNotice = notice.data?.unread?.isNotEmpty() ?: false
                        _moreMenuState.value = MoreMenuState(
                            menus = rnbResponse,
                            isShowNewIcon = isHasNewNotice
                        )
                    }

                    rnb.isSuccess() && notice.isSuccess().not() -> {
                        val rnbResponse = rnb.data?.toMenu() ?: emptyList()
                        _moreMenuState.value = MoreMenuState(
                            menus = rnbResponse,
                            isShowNewIcon = false
                        )
                    }

                    rnb.isSuccess().not() -> {
                        _moreMenuState.value = MoreMenuState(
                            menus = emptyList(),
                            isShowNewIcon = false
                        )
                    }
                }
            }
        }
    }

    fun onClickBackButton() {
        viewModelScope.launch {
            _moreMenuEvent.emit(MoreMenuSideEffect.NavigateBackStack)
        }
    }

    fun onClickMenuButton(menu: Menu) {
        viewModelScope.launch {
            _moreMenuEvent.emit(MoreMenuSideEffect.NavigateMenu(menu))
        }
    }
}
