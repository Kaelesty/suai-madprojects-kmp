package com.kaelesty.madprojects_kmp.blocs.project.messenger.chatslist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects_kmp.extensions.toReadableTime
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledRoundedCard
import entities.Chat
import entities.ChatType
import madprojects.composeapp.generated.resources.Res
import madprojects.composeapp.generated.resources.chat_kard
import madprojects.composeapp.generated.resources.chat_private
import madprojects.composeapp.generated.resources.chat_public
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ChatListContent(
	component: ChatsListComponent
) {
	val state by component.state.collectAsState()

	LazyColumn{
		items(items = state.chats, key = { it.chat.id }) {
			ChatCard(
				chat = it.chat,
				modifier = Modifier
					.fillMaxWidth()
					.clickable {
						component.onChatSelected(chatId = it.chat.id)
					}
			)
		}
	}
}

@Composable
fun ChatCard(
	chat: Chat,
	modifier: Modifier = Modifier
) {
	StyledRoundedCard(
		modifier = modifier
			.padding(2.dp)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(8.dp)
		) {
			Image(
				imageVector =  vectorResource(when (chat.chatType) {
					ChatType.Public -> {
						Res.drawable.chat_public
					}
					ChatType.MembersPrivate, ChatType.CuratorPrivate  -> {
						Res.drawable.chat_private
					}
					ChatType.Kard -> {
						Res.drawable.chat_kard
					}
				}), null,
				modifier = Modifier.size(60.dp)
			)
			Spacer(Modifier.width(10.dp))
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier//.padding(vertical = 4.dp)
			) {
				Column {
					Text(
						style = MaterialTheme.typography.body2.copy(
							fontSize = 20.sp
						),
						text = chat.title
					)
					val secondaryText = if (chat.lastMessage != null) {
						buildAnnotatedString {
							withStyle(
								style = SpanStyle(
									fontStyle = MaterialTheme.typography.body2.fontStyle
								)
							) {
								append("${chat.lastMessage!!.senderId}:")
							}
							withStyle(
								style = SpanStyle(
									fontStyle = MaterialTheme.typography.body2.fontStyle,
									color = Color.Gray
								)
							) {
								append(
									if (chat.lastMessage!!.text.length > 16) {
										" ${chat.lastMessage!!.text.take(16)}..."
									}
									else {
										" ${chat.lastMessage!!.text}"
									}
								)
							}
							withStyle(
								style = SpanStyle(
									fontStyle = MaterialTheme.typography.body2.fontStyle
								)
							) {
								append("${chat.lastMessage!!.time.toReadableTime()}:")
							}
						}
					} else {
						buildAnnotatedString {
							withStyle(
								style = SpanStyle(
									fontStyle = MaterialTheme.typography.body2.fontStyle,
									color = Color.Gray
								)
							) {
								append("(нет сообщений)")
							}
						}
					}
					Text(
						style = MaterialTheme.typography.body2.copy(
							fontSize = 20.sp
						),
						text = secondaryText
					)
				}
			}
		}
	}
}