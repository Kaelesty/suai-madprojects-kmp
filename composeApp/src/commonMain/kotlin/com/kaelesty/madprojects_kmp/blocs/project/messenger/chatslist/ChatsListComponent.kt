package com.kaelesty.madprojects_kmp.blocs.project.messenger.chatslist

import com.kaelesty.madprojects_kmp.blocs.project.messenger.MessengerStore
import kotlinx.coroutines.flow.StateFlow

interface ChatsListComponent {

	val state: StateFlow<MessengerStore.State>
}