package com.kaelesty.madprojects_kmp.blocs.project.messenger.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import entities.Message

@Composable
fun ChatContent(
	component: ChatComponent
) {

	//val state by component.state.collectAsState()

	val state = ChatComponent.State(
		chatId = 0,
		chatTitle = "Общий чат",
		senders = listOf(
			ChatComponent.State.MessageSender("MadTeacher", 0, "https://www.ocregister.com/wp-content/uploads/migration/nru/nruwt7-feature1176977504.jpg?w=535"),
			ChatComponent.State.MessageSender("Gri Aristov", 1, "https://i1.sndcdn.com/artworks-i9SJBYgRsg9zb7ua-Pg6XLw-t500x500.jpg")
		),
		readBlocks = mutableListOf(
			ChatComponent.State.MessageBlock(
				type = ChatComponent.State.MessageBlock.MessageBlockType.Incoming,
				senderId = 0,
				messages = listOf(
					Message(0, "Я в своем познании настолько преисполнился, что я как будто бы уже сто триллионов миллиардов лет проживаю на триллионах и" +
							"триллионах таких же планет, как эта Земля, мне этот мир абсолютно" +
							"понятен, и я здесь ищу только одного - покоя, умиротворения и" +
							"вот этой гармонии, от слияния с бесконечно вечным, от созерцания" +
							"великого фрактального подобия и от вот этого замечательного всеединства существа, бесконечно вечного, куда ни посмотри, хоть вглубь - бесконечно малое, хоть ввысь - бесконечное большое, понимаешь? А ты мне опять со своим вот этим, иди суетись дальше, это твоё распределение, это твой путь и твой горизонт познания и ощущения твоей природы, он несоизмеримо мелок по сравнению с моим, понимаешь? Я как будто бы уже давно глубокий старец, бессмертный, ну или там уже почти бессмертный, который на этой планете от её самого зарождения, ещё когда только Солнце" +
							"только-только сформировалось как звезда, и вот это газопылевое облако, вот, после взрыва, Солнца, когда оно вспыхнуло, как звезда, начало формировать вот эти коацерваты, планеты, понимаешь, я на этой Земле уже как будто почти пять миллиардов лет живу и знаю её вдоль и поперёк этот весь мир, а ты мне какие-то... мне не важно на твои тачки, на твои яхты, на твои квартиры, там, на твоё благо.",
							time = 12442241,
						senderId = 0
					),
					Message(1,
						text = "Где коммиты?",
						time = 12442600,
						senderId = 0
					)
				)
			),
		).apply {
			for (i in 2..50) {
				ChatComponent.State.MessageBlock(
					type = ChatComponent.State.MessageBlock.MessageBlockType.Outcoming,
					senderId = 1,
					messages = listOf(
						Message(
							id = i,
							text = "Я был на этой планете бесконечным множеством, и круче Цезаря, и круче Наполеона, и круче всех великих, понимаешь, был, а где-то был конченым говном, ещё хуже, чем здесь. Я множество этих состояний чувствую. Где-то я был больше подобен растению, где-то я больше был подобен птице, там, червю, где-то был просто сгусток камня, это всё есть душа, понимаешь?",
							time = 12460000 + i * 10L,
							senderId = 1,
						)
					)
				)
			}
		},
		unreadBlocks = listOf(
			ChatComponent.State.MessageBlock(
				type = ChatComponent.State.MessageBlock.MessageBlockType.Incoming,
				senderId = 0,
				messages = listOf(
					Message(51,
						"на сессии расскажешь",
						time = 12542241,
						senderId = 0
					),
					Message(52,
						text = "Где коммиты?",
						time = 12542600,
						senderId = 0
					)
				)
			),
		)
	)

	val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = state.readBlocks.size)

	val blocks = state.readBlocks + state.unreadBlocks

	Column(
		modifier = Modifier
			.padding(horizontal = 8.dp)
			.padding(top = 8.dp)
	) {
		Text(
			text = state.chatTitle

		)

		LazyColumn(
			state = scrollState
		) {
			items(blocks) {
				when (it.type) {
					ChatComponent.State.MessageBlock.MessageBlockType.Incoming -> IncomingMessageBlock(
						it, sender = state.senders.first { sender -> sender.id == it.senderId }
					)
					ChatComponent.State.MessageBlock.MessageBlockType.Outcoming -> OutComingMessageBlock(
						it, sender = state.senders.first { sender -> sender.id == it.senderId }
					)
				}
			}
		}
	}
}

@Composable
fun IncomingMessageBlock(
	block: ChatComponent.State.MessageBlock,
	sender: ChatComponent.State.MessageSender
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
	) {
		AsyncImage(
			model = sender.avatarUrl,
			null,
			modifier = Modifier
				.size(80.dp)
				.aspectRatio(1f)
		)
	}
}

@Composable
fun OutComingMessageBlock(
	block: ChatComponent.State.MessageBlock,
	sender: ChatComponent.State.MessageSender
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
	) {
		AsyncImage(
			model = sender.avatarUrl,
			null,
			modifier = Modifier
				.size(80.dp)
				.aspectRatio(1f)
		)
	}
}