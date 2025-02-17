package com.kaelesty.madprojects_kmp.blocs.project.messenger.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kaelesty.madprojects.view.ui.experimental.Styled
import com.kaelesty.madprojects_kmp.extensions.toReadableTime
import com.kaelesty.madprojects_kmp.ui.uikit.cards.StyledRoundedCard
import com.kaelesty.madprojects_kmp.ui.uikit.text.TransparentTextField
import madprojects.composeapp.generated.resources.Res
import madprojects.composeapp.generated.resources.right_arrow
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ChatContent(
    component: ChatComponent
) {

    val state by component.state.collectAsState()

    val scrollState = rememberLazyListState(
        initialFirstVisibleItemIndex = state.readBlocks.size -
                if (state.unreadBlocks.isNotEmpty()) 1 else 0
    )

    Scaffold(
        bottomBar = {
            ChatInput(
                onSubmit = { component.sendMessage(it) }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp)
        ) {
            Text(
                text = state.chatTitle,
                fontSize = 24.sp
            )

            LazyColumn(
                state = scrollState,
                reverseLayout = true
            ) {
                item {
                    Spacer(Modifier.height(80.dp))
                }
                items(state.unreadBlocks.reversed()) {
                    state.senders.firstOrNull { sender -> sender.id == it.senderId }.let { sender ->
                        IncomingMessageBlock(
                            it, sender = sender ?: ChatComponent.State.MessageSender(
                                "Неопознанный хакер", -1, null
                            ),
                            onShow = {
                                component.readMessage(it)
                            }
                        )
                    }

                }
                item {
                    if (state.unreadBlocks.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Непрочитанные сообщения",
                                color = Color.Gray,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }
                }
                items(state.readBlocks.reversed()) {
                    when (it.type) {
                        ChatComponent.State.MessageBlock.MessageBlockType.Incoming -> {
                            IncomingMessageBlock(
                                it,
                                sender = state.senders.firstOrNull { sender -> sender.id == it.senderId } ?: ChatComponent.State.MessageSender(
                                    "Неопознанный хакер", -2, null
                                ),
                                onShow = {}
                            )
                        }

                        ChatComponent.State.MessageBlock.MessageBlockType.Outcoming -> {
                            OutComingMessageBlock(
                                it,
                                sender = state.senders.firstOrNull { sender -> sender.id == it.senderId } ?: ChatComponent.State.MessageSender(
                                    "Неопознанный хакер", -2, null
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatInput(
    onSubmit: (String) -> Unit,
) {

    var text by rememberSaveable {
        mutableStateOf("")
    }

    StyledRoundedCard(
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomEnd = 0.dp,
            bottomStart = 0.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier.padding(6.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            TransparentTextField(
                text = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                placeholder = "Введите сообщение..."
            )
            Image(
                imageVector = vectorResource(Res.drawable.right_arrow),
                modifier = Modifier
                    .offset(y = (-16).dp)
                    .padding(2.dp)
                    .size(30.dp)
                    .clickable {
                        onSubmit(text)
                        text = ""
                    },
                contentDescription = null,
            )
        }
    }
}

@Composable
fun Avatar(
    url: String?,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = url ?: "\"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQa-Q-sLOhtUNX_f7Fo9UvrRVs8wStydNgI4TCVFbtbcERWqtz-QhBhhQIhuPyQRoUUUp8&usqp=CAU\"",
        null,
        modifier = modifier
            .padding(4.dp)
            .clip(CircleShape)
            .size(45.dp),
        contentScale = ContentScale.FillBounds,
    )
}

@Composable
fun IncomingMessageBlock(
    block: ChatComponent.State.MessageBlock,
    sender: ChatComponent.State.MessageSender,
    onShow: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Avatar(
            sender.avatarUrl,
            modifier = Modifier
                .weight(0.15f)
        )
        Column(
            modifier = Modifier
                .weight(0.8f)
        ) {
            block.messages.forEachIndexed { i, message ->
                LaunchedEffect(message.id) {
                    onShow(message.id)
                }
                Column {
                    if (i == 0) {
                        Text(
                            text = sender.name
                        )
                    }
                    StyledRoundedCard(
                        shape = RoundedCornerShape(
                            topStart = if (i == 0) 0.dp else 16.dp,
                            topEnd = 16.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    ) {
                        Text(
                            text = message.text,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    if (i == block.messages.size - 1) {
                        Text(
                            block.messages.last().time.toReadableTime(),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier
                .weight(0.05f)
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
            .fillMaxWidth(),
    ) {
        Spacer(
            modifier = Modifier
                .weight(0.15f)
        )
        Column(
            modifier = Modifier
                .weight(0.85f),
            horizontalAlignment = Alignment.End
        ) {
            block.messages.forEachIndexed { i, message ->
                Column(
                    horizontalAlignment = Alignment.End
                ) {
//                    if (i == 0) {
//                        Text(
//                            text = sender.name
//                        )
//                    }
                    StyledRoundedCard(
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = if (i == 0) 0.dp else 16.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        ),
                        backgroundColor = Styled.uiKit().colors().github_25
                    ) {
                        Text(
                            text = message.text,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    if (i == block.messages.size - 1) {
                        Text(
                            block.messages.last().time.toReadableTime(),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}