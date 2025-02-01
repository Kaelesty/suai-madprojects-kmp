package com.kaelesty.madprojects_kmp.blocs.project.messenger.chatslist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaelesty.madprojects.domain.repos.socket.Chat
import com.kaelesty.madprojects.domain.repos.socket.ChatType
import com.kaelesty.madprojects_kmp.extensions.toReadableTime
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledRoundedCard
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
				.padding(8.dp),
			verticalAlignment = Alignment.CenterVertically
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
				contentAlignment = Alignment.CenterStart,
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
										"\t${chat.lastMessage!!.text.take(16)}..."
									}
									else {
										" ${chat.lastMessage!!.text}"
									}
								)
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
					Row(
						verticalAlignment = Alignment.CenterVertically
					) {
						Text(
							style = MaterialTheme.typography.body2.copy(
								fontSize = 20.sp
							),
							text = secondaryText,
						)
						Spacer(Modifier.width(8.dp))
						chat.lastMessage?.let {
							Text(
								text = " ${it.time.toReadableTime()}",
								style = MaterialTheme.typography.body2.copy(
									fontSize = 12.sp,
									color = Color.Gray,
								)
							)
						}
					}

				}
			}
			Spacer(Modifier.weight(1f))
			if (chat.unreadMessagesCount != 0) {
				Text(
					text = chat.unreadMessagesCount.toString(),
					modifier = Modifier
						.border(
							BorderStroke(1.dp, Color.Blue),
							shape = RoundedCornerShape(100)
						)
						.padding(8.dp)
				)
			}
		}
	}
}